package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CLogin {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "malak";
    private static final String DB_PASSWORD = "123456";

    private static final int USER_TYPE_STUDENT = 1;
    private static final int USER_TYPE_TEACHER = 2;
    private static final int USER_TYPE_MANAGER = 3;

    private static final String LOGIN_QUERY = "SELECT user_type, firstname FROM public.\"User\" WHERE email = ? AND password = ?";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private TextField currentPasswordTextField;

    @FXML
    private Button togglePasswordVisibilityButton;

    @FXML
    private Button login;

    @FXML
    private void initialize() {
        Bindings.bindBidirectional(currentPasswordTextField.textProperty(), currentPasswordField.textProperty());
        currentPasswordTextField.setVisible(false);
        currentPasswordTextField.setManaged(false);
        currentPasswordField.setVisible(true);
        currentPasswordField.setManaged(true);
        togglePasswordVisibilityButton.setText("Show");
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean passwordTextVisible = currentPasswordTextField.isVisible();

        currentPasswordTextField.setVisible(!passwordTextVisible);
        currentPasswordTextField.setManaged(!passwordTextVisible);

        currentPasswordField.setVisible(passwordTextVisible);
        currentPasswordField.setManaged(passwordTextVisible);

        togglePasswordVisibilityButton.setText(passwordTextVisible ? "Show" : "Hide");
    }

    @FXML
    private void openface() {
        openExternalLink("https://www.facebook.com/");
    }

    @FXML
    private void openWebsite() {
        openExternalLink("https://mail.google.com/mail/u/0/#inbox");
    }

    @FXML
    private void openinsta() {
        openExternalLink("https://www.instagram.com/");
    }

    @FXML
    private void openlinked() {
        openExternalLink("https://www.linkedin.com/");
    }

    private void openExternalLink(String url) {
        if (!Desktop.isDesktopSupported()) {
            showAlert("Error", "Desktop browsing is not supported in this environment.");
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            showAlert("Error", "Unable to open link: " + url);
        }
    }

    @FXML
    private void creatAccount(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UCreateAccount.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Account");
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Cannot open create-account screen.");
        }
    }

    @FXML
    void handleLogin() {
        String email = emailField.getText().trim();
        String password = currentPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter email and password.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(LOGIN_QUERY)) {

            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    showAlert("Error", "Invalid email or password.");
                    return;
                }

                int userType = rs.getInt("user_type");
                String firstName = rs.getString("firstname");
                UserSession.createSession(email, firstName);

                switch (userType) {
                    case USER_TYPE_STUDENT:
                        loadScene("UStudentMain.fxml");
                        break;
                    case USER_TYPE_TEACHER:
                        loadScene("UTeacherMain.fxml");
                        break;
                    case USER_TYPE_MANAGER:
                        loadScene("UManagerMain.fxml");
                        break;
                    default:
                        showAlert("Error", "Unknown user type.");
                }
            }

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) login.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Error", "Cannot load scene: " + fxmlFile);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
