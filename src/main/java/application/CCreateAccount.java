package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.*;
import java.time.LocalDate;

public class CCreateAccount {

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private RadioButton rbMale;
    @FXML
    private RadioButton rbFemale;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "malak";
    private final String PASSWORD = "123456";

    @FXML
    void onCreate(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        LocalDate birthdate = dpBirthdate.getValue();
        String gender = rbMale.isSelected() ? "Male" : rbFemale.isSelected() ? "Female" : "";
        String pass = txtPassword.getText();
        String confirmPass = txtConfirmPassword.getText();

        // Validate email format
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showAlert("Invalid email format");
            return;
        }
        // Validate passwords
        if (!pass.equals(confirmPass) || pass.length() < 8) {
            showAlert("Passwords must match and be at least 8 characters long");
            return;
        }
        // Check required fields
        if (gender.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthdate == null) {
            showAlert("Please fill in all required fields");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            PreparedStatement checkEmailStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM public.\"User\" WHERE email = ?"
            );
            checkEmailStmt.setString(1, email);
            ResultSet rs = checkEmailStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                showAlert("This email is already registered");
                return;
            }

            conn.setAutoCommit(false);
            PreparedStatement psUser = conn.prepareStatement(
                "INSERT INTO public.\"User\" (firstname, lastname, email, gender, birthdate, password, user_type) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            psUser.setString(1, firstName);
            psUser.setString(2, lastName);
            psUser.setString(3, email);
            psUser.setString(4, gender);
            psUser.setDate(5, Date.valueOf(birthdate));
            psUser.setString(6, pass);
            psUser.setInt(7, 1);
            psUser.executeUpdate();

            // Insert into Student table
            PreparedStatement psStudent = conn.prepareStatement(
                "INSERT INTO public.student (email) VALUES (?)"
            );
            psStudent.setString(1, email);
            psStudent.executeUpdate();

            conn.commit();
            showAlert("Account created successfully");
            goToLogin();

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    void onBack(ActionEvent event) {
        goToLogin();
    }

    private void goToLogin() {
        try {
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("ULogin.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Error loading login page");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
