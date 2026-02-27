package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Banned {
    @FXML private TableView<BannedStudent> bannedTableView;
    @FXML private TableColumn<BannedStudent, String> clmBannedName;
    @FXML private TableColumn<BannedStudent, String> clmBannedCourse;
    @FXML private Button btnUnban;
    private Connection conn;

    // Class to hold data for TableView
    public static class BannedStudent {
        private final String studentEmail;
        private final String courseName;
        private final int courseId;

        public BannedStudent(String studentEmail, String courseName, int courseId) {
            this.studentEmail = studentEmail;
            this.courseName = courseName;
            this.courseId = courseId;
        }

        public String getStudentEmail() {
            return studentEmail;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getCourseId() {
            return courseId;
        }
    }

    @FXML
    public void initialize() {
        // Set up TableView columns
        clmBannedName.setCellValueFactory(new PropertyValueFactory<>("studentEmail"));
        clmBannedCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        // Connect to the database and load data
        connectToDatabase();
        loadBannedStudents();
    }

    private void connectToDatabase() {
        try {
            // Replace with environment variables or configuration file in production
            String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/postgres";
            String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "malak";
            String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "123456";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            showErrorAlert("Faild Connecting", e.getMessage());
        }
    }

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
    private void handleBanned(ActionEvent event) {
        loadScene(event, "UBanned.fxml", "Banned Students");
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
            showErrorAlert("error " + title, e.getMessage());
        } finally {
            closeConnection(); // Close connection after scene change
        }
    }

    private void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong");
        alert.setHeaderText(header);
        alert.setContentText("!  " + message);
        alert.showAndWait();
    }

    private void loadBannedStudents() {
        ObservableList<BannedStudent> bannedList = FXCollections.observableArrayList();
        String query = "SELECT b.student_email, c.name_course, b.course_id FROM public.banned b JOIN public.course c ON b.course_id = c.course_id";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                bannedList.add(new BannedStudent(
                    rs.getString("student_email"),
                    rs.getString("name_course"),
                    rs.getInt("course_id")
                ));
            }
            bannedTableView.setItems(bannedList);
        } catch (SQLException e) {
            showErrorAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void unbannedSelectedStudent() {
        BannedStudent selected = bannedTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("!");
            confirm.setHeaderText("");
            confirm.setContentText("Are you sure ? " );
            if (confirm.showAndWait().get() == ButtonType.OK) {
                String query = "DELETE FROM public.banned WHERE student_email = ? AND course_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, selected.getStudentEmail());
                    pstmt.setInt(2, selected.getCourseId());
                    pstmt.executeUpdate();
                    loadBannedStudents();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Done");
                    alert.showAndWait();
                } catch (SQLException e) {
                    showErrorAlert("Error", e.getMessage());
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a student");
            alert.showAndWait();
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                showErrorAlert("Error", e.getMessage());
            }
        }
    }
}