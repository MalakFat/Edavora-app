package application;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CChangePassword {

    @FXML private PasswordField currentPasswordField;
    @FXML private TextField currentPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField newPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton2;
    @FXML private PasswordField repeatPasswordField;
    @FXML private TextField repeatPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton21,setNewPasswordButton;
    @FXML private Label errorLabel;
    @FXML
    private Button homeBtn;
    @FXML
    private Button addCoursesBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Button messagesBtn;
    @FXML
    private Button logoutBtn;

    private void switchScene(String fxmlFile, Button sourceButton) {
        try {
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void initialize() {
        Bindings.bindBidirectional(currentPasswordTextField.textProperty(), currentPasswordField.textProperty());
        Bindings.bindBidirectional(newPasswordTextField.textProperty(), newPasswordField.textProperty());
        Bindings.bindBidirectional(repeatPasswordTextField.textProperty(), repeatPasswordField.textProperty());
        errorLabel.setVisible(false);
        // Home screen
        homeBtn.setOnAction(e -> {
            switchScene("UStudentMain.fxml", homeBtn);
        });

        // Add courses
        addCoursesBtn.setOnAction(e -> {
            switchScene("UStudentAddCourses.fxml", addCoursesBtn);
        });

        // Profile
        profileBtn.setOnAction(e -> {
            switchScene("UStudentProfile.fxml", profileBtn);
        });

        // Messages
        messagesBtn.setOnAction(e -> {
            switchScene("UStudentMessage.fxml", messagesBtn);
        });

        // Logout
        logoutBtn.setOnAction(e -> {
            UserSession.clearSession();
            switchScene("ULogin.fxml", logoutBtn);
        });
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean visible = currentPasswordTextField.isVisible();
        currentPasswordTextField.setVisible(!visible);
        currentPasswordTextField.setManaged(!visible);
        currentPasswordField.setVisible(visible);
        currentPasswordField.setManaged(visible);
    }

    @FXML
    private void togglePasswordVisibility2() {
        boolean visible = newPasswordTextField.isVisible();
        newPasswordTextField.setVisible(!visible);
        newPasswordTextField.setManaged(!visible);
        newPasswordField.setVisible(visible);
        newPasswordField.setManaged(visible);
    }

    @FXML
    private void togglePasswordVisibility21() {
        boolean visible = repeatPasswordTextField.isVisible();
        repeatPasswordTextField.setVisible(!visible);
        repeatPasswordTextField.setManaged(!visible);
        repeatPasswordField.setVisible(visible);
        repeatPasswordField.setManaged(visible);
    }

    @FXML
    private void handleSetNewPassword() {
        String current = currentPasswordField.getText();
        String newPass = newPasswordField.getText();
        String repeat = repeatPasswordField.getText();

        // التحقق من الشروط وعرض الرسالة المناسبة في الـ Label
        if (newPass.length() < 8) {
            errorLabel.setText("New password must be at least 8 characters");
            errorLabel.setVisible(true);
            return;
        }
        if (!newPass.equals(repeat)) {
            errorLabel.setText("Passwords do not match");
            errorLabel.setVisible(true);
            return;
        }

        try (Connection conn = connectDB()) {
            String email = UserSession.getInstance().getEmail();
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM public.\"User\" WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPass = rs.getString("password");
                if (!dbPass.equals(current)) {
                    errorLabel.setText("Current password is incorrect");
                    errorLabel.setVisible(true);
                    return;
                }
            } else {
                errorLabel.setText("Current password is incorrect");
                errorLabel.setVisible(true);
                return;
            }

            // إذا وصلنا هنا يعني كل الشروط صحيحة → تحديث كلمة السر
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE public.\"User\" SET password = ? WHERE email = ?");
            updateStmt.setString(1, newPass);
            updateStmt.setString(2, email);
            int rowsUpdated = updateStmt.executeUpdate();
            switchScene("UStudentProfile.fxml",setNewPasswordButton);

            if (rowsUpdated > 0) {
                errorLabel.setVisible(false);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password Updated", "Your password has been updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Failed to update your password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while connecting to the database.");
        }
    }

    @FXML
    private void handleCancel() {
        try {
            Stage stage = (Stage) currentPasswordField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("UStudentProfile.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connectDB() throws Exception {
    	    String URL = "jdbc:postgresql://localhost:5432/postgres";
    	    String USER = "malak";
    	    String PASSWORD = "123456";
    	    return java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
    	
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
