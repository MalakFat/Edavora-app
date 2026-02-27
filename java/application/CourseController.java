package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseController {

    @FXML
    private TableView<Course> tableView;
    @FXML
    private TableColumn<Course, String> clmname;
    @FXML
    private TableColumn<Course, Integer> clmmax;
    @FXML
    private TableColumn<Course, String> clmteachername;
    @FXML
    private TableColumn<Course, String> clmstart;
    @FXML
    private TableColumn<Course, String> clmend;
    @FXML
    private TableColumn<Course, Integer> clmmax1; 
    @FXML
    private TextField name;
    @FXML
    private TextField maxcapacity;
    @FXML
    private ComboBox<String> teachername; 
    @FXML
    private TextField courseen;
    @FXML
    private ComboBox<String> FH; 
    @FXML
    private ComboBox<String> FM; 
    @FXML
    private ComboBox<String> TH;
    @FXML
    private ComboBox<String> TM; 
    @FXML
    private Button btnsearch;
    @FXML
    private Button btnremove;
    @FXML
    private Button btnupdat;
  

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private boolean searchPerformed = false;
    private Map<String, String> teacherMap = new HashMap<>(); 

    // كائن يمثل الدورة
    public static class Course {
        private final int courseId;
        private final String nameCourse;
        private final String startTime;
        private final String endTime;
        private final int maxCapacity;
        private final String teacherName;
        private final String teacherEmail;
        private final int enrolledStudents;

        public Course(int courseId, String nameCourse, String startTime, String endTime, int maxCapacity, String teacherName, String teacherEmail, int enrolledStudents) {
            this.courseId = courseId;
            this.nameCourse = nameCourse;
            this.startTime = startTime;
            this.endTime = endTime;
            this.maxCapacity = maxCapacity;
            this.teacherName = teacherName;
            this.teacherEmail = teacherEmail;
            this.enrolledStudents = enrolledStudents;
        }

        public int getCourseId() {
            return courseId;
        }

        public String getNameCourse() {
            return nameCourse;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public String getTeacherEmail() {
            return teacherEmail;
        }

        public int getEnrolledStudents() {
            return enrolledStudents;
        }
    }

    // الاتصال بقاعدة البيانات
    private Connection connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "malak";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }

    // تحميل أسماء المعلمين من قاعدة البيانات إلى ComboBox
    private void loadTeachers() {
        teacherMap.clear();
        ObservableList<String> teachers = FXCollections.observableArrayList();
        try (Connection conn = connect()) {
            String query = """
                    SELECT u.firstname || ' ' || u.lastname AS teacher_name, t.email
                    FROM teacher t
                    JOIN "User" u ON t.email = u.email
                    """;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String teacherName = rs.getString("teacher_name");
                String email = rs.getString("email");
                teachers.add(teacherName);
                teacherMap.put(teacherName, email);
            }
            teachername.setItems(teachers);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading teachers: " + e.getMessage());
        }
    }

    // تحميل البيانات من قاعدة البيانات
    private void loadCoursesFromDB() {
        courseList.clear();
        try (Connection conn = connect()) {
            String query = """
                    SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity,
                           u.firstname || ' ' || u.lastname AS teacher_name,
                           c.email_teacher AS teacher_email,
                           COUNT(s.student_email) AS enrolled_students
                    FROM course c
                    JOIN teacher t ON c.email_teacher = t.email
                    JOIN "User" u ON t.email = u.email
                    LEFT JOIN study s ON c.course_id = s.course_id
                    GROUP BY c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity, u.firstname, u.lastname, c.email_teacher
                    """;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                courseList.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("name_course"),
                        rs.getTime("start_time").toString(),
                        rs.getTime("end_time").toString(),
                        rs.getInt("max_capacity"),
                        rs.getString("teacher_name"),
                        rs.getString("teacher_email"),
                        rs.getInt("enrolled_students")
                ));
            }
            tableView.setItems(courseList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading courses: " + e.getMessage());
        }
    }

    // البحث في قاعدة البيانات
    private void searchCourse() {
        String nameText = name.getText().trim();
        String maxCapacityText = maxcapacity.getText().trim();
        String teacherNameText = teachername.getValue() != null ? teachername.getValue().trim() : "";
        String courseEnText = courseen.getText().trim();
        String startTimeText = FH.getValue() != null && FM.getValue() != null ? FH.getValue() + ":" + FM.getValue() + ":00" : "";
        String endTimeText = TH.getValue() != null && TM.getValue() != null ? TH.getValue() + ":" + TM.getValue() + ":00" : "";

        if (nameText.isEmpty() && maxCapacityText.isEmpty() && teacherNameText.isEmpty() && courseEnText.isEmpty() && startTimeText.isEmpty() && endTimeText.isEmpty()) {
            showAllCourses();
            return;
        }

        StringBuilder query = new StringBuilder("""
                SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity,
                       u.firstname || ' ' || u.lastname AS teacher_name,
                       c.email_teacher AS teacher_email,
                       COUNT(s.student_email) AS enrolled_students
                FROM course c
                JOIN teacher t ON c.email_teacher = t.email
                JOIN "User" u ON t.email = u.email
                LEFT JOIN study s ON c.course_id = s.course_id
                WHERE 1=1
                """);
        ArrayList<Object> params = new ArrayList<>();

        if (!nameText.isEmpty()) {
            query.append(" AND LOWER(c.name_course) LIKE LOWER(?)");
            params.add("%" + nameText + "%");
        }
        if (!maxCapacityText.isEmpty()) {
            try {
                int maxCapacityValue = Integer.parseInt(maxCapacityText);
                query.append(" AND c.max_capacity = ?");
                params.add(maxCapacityValue);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid max capacity.");
                return;
            }
        }
        if (!teacherNameText.isEmpty()) {
            query.append(" AND LOWER(u.firstname || ' ' || u.lastname) LIKE LOWER(?)");
            params.add("%" + teacherNameText + "%");
        }
        if (!startTimeText.isEmpty()) {
            query.append(" AND c.start_time = ?");
            params.add(Time.valueOf(startTimeText));
        }
        if (!endTimeText.isEmpty()) {
            query.append(" AND c.end_time = ?");
            params.add(Time.valueOf(endTimeText));
        }

        query.append(" GROUP BY c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity, u.firstname, u.lastname, c.email_teacher");

        if (!courseEnText.isEmpty()) {
            try {
                int enrolledStudents = Integer.parseInt(courseEnText);
                query.append(" HAVING COUNT(s.student_email) = ?");
                params.add(enrolledStudents);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid number of enrolled students.");
                return;
            }
        }

        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) param);
                } else if (param instanceof Time) {
                    pstmt.setTime(i + 1, (Time) param);
                }
            }

            ResultSet rs = pstmt.executeQuery();
            courseList.clear();
            while (rs.next()) {
                courseList.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("name_course"),
                        rs.getTime("start_time").toString(),
                        rs.getTime("end_time").toString(),
                        rs.getInt("max_capacity"),
                        rs.getString("teacher_name"),
                        rs.getString("teacher_email"),
                        rs.getInt("enrolled_students")
                ));
            }

            tableView.setItems(courseList);
            searchPerformed = true;

            if (courseList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Result", "No matching courses found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching: " + e.getMessage());
        }

        // Clear all fields after search
        clearFields();
    }

    // حذف دورة
    private void removeCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a course to delete.");
            return;
        }

        try (Connection conn = connect()) {
            String deleteQuery = "DELETE FROM course WHERE course_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, selectedCourse.getCourseId());

            conn.setAutoCommit(false);
            pstmt.executeUpdate();
            conn.commit();

            showAllCourses(); // Show all courses after deletion
            tableView.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Course deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting: " + e.getMessage());
        }
    }

    // تحديث دورة
    private void updateCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a course to update.");
            return;
        }

        String nameText = name.getText().trim();
        String maxCapacityText = maxcapacity.getText().trim();
        String teacherNameText = teachername.getValue() != null ? teachername.getValue().trim() : "";
        String courseEnText = courseen.getText().trim();
        String startTimeText = FH.getValue() != null && FM.getValue() != null ? FH.getValue() + ":" + FM.getValue() + ":00" : "";
        String endTimeText = TH.getValue() != null && TM.getValue() != null ? TH.getValue() + ":" + TM.getValue() + ":00" : "";

        if (nameText.isEmpty() && maxCapacityText.isEmpty() && teacherNameText.isEmpty() && courseEnText.isEmpty() && startTimeText.isEmpty() && endTimeText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter at least one value to update.");
            return;
        }

        // معالجة عدد الطلاب
        if (!courseEnText.isEmpty()) {
            try {
                int inputEnrolled = Integer.parseInt(courseEnText);
                if (inputEnrolled != selectedCourse.getEnrolledStudents()) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "You cannot change the number of enrolled students. It must match the calculated value.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid number for enrolled students.");
                return;
            }
        }

        // تحديد القيم الجديدة للوقت والمعلم
        Time newStartTime = !startTimeText.isEmpty() ? Time.valueOf(startTimeText) : Time.valueOf(selectedCourse.getStartTime());
        Time newEndTime = !endTimeText.isEmpty() ? Time.valueOf(endTimeText) : Time.valueOf(selectedCourse.getEndTime());
        String newTeacherEmail = !teacherNameText.isEmpty() && !teacherNameText.equals(selectedCourse.getTeacherName()) 
                                 ? teacherMap.get(teacherNameText) 
                                 : selectedCourse.getTeacherEmail();

        if (newTeacherEmail == null && !teacherNameText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Selected teacher not found.");
            return;
        }

        // التحقق من أن start_time < end_time
        if (newStartTime.compareTo(newEndTime) >= 0) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Start time must be before end time.");
            return;
        }

        // التحقق من عدم وجود تعارض مع دورات أخرى لنفس المعلم
        if ((!startTimeText.isEmpty() || !endTimeText.isEmpty() || !teacherNameText.isEmpty()) && hasTimeConflict(selectedCourse.getCourseId(), newTeacherEmail, newStartTime, newEndTime)) {
            showAlert(Alert.AlertType.WARNING, "Warning", "The new time conflicts with another course for this teacher.");
            return;
        }

        try (Connection conn = connect()) {
            StringBuilder updateQuery = new StringBuilder("UPDATE course SET ");
            ArrayList<Object> params = new ArrayList<>();
            boolean updated = false;

            if (!nameText.isEmpty()) {
                updateQuery.append("name_course = ?, ");
                params.add(nameText);
                updated = true;
            }
            if (!maxCapacityText.isEmpty()) {
                try {
                    int maxCapacityValue = Integer.parseInt(maxCapacityText);
                    if (maxCapacityValue < selectedCourse.getEnrolledStudents()) {
                        showAlert(Alert.AlertType.WARNING, "Warning", "Max capacity cannot be less than the number of enrolled students (" + selectedCourse.getEnrolledStudents() + ").");
                        return;
                    }
                    updateQuery.append("max_capacity = ?, ");
                    params.add(maxCapacityValue);
                    updated = true;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid max capacity.");
                    return;
                }
            }
            if (!startTimeText.isEmpty()) {
                updateQuery.append("start_time = ?, ");
                params.add(newStartTime);
                updated = true;
            }
            if (!endTimeText.isEmpty()) {
                updateQuery.append("end_time = ?, ");
                params.add(newEndTime);
                updated = true;
            }
            if (!teacherNameText.isEmpty() && !teacherNameText.equals(selectedCourse.getTeacherName())) {
                updateQuery.append("email_teacher = ?, ");
                params.add(newTeacherEmail);
                updated = true;
            }

            if (!updated) {
                return;
            }

            updateQuery.setLength(updateQuery.length() - 2); // إزالة الفاصلة
            updateQuery.append(" WHERE course_id = ?");
            params.add(selectedCourse.getCourseId());

            PreparedStatement pstmt = conn.prepareStatement(updateQuery.toString());
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) param);
                } else if (param instanceof Time) {
                    pstmt.setTime(i + 1, (Time) param);
                }
            }

            conn.setAutoCommit(false);
            pstmt.executeUpdate();
            conn.commit();

            showAllCourses(); // Show all courses after update
            tableView.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Course updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating: " + e.getMessage());
        }
    }

    // دالة جديدة للتحقق من التعارض في الأوقات لنفس المعلم
    private boolean hasTimeConflict(int currentCourseId, String teacherEmail, Time newStart, Time newEnd) {
        try (Connection conn = connect()) {
            String query = """
                    SELECT start_time, end_time
                    FROM course
                    WHERE email_teacher = ? AND course_id != ?
                    """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, teacherEmail);
            pstmt.setInt(2, currentCourseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Time existingStart = rs.getTime("start_time");
                Time existingEnd = rs.getTime("end_time");

                // تحقق التعارض: إذا كان newStart < existingEnd و newEnd > existingStart
                if (newStart.before(existingEnd) && newEnd.after(existingStart)) {
                    return true; // تعارض
                }
            }
            return false; // لا تعارض

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while checking for conflicts: " + e.getMessage());
            return true; // في حال خطأ، نفترض تعارض للحذر
        }
    }

    // عرض كل الدورات
    private void showAllCourses() {
        loadCoursesFromDB();
        searchPerformed = false; // إعادة تعيين العلم
        clearFields();
    }

    // دالة لمسح الحقول
    private void clearFields() {
        name.clear();
        maxcapacity.clear();
        teachername.getSelectionModel().clearSelection();
        courseen.clear();
        FH.getSelectionModel().clearSelection();
        FM.getSelectionModel().clearSelection();
        TH.getSelectionModel().clearSelection();
        TM.getSelectionModel().clearSelection();
    }

    // تهيئة ComboBox للساعات والدقائق
    private void initializeTimeComboBoxes() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) {
            minutes.add(String.format("%02d", i));
        }

        FH.setItems(hours);
        FM.setItems(minutes);
        TH.setItems(hours);
        TM.setItems(minutes);
    }

    // عرض تنبيه
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        clmname.setCellValueFactory(new PropertyValueFactory<>("nameCourse"));
        clmmax.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        clmteachername.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        clmstart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        clmend.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        clmmax1.setCellValueFactory(new PropertyValueFactory<>("enrolledStudents"));

        btnsearch.setOnAction(e -> searchCourse());
        btnremove.setOnAction(e -> removeCourse());
        btnupdat.setOnAction(e -> updateCourse());
      

        
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name.setText(newSelection.getNameCourse());
                maxcapacity.setText(String.valueOf(newSelection.getMaxCapacity()));
                teachername.setValue(newSelection.getTeacherName());
                courseen.setText(String.valueOf(newSelection.getEnrolledStudents()));

                // تحليل وقت البداية
                String[] startTimeParts = newSelection.getStartTime().split(":");
                if (startTimeParts.length >= 2) {
                    FH.setValue(startTimeParts[0]); // الساعة
                    FM.setValue(startTimeParts[1]); // الدقيقة
                }

                // تحليل وقت النهاية
                String[] endTimeParts = newSelection.getEndTime().split(":");
                if (endTimeParts.length >= 2) {
                    TH.setValue(endTimeParts[0]); // الساعة
                    TM.setValue(endTimeParts[1]); // الدقيقة
                }
            } else {
                clearFields();
            }
        });

        initializeTimeComboBoxes();
        loadTeachers();
        loadCoursesFromDB();
    }
}