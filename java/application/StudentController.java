package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

public class StudentController {

    @FXML
    private TableView<StudentAbsence> tbl;
    @FXML
    private TableColumn<StudentAbsence, String> clmfiestname;
    @FXML
    private TableColumn<StudentAbsence, String> clmlastname;
    @FXML
    private TableColumn<StudentAbsence, String> clmemail;
    @FXML
    private TableColumn<StudentAbsence, LocalDate> clmbirth;
    @FXML
    private TableColumn<StudentAbsence, Integer> clmage;
    @FXML
    private TableColumn<StudentAbsence, String> clmgender;
    @FXML
    private TableColumn<StudentAbsence, String> clmcoursename;
    @FXML
    private TableColumn<StudentAbsence, Integer> clmabscount;

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private ComboBox<String> coursename;
    @FXML
    private TextField age;
    @FXML
    private DatePicker birth;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;

    @FXML
    private Button btnserch;
    @FXML
    private Button btnupdat;
    @FXML
    private Button btndelete;

    private ObservableList<StudentAbsence> data = FXCollections.observableArrayList();

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
    }

    private void loadCoursesToComboBox() {
        ObservableList<String> courseNames = FXCollections.observableArrayList();
        courseNames.add("All Courses");
        try (Connection conn = connectDB()) {
            String query = "SELECT name_course FROM public.course ORDER BY name_course";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                courseNames.add(rs.getString("name_course"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading courses: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
        coursename.setItems(courseNames);
        coursename.getSelectionModel().selectFirst();
    }

    @FXML
    public void initialize() {
        clmfiestname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        clmlastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        clmemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clmbirth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        clmage.setCellValueFactory(new PropertyValueFactory<>("age"));
        clmgender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clmcoursename.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        clmabscount.setCellValueFactory(new PropertyValueFactory<>("absenceCount"));

        loadCoursesToComboBox();
        loadData("");

        tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                firstname.setText(newSelection.getFirstname());
                lastname.setText(newSelection.getLastname());
                email.setText(newSelection.getEmail());
                birth.setValue(newSelection.getBirthDate());
                age.setText(String.valueOf(newSelection.getAge()));
                coursename.getSelectionModel().select(newSelection.getCourseName() != null && !newSelection.getCourseName().equals("None") ? newSelection.getCourseName() : "All Courses");
                if ("Male".equals(newSelection.getGender())) {
                    male.setSelected(true);
                    female.setSelected(false);
                } else if ("Female".equals(newSelection.getGender())) {
                    female.setSelected(true);
                    male.setSelected(false);
                } else {
                    male.setSelected(false);
                    female.setSelected(false);
                }
            } else {
                clearFields();
            }
        });

        btnserch.setOnAction(e -> {
            String firstNameText = firstname.getText().trim();
            String lastNameText = lastname.getText().trim();
            String course = coursename.getSelectionModel().getSelectedItem();
            if ("All Courses".equals(course)) {
                course = "";
            }
            String emailText = email.getText().trim();
            LocalDate birthDate = birth.getValue();
            String ageText = age.getText().trim();
            String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "");
            if (firstNameText.isEmpty() && lastNameText.isEmpty() && course.isEmpty() && emailText.isEmpty() && birthDate == null && ageText.isEmpty() && gender.isEmpty()) {
                showAllStudents();
                return;
            }
            String where = "";
            if (!firstNameText.isEmpty()) {
                where += " AND u.firstname ILIKE '%" + firstNameText + "%'";
            }
            if (!lastNameText.isEmpty()) {
                where += " AND u.lastname ILIKE '%" + lastNameText + "%'";
            }
            if (!course.isEmpty()) {
                where += " AND c.name_course ILIKE '%" + course + "%'";
            }
            if (!emailText.isEmpty()) {
                where += " AND u.email ILIKE '%" + emailText + "%'";
            }
            if (birthDate != null) {
                where += " AND u.birthdate = '" + Date.valueOf(birthDate) + "'";
            }
            if (!ageText.isEmpty()) {
                where += " AND u.age = " + ageText;
            }
            if (!gender.isEmpty()) {
                where += " AND u.gender = '" + gender + "'";
            }
            loadData(where);
            clearFields();
        });

        btndelete.setOnAction(e -> {
            StudentAbsence selected = tbl.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if ("None".equals(selected.getCourseName()) || "All Courses".equals(selected.getCourseName())) {
                    new Alert(Alert.AlertType.WARNING, "Cannot delete student from no course or all courses", ButtonType.OK).showAndWait();
                    return;
                }
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to remove this student from " + selected.getCourseName() + "?",
                        ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        removeStudentFromCourse(selected.getEmail(), selected.getCourseName());
                        showAllStudents();
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a student to remove from a course", ButtonType.OK).showAndWait();
            }
        });

        btnupdat.setOnAction(e -> {
            StudentAbsence selected = tbl.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a row to update", ButtonType.OK).showAndWait();
                return;
            }
            String selectedCourse = coursename.getSelectionModel().getSelectedItem();
            if (selectedCourse != null && !"All Courses".equals(selectedCourse) && !selectedCourse.equals(selected.getCourseName())) {
                new Alert(Alert.AlertType.WARNING, "You cannot change the course", ButtonType.OK).showAndWait();
                return;
            }
            String firstNameText = firstname.getText().trim();
            String lastNameText = lastname.getText().trim();
            String emailText = email.getText().trim();
            LocalDate birthDate = birth.getValue();
            String ageText = age.getText().trim();
            Integer newAge = null;
            if (!ageText.isEmpty()) {
                try {
                    newAge = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Age must be a valid integer", ButtonType.OK).showAndWait();
                    return;
                }
            }
            String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "");
            if (firstNameText.isEmpty() && lastNameText.isEmpty() && emailText.isEmpty() && birthDate == null && newAge == null && gender.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill at least one field to update", ButtonType.OK).showAndWait();
                return;
            }
            // Check if email exists if changing
            if (!emailText.isEmpty() && !emailText.equals(selected.getEmail())) {
                try (Connection conn = connectDB()) {
                    String checkSql = "SELECT COUNT(*) FROM public.\"User\" WHERE email = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                        checkStmt.setString(1, emailText);
                        ResultSet rs = checkStmt.executeQuery();
                        if (rs.next() && rs.getInt(1) > 0) {
                            new Alert(Alert.AlertType.ERROR, "This email already exists", ButtonType.OK).showAndWait();
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "An error occurred while checking the email", ButtonType.OK).showAndWait();
                    return;
                }
            }
            // Synchronize age and birthdate
            if (birthDate != null && newAge != null) {
                int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();
                if (calculatedAge != newAge) {
                    birthDate = LocalDate.now().minusYears(newAge);
                }
            } else if (birthDate == null && newAge != null) {
                birthDate = LocalDate.now().minusYears(newAge);
            } else if (birthDate != null && newAge == null) {
                newAge = Period.between(birthDate, LocalDate.now()).getYears();
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this student's data?", ButtonType.YES, ButtonType.NO);
            LocalDate finalBirthDate = birthDate;
            Integer finalNewAge = newAge;
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try (Connection conn = connectDB()) {
                        String getSql = "SELECT firstname, lastname, email, birthdate, age, gender FROM public.\"User\" WHERE email = ?";
                        String oldFirstname = null, oldLastname = null, oldEmail = null, oldGender = null;
                        LocalDate oldBirthDate = null;
                        Integer oldAge = null;
                        try (PreparedStatement getStmt = conn.prepareStatement(getSql)) {
                            getStmt.setString(1, selected.getEmail());
                            ResultSet rs = getStmt.executeQuery();
                            if (rs.next()) {
                                oldFirstname = rs.getString("firstname");
                                oldLastname = rs.getString("lastname");
                                oldEmail = rs.getString("email");
                                oldBirthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                                oldAge = rs.getInt("age");
                                oldGender = rs.getString("gender");
                            }
                        }
                        if (!firstNameText.isEmpty()) oldFirstname = firstNameText;
                        if (!lastNameText.isEmpty()) oldLastname = lastNameText;
                        if (!emailText.isEmpty()) oldEmail = emailText;
                        if (finalBirthDate != null) oldBirthDate = finalBirthDate;
                        if (finalNewAge != null) oldAge = finalNewAge;
                        if (!gender.isEmpty()) oldGender = gender;
                        String sql = "UPDATE public.\"User\" SET firstname = ?, lastname = ?, email = ?, birthdate = ?, age = ?, gender = ? WHERE email = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, oldFirstname);
                            pstmt.setString(2, oldLastname);
                            pstmt.setString(3, oldEmail);
                            pstmt.setDate(4, oldBirthDate != null ? Date.valueOf(oldBirthDate) : null);
                            pstmt.setInt(5, oldAge);
                            pstmt.setString(6, oldGender);
                            pstmt.setString(7, selected.getEmail());
                            int updatedRows = pstmt.executeUpdate();
                            if (updatedRows > 0) {
                                new Alert(Alert.AlertType.INFORMATION, "Student data updated successfully", ButtonType.OK).showAndWait();
                                showAllStudents();
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "An error occurred while updating the data", ButtonType.OK).showAndWait();
                    }
                }
            });
        });

    }

    private void clearFields() {
        firstname.clear();
        lastname.clear();
        email.clear();
        birth.setValue(null);
        age.clear();
        coursename.getSelectionModel().selectFirst();
        male.setSelected(false);
        female.setSelected(false);
    }

    private void showAllStudents() {
        loadData("");
        clearFields();
    }

    private void loadData(String whereClause) {
        data.clear();
        String sql = "SELECT " +
                "u.firstname, u.lastname, " +
                "u.email, u.birthdate, u.age, u.gender, " +
                "c.name_course, COALESCE(a.absence_count, 0) AS absence_count " +
                "FROM public.\"User\" u " +
                "JOIN public.student s ON u.email = s.email " +
                "LEFT JOIN public.study st ON s.email = st.student_email " +
                "LEFT JOIN public.course c ON st.course_id = c.course_id " +
                "LEFT JOIN (SELECT student_email, course_id, COUNT(*) AS absence_count FROM public.attendance WHERE attendance_status = 'Absent' GROUP BY student_email, course_id) a ON s.email = a.student_email AND c.course_id = a.course_id " +
                "WHERE 1=1 " + whereClause +
                " ORDER BY u.firstname, u.lastname";
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                LocalDate birthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String courseName = rs.getString("name_course") != null ? rs.getString("name_course") : "None";
                int absences = rs.getInt("absence_count");
                data.add(new StudentAbsence(firstname, lastname, email, birthDate, age, gender, courseName, absences));
            }
            tbl.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent(String email) {
        try (Connection conn = connectDB()) {
            conn.setAutoCommit(false);

            // Delete from attendance
            String deleteAttendance = "DELETE FROM public.attendance WHERE student_email = ?";
            try (PreparedStatement pstmtAttendance = conn.prepareStatement(deleteAttendance)) {
                pstmtAttendance.setString(1, email);
                pstmtAttendance.executeUpdate();
            }

            // Delete from study
            String deleteStudy = "DELETE FROM public.study WHERE student_email = ?";
            try (PreparedStatement pstmtStudy = conn.prepareStatement(deleteStudy)) {
                pstmtStudy.setString(1, email);
                pstmtStudy.executeUpdate();
            }

            // Delete from student
            String deleteStudent = "DELETE FROM public.student WHERE email = ?";
            try (PreparedStatement pstmtStudent = conn.prepareStatement(deleteStudent)) {
                pstmtStudent.setString(1, email);
                pstmtStudent.executeUpdate();
            }

            // Delete from User
            String deleteUser = "DELETE FROM public.\"User\" WHERE email = ?";
            try (PreparedStatement pstmtUser = conn.prepareStatement(deleteUser)) {
                pstmtUser.setString(1, email);
                pstmtUser.executeUpdate();
            }

            conn.commit();
            new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully", ButtonType.OK).showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the student: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void removeStudentFromCourse(String email, String courseName) {
        if ("None".equals(courseName) || "All Courses".equals(courseName)) {
            new Alert(Alert.AlertType.WARNING, "Cannot remove from no course or all courses", ButtonType.OK).showAndWait();
            return;
        }
        try (Connection conn = connectDB()) {
            conn.setAutoCommit(false);

            // Delete from attendance for the specific course
            String deleteAttendance = "DELETE FROM public.attendance WHERE student_email = ? AND course_id = (SELECT course_id FROM public.course WHERE name_course = ? LIMIT 1)";
            try (PreparedStatement pstmtAttendance = conn.prepareStatement(deleteAttendance)) {
                pstmtAttendance.setString(1, email);
                pstmtAttendance.setString(2, courseName);
                pstmtAttendance.executeUpdate();
            }

            // Delete from study for the specific course
            String deleteStudy = "DELETE FROM public.study WHERE student_email = ? AND course_id = (SELECT course_id FROM public.course WHERE name_course = ? LIMIT 1)";
            try (PreparedStatement pstmtStudy = conn.prepareStatement(deleteStudy)) {
                pstmtStudy.setString(1, email);
                pstmtStudy.setString(2, courseName);
                pstmtStudy.executeUpdate();
            }

            conn.commit();
            new Alert(Alert.AlertType.INFORMATION, "Student removed from course successfully", ButtonType.OK).showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while removing the student from the course: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public static class StudentAbsence {
        private String firstname;
        private String lastname;
        private String email;
        private LocalDate birthDate;
        private int age;
        private String gender;
        private String courseName;
        private int absenceCount;

        public StudentAbsence(String firstname, String lastname, String email, LocalDate birthDate, int age, String gender, String courseName, int absenceCount) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.birthDate = birthDate;
            this.age = age;
            this.gender = gender;
            this.courseName = courseName;
            this.absenceCount = absenceCount;
        }

        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getEmail() { return email; }
        public LocalDate getBirthDate() { return birthDate; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public String getCourseName() { return courseName; }
        public int getAbsenceCount() { return absenceCount; }
    }
}