package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CStudentSearch {

    @FXML
    private TableView<StudentAbsence> tableView;
    @FXML
    private TableColumn<StudentAbsence, String> colName;
    @FXML
    private TableColumn<StudentAbsence, String> colEmail;
    @FXML
    private TableColumn<StudentAbsence, LocalDate> colBirthDate;
    @FXML
    private TableColumn<StudentAbsence, Integer> colAge;
    @FXML
    private TableColumn<StudentAbsence, String> colGender;
    @FXML
    private TableColumn<StudentAbsence, String> colCourse;
    @FXML
    private TableColumn<StudentAbsence, Integer> colAbsences;

    @FXML
    private TextField searchNameField;
    @FXML
    private TextField searchCourseField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker birthDateField;
    @FXML
    private TextField ageField;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;

    @FXML
    private Button searchButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private ObservableList<StudentAbsence> data = FXCollections.observableArrayList();

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
    }
    

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colAbsences.setCellValueFactory(new PropertyValueFactory<>("absenceCount"));

        loadData("");

        searchButton.setOnAction(e -> {
            String name = searchNameField.getText().trim();
            String course = searchCourseField.getText().trim();
            String where = "";
            if (!name.isEmpty()) {
                where += " AND (u.firstname || ' ' || u.lastname) ILIKE '%" + name + "%'";
            }
            if (!course.isEmpty()) {
                where += " AND c.name_course ILIKE '%" + course + "%'";
            }
            loadData(where);
        });

        deleteButton.setOnAction(e -> {
            StudentAbsence selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to remove this student from this course?",
                        ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        removeStudentFromCourse(selected.getEmail(), selected.getCourseName());
                        loadData("");
                    }
                });
            }
        });

        updateButton.setOnAction(e -> {
            StudentAbsence selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a row to update", ButtonType.OK).showAndWait();
                return;
            }
            if (!searchCourseField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "You cannot change the course", ButtonType.OK).showAndWait();
                return;
            }
            String fullName = searchNameField.getText().trim();
            String email = emailField.getText().trim();
            LocalDate birthDate = birthDateField.getValue();
            String ageText = ageField.getText().trim();
            Integer newAge = null;
            if (!ageText.isEmpty()) {
                try {
                    newAge = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Age must be a valid integer", ButtonType.OK).showAndWait();
                    return;
                }
            }
            String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "");
            if (fullName.isEmpty() && email.isEmpty() && birthDate == null && newAge == null && gender.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill at least one field to update", ButtonType.OK).showAndWait();
                return;
            }
            if (birthDate != null && newAge != null) {
                int calculatedAge = LocalDate.now().getYear() - birthDate.getYear();
                if (calculatedAge != newAge) {
                    new Alert(Alert.AlertType.ERROR, "Age does not match the birth date", ButtonType.OK).showAndWait();
                    return;
                }
            }
            if (birthDate == null && newAge != null) {
                birthDate = LocalDate.now().minusYears(newAge);
            }
            if (birthDate != null && newAge == null) {
                newAge = LocalDate.now().getYear() - birthDate.getYear();
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this student's data?", ButtonType.YES, ButtonType.NO);
            LocalDate finalBirthDate = birthDate;
            Integer finalNewAge = newAge;
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try (Connection conn = connectDB()) {
                        String getSql = "SELECT firstname, lastname, email, birthdate, age, gender FROM public.\"User\" WHERE email = ?";
                        String firstName = null, lastName = null, oldEmail = null, oldGender = null;
                        LocalDate oldBirthDate = null;
                        Integer oldAge = null;
                        try (PreparedStatement getStmt = conn.prepareStatement(getSql)) {
                            getStmt.setString(1, selected.getEmail());
                            ResultSet rs = getStmt.executeQuery();
                            if (rs.next()) {
                                firstName = rs.getString("firstname");
                                lastName = rs.getString("lastname");
                                oldEmail = rs.getString("email");
                                oldBirthDate = rs.getDate("birthdate").toLocalDate();
                                oldAge = rs.getInt("age");
                                oldGender = rs.getString("gender");
                            }
                        }
                        if (!fullName.isEmpty()) {
                            if (fullName.contains(" ")) {
                                int spaceIndex = fullName.lastIndexOf(" ");
                                firstName = fullName.substring(0, spaceIndex);
                                lastName = fullName.substring(spaceIndex + 1);
                            } else {
                                firstName = fullName;
                                lastName = "";
                            }
                        }
                        if (!email.isEmpty()) oldEmail = email;
                        if (finalBirthDate != null) oldBirthDate = finalBirthDate;
                        if (finalNewAge != null) oldAge = finalNewAge;
                        if (!gender.isEmpty()) oldGender = gender;
                        String sql = "UPDATE public.\"User\" SET firstname = ?, lastname = ?, email = ?, birthdate = ?, age = ?, gender = ? WHERE email = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, firstName);
                            pstmt.setString(2, lastName);
                            pstmt.setString(3, oldEmail);
                            pstmt.setDate(4, Date.valueOf(oldBirthDate));
                            pstmt.setInt(5, oldAge);
                            pstmt.setString(6, oldGender);
                            pstmt.setString(7, selected.getEmail());
                            int updatedRows = pstmt.executeUpdate();
                            if (updatedRows > 0) {
                                new Alert(Alert.AlertType.INFORMATION, "Student data updated successfully", ButtonType.OK).showAndWait();
                                loadData("");
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

    private void loadData(String whereClause) {
        data.clear();
        String sql = "SELECT " +
                "u.firstname || ' ' || u.lastname AS fullname, " +
                "u.email, u.birthdate, u.age, u.gender, " +
                "c.name_course, a.absence_count " +
                "FROM public.absences a " +
                "JOIN public.student s ON a.student_email = s.email " +
                "JOIN public.\"User\" u ON s.email = u.email " +
                "JOIN public.course c ON a.course_id = c.course_id " +
                "WHERE 1=1 " + whereClause +
                " ORDER BY fullname";
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("fullname");
                String email = rs.getString("email");
                LocalDate birthDate = rs.getDate("birthdate").toLocalDate();
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String courseName = rs.getString("name_course");
                int absences = rs.getInt("absence_count");
                data.add(new StudentAbsence(name, email, birthDate, age, gender, courseName, absences));
            }
            tableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeStudentFromCourse(String email, String courseName) {
        String sql = "DELETE FROM public.study " +
                "WHERE student_email = ? AND course_id = (" +
                "SELECT course_id FROM public.course WHERE name_course = ? LIMIT 1)";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, courseName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class StudentAbsence {
        private String name;
        private String email;
        private LocalDate birthDate;
        private int age;
        private String gender;
        private String courseName;
        private int absenceCount;

        public StudentAbsence(String name, String email, LocalDate birthDate, int age, String gender, String courseName, int absenceCount) {
            this.name = name;
            this.email = email;
            this.birthDate = birthDate;
            this.age = age;
            this.gender = gender;
            this.courseName = courseName;
            this.absenceCount = absenceCount;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public LocalDate getBirthDate() { return birthDate; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public String getCourseName() { return courseName; }
        public int getAbsenceCount() { return absenceCount; }
    }
}
