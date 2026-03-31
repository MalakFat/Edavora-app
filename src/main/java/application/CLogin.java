package application;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
	 @FXML private TextField emailField;
	  @FXML private PasswordField currentPasswordField;
	    @FXML private TextField currentPasswordTextField;
	    @FXML private Button togglePasswordVisibilityButton;
	    @FXML
	    private Button login;
	    @FXML
	    private void initialize() {
	        Bindings.bindBidirectional(currentPasswordTextField.textProperty(), currentPasswordField.textProperty());
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
    private void openface() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@FXML
    private void openWebsite() {
        try {
            Desktop.getDesktop().browse(new URI("https://mail.google.com/mail/u/0/#inbox"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@FXML
    private void openinsta() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.instagram.com/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@FXML
    private void openlinked() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/"));
        } catch (Exception e) {
            e.printStackTrace();
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
	        e.printStackTrace();
	    }
	}
	 @FXML
	    void handleLogin() {
	        String email = emailField.getText();
	        String password = currentPasswordField.getText();

	        if (email.isEmpty() || password.isEmpty()) {
	            showAlert("Error", "Please enter email and password.");
	            return;
	        }

	        String query = "SELECT user_type,firstname FROM public.\"User\" WHERE email = ? AND password = ?";

	        try (
	        	 
	        	 Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "malak", "123456");
	             PreparedStatement pst = conn.prepareStatement(query)) {

	            pst.setString(1, email);
	            pst.setString(2, password);
	            ResultSet rs = pst.executeQuery();
	            
	            if (rs.next()) {
	                int userType = rs.getInt("user_type");
	                switch (userType) {
	                    case 1:
	                        UserSession.createSession(email, rs.getString("firstname"));
	                        loadScene("UStudentMain.fxml");
	                        break;
	                    case 2:
	                        UserSession.createSession(email, rs.getString("firstname"));
	                        loadScene("UTeacherMain.fxml");

	                        break;
	                    case 3:
	                        UserSession.createSession(email, rs.getString("firstname"));
	                        loadScene("UManagerMain.fxml");

	                        break;
	                    default:
	                        showAlert("Error", "Unknown user type.");
	                }
	            } else {
	                showAlert("Error", "Invalid email or password.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Database Error", e.getMessage());
	        }
	    }

	    private void loadScene(String fxmlFile) {
	        try {
	            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
	            Stage stage = (Stage) login.getScene().getWindow();
	            stage.setScene(new Scene(root));
	        } catch (Exception e) {
	            e.printStackTrace();
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
