package application;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CManagerChangePassword {

    @FXML private PasswordField currentPasswordField;
    @FXML private TextField currentPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton,setNewPasswordButton;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField newPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton2;
    @FXML private PasswordField repeatPasswordField;
    @FXML private TextField repeatPasswordTextField;
    @FXML private Button togglePasswordVisibilityButton21;
    @FXML private Label errorLabel;

    @FXML private Button home;
    @FXML private Button profile;
    @FXML private Button message;
    @FXML private Button logout;
    @FXML private Button adds;
    @FXML private Button addt;
    @FXML private Button addc;
    @FXML private Button student1;
    @FXML private Button teacher1;
    @FXML private Button course1;

    @FXML
    private void openHome(ActionEvent event) {
        loadScene(event, "UManagerMain.fxml", "Home Screen");
    }

    @FXML
    private void openProfile(ActionEvent event) {
        loadScene(event, "UManagerProfile.fxml", "Manager Profile");
    }

    @FXML
    private void openMessageWindow(ActionEvent event) {
        loadScene(event, "UManagerMessage.fxml", "Message Window");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene(event, "ULogin.fxml", "Login");
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        loadScene(event, "UAddStudent.fxml", "Add Student");
    }

    @FXML
    private void handleAddTeacher(ActionEvent event) {
        loadScene(event, "UAddTeacher.fxml", "Add Teacher");
    }

    @FXML
    private void handleAddCourse(ActionEvent event) {
        loadScene(event, "UAddCourse.fxml", "Add Course");
    }

    @FXML
    private void handleShowStudents(ActionEvent event) {
        loadScene(event, "UStudentSearch.fxml", "Student Search");
    }

    @FXML
    private void handleShowTeachers(ActionEvent event) {
        loadScene(event, "UTeacherSearch.fxml", "Teachers List");
    }

    @FXML
    private void handleShowCourses(ActionEvent event) {
        loadScene(event, "UCourseSearch.fxml", "Courses List");
    }
    @FXML
    private void handleShow1() {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/StudentReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Student Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    

    
    @FXML
    private void handleShow2(ActionEvent event) {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/TeacherReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Teacher Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }

    @FXML
    private void handleShow3(ActionEvent event) {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/CourseReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Course Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    private void loadScene(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to load " + title, e.getMessage());
        }
    }
    
    private void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("An error occurred: " + message);
        alert.showAndWait();
    }

    @FXML
    private void handleBanned(ActionEvent event) {
        loadScene(event, "UBanned.fxml", "Banned Students");
    }
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
            switchScene("UManagerProfile.fxml",setNewPasswordButton);


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
