package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CManagerMessage implements Initializable {

    @FXML private CheckBox specificPeopleCheckBox;
    @FXML private ScrollPane scrollPane;
    @FXML private TextArea txtMessage;
    @FXML private CheckBox chkTeacher;
    @FXML private CheckBox chkStudent;
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
    private FlowPane flowPane;
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
    

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "malak";
    private final String PASSWORD = "123456";

    private final List<String> selectedSpecificEmails = new ArrayList<>();
   
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flowPane = new FlowPane();
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(20, 20, 0, 20));
        flowPane.setPrefWidth(scrollPane.getWidth());
        scrollPane.setContent(flowPane);

        chkTeacher.setOnAction(e -> {
            if (chkTeacher.isSelected()) {
                specificPeopleCheckBox.setSelected(false);
                selectedSpecificEmails.clear();
            }
            
        });

        chkStudent.setOnAction(e -> {
            if (chkStudent.isSelected()) {
                specificPeopleCheckBox.setSelected(false);
                selectedSpecificEmails.clear();
            }
        });

        specificPeopleCheckBox.setOnAction(e -> {
            if (specificPeopleCheckBox.isSelected()) {
                chkTeacher.setSelected(false);
                chkStudent.setSelected(false);
                showPeopleList();
            } else {
                selectedSpecificEmails.clear();
            }
        });

        loadMessages();
    }

    private static class UserOption {
        final String display;
        final String email;
        UserOption(String display, String email) { this.display = display; this.email = email; }
        @Override public String toString() { return display; }
    }

    private void loadMessages() {
        flowPane.getChildren().clear();
        String sql = "SELECT message_id, message_content,date FROM public.\"message\" ORDER BY date DESC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("message_id");
                String content = rs.getString("message_content");
                String dateStr = rs.getString("date");
                HBox box = createMessageBox(id, content, dateStr);
                flowPane.getChildren().add(box);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPeopleList() {
        Dialog<List<UserOption>> dialog = new Dialog<>();
        dialog.setTitle("Select People");

        ListView<UserOption> listView = new ListView<>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        String sql = "SELECT firstname, lastname, email, user_type FROM public.\"User\" ORDER BY firstname, lastname";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int type = rs.getInt("user_type");
                if (type == 3) continue;
                String typeStr = (type == 1) ? "Student" : (type == 2) ? "Teacher" : "";
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                String email = rs.getString("email");
                String display = fullName + " (" + typeStr + ") — " + email;
                listView.getItems().add(new UserOption(display, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Failed to load users list.");
            specificPeopleCheckBox.setSelected(false);
            return;
        }

        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return new ArrayList<>(listView.getSelectionModel().getSelectedItems());
            }
            return null;
        });

        dialog.showAndWait().ifPresentOrElse(selected -> {
            selectedSpecificEmails.clear();
            for (UserOption u : selected) selectedSpecificEmails.add(u.email);
            if (selectedSpecificEmails.isEmpty()) {
                specificPeopleCheckBox.setSelected(false);
            }
        }, () -> {
            specificPeopleCheckBox.setSelected(false);
            selectedSpecificEmails.clear();
        });
    }

    private HBox createMessageBox(int id, String content, String dateStr) {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-border-color: #271048; -fx-border-width: 2; -fx-border-radius:20; -fx-background-radius:20; -fx-padding: 10; -fx-background-color: white;");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(20);
        hbox.setPrefWidth(700);

        Label lblContent = new Label(content);
        lblContent.setWrapText(true);
        lblContent.setPrefWidth(600);
        lblContent.setStyle("-fx-font-size: 20px; -fx-font-family: 'System';");

        Label lblDate = new Label(dateStr != null ? dateStr : "");
        lblDate.setWrapText(true);
        lblDate.setPrefWidth(600);
        lblDate.setStyle("-fx-font-size: 13px; -fx-font-family: 'System';");

        Button deleteBtn = new Button("❌");
        deleteBtn.setStyle("-fx-text-fill: red; -fx-background-color: white; -fx-font-size: 20; -fx-border-radius:50; -fx-background-radius:50;");
        deleteBtn.setOnAction(e -> {
            deleteMessage(id);
            loadMessages();
        });

        Button recipientsBtn = new Button("Recipients");
        recipientsBtn.setStyle("-fx-background-color: #271048; -fx-text-fill: white; -fx-font-size: 14; -fx-border-radius:10; -fx-background-radius:10;");
        recipientsBtn.setOnAction(e -> showRecipientsList(id));

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(lblContent, lblDate, recipientsBtn);

        hbox.getChildren().addAll(vbox, deleteBtn);
        return hbox;
    }

    private void showRecipientsList(int messageId) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Recipients");

        ListView<String> listView = new ListView<>();

        String sql = "SELECT u.firstname, u.lastname, u.user_type, u.email " +
                     "FROM public.allmessages am " +
                     "JOIN public.\"User\" u ON am.email = u.email " +
                     "WHERE am.message_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int type = rs.getInt("user_type");
                    String typeStr = (type == 1) ? "Student" : (type == 2) ? "Teacher" : "";
                    String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                    String email = rs.getString("email");
                    listView.getItems().add(fullName + " (" + typeStr + ") — " + email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Failed to load recipients list.");
            return;
        }

        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void deleteMessage(int id) {
        String delAll = "DELETE FROM public.allmessages WHERE message_id = ?";
        String delMsg = "DELETE FROM public.\"message\" WHERE message_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(delAll);
                 PreparedStatement ps2 = conn.prepareStatement(delMsg)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
                ps2.setInt(1, id);
                ps2.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error while deleting the message!");
        }
    }

    @FXML
    private void sendMessage() {
        String content = txtMessage.getText().trim();
        boolean toTeacher = chkTeacher.isSelected();
        boolean toStudent = chkStudent.isSelected();
        boolean toSpecific = specificPeopleCheckBox.isSelected();

        if (content.isEmpty()) {
            showAlert(AlertType.WARNING, "Message is empty!");
            return;
        }
        if (!toSpecific && !toTeacher && !toStudent) {
            showAlert(AlertType.WARNING, "Please select recipients (Teachers/Students) or Specific People.");
            return;
        }
        if (toSpecific && selectedSpecificEmails.isEmpty()) {
            showAlert(AlertType.WARNING, "No specific people selected.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            int messageId;
            String insertMsg = "INSERT INTO public.\"message\" (message_content,date) VALUES (?, ?) RETURNING message_id";
            try (PreparedStatement ps = conn.prepareStatement(insertMsg)) {
                ps.setString(1, content);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    messageId = rs.getInt("message_id");
                }
            }

            Set<String> recipientEmails = new LinkedHashSet<>();
            if (toTeacher) recipientEmails.addAll(getEmailsByType(conn, "teacher"));
            if (toStudent) recipientEmails.addAll(getEmailsByType(conn, "student"));
            if (toSpecific) recipientEmails.addAll(selectedSpecificEmails);

            if (recipientEmails.isEmpty()) {
                conn.rollback();
                showAlert(AlertType.WARNING, "No recipients found.");
                return;
            }

            String insertAll = "INSERT INTO public.allmessages (message_id, email) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertAll)) {
                for (String email : recipientEmails) {
                    ps.setInt(1, messageId);
                    ps.setString(2, email);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();

            showAlert(AlertType.INFORMATION, "Message sent successfully!");
            txtMessage.clear();
            chkTeacher.setSelected(false);
            chkStudent.setSelected(false);
            specificPeopleCheckBox.setSelected(false);
            selectedSpecificEmails.clear();
            loadMessages();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error while saving the message!");
        }
    }

    private List<String> getEmailsByType(Connection conn, String userType) throws SQLException {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email FROM public.\"User\" WHERE user_type = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userType.equalsIgnoreCase("teacher")) {
                ps.setInt(1, 2);
            } else if (userType.equalsIgnoreCase("student")) {
                ps.setInt(1, 1);
            } else {
                return emails;
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emails.add(rs.getString("email"));
                }
            }
        }
        return emails;
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
