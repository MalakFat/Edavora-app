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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CManagerMain implements Initializable {

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
    @FXML private Text LBL;
    @FXML
    private FlowPane flow;
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
    @FXML
    private TextField search1;
    @FXML
    private Button showAllButton;

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
    	 UserSession session = UserSession.getInstance();
    	 search1.setDisable(false);
 		// أو
 		search1.setEditable(true);
		if (session != null) {
	         LBL.setText("Hello " + session.getFullName());
	     } else {
	         LBL.setText("Hello Guest");
	     }
	     loadCourses() ;
    }
    void loadCourses() {
        String queryCourse = """
            SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.photo,
                   u.firstname || ' ' || u.lastname AS teacher_name
            FROM public.course c
            JOIN public."User" u ON c.email_teacher = u.email
        """;

        boolean hasCourses = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstCourse = conn.prepareStatement(queryCourse)) {

            ResultSet rsCourse = pstCourse.executeQuery();

            if (flow == null) {
                flow = new FlowPane();
                scrollPane.setContent(flow);
            }

            while (rsCourse.next()) {
                hasCourses = true;

                int courseId = rsCourse.getInt("course_id");
                String name = rsCourse.getString("name_course");
                Time start = rsCourse.getTime("start_time");
                Time end = rsCourse.getTime("end_time");
                byte[] photoBytes = rsCourse.getBytes("photo");
                String teacherName = rsCourse.getString("teacher_name");

                addCourseCard(courseId, name, teacherName, start, end, photoBytes);
            }

            if (!hasCourses) {
                Label noCoursesLabel = new Label("No courses found.");
                noCoursesLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
                flow.getChildren().add(noCoursesLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addCourseCard(int id, String name, String Tname, Time start, Time end, byte[] photoBytes) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UCoursemanager.fxml"));
            Node card = loader.load();

            ImageView imgView = (ImageView) card.lookup("#imgView");
            Label nameLabel = (Label) card.lookup("#courseName");
            Label timeLabel = (Label) card.lookup("#courseTime");
            Label teacher = (Label) card.lookup("#TeacherName");
            Button arrowButton = (Button) card.lookup("#arrowButton");
            Button studentBtn = (Button) card.lookup("#student");
            
           
                try {
                	 if (photoBytes != null && photoBytes.length > 0) {
                    Image img = new Image(new ByteArrayInputStream(photoBytes));
                    imgView.setImage(img);}
                	 else {
                		 byte[] defaultPhotoBytes = Files.readAllBytes(Paths.get("C:/Users/Hp/OneDrive/Desktop/default.jpg"));
                		   Image img = new Image(new ByteArrayInputStream(defaultPhotoBytes));
                           imgView.setImage(img);
                	 }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            

            nameLabel.setText(name);
            teacher.setText(Tname);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startFormatted = start.toLocalTime().format(formatter);
            String endFormatted = end.toLocalTime().format(formatter);
            timeLabel.setText(startFormatted + " - " + endFormatted);

            arrowButton.setOnAction(e -> {
                try {
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("UManagerCourse.fxml"));
                    Parent root = loader2.load();
                    CManagerCourse controller = loader2.getController();
                    controller.setCourseId(id);
                    Stage stage = (Stage) arrowButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            String countQuery = "SELECT COUNT(*) FROM public.study WHERE course_id = ?";
            int studentCount = 0;
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pst = conn.prepareStatement(countQuery)) {
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    studentCount = rs.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            studentBtn.setText(""+studentCount);
            studentBtn.setStyle("-fx-text-fill:white;-fx-background-color: #271048;-fx-background-radius:50;");
            studentBtn.setPrefHeight(35);
            studentBtn.setPrefWidth(35);

            studentBtn.setOnAction(ev -> showStudentsDialog(id));

            flow.getChildren().add(card);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showStudentsDialog(int courseId) {
        Stage dialog = new Stage();
        dialog.setTitle("Registered Students");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ListView<String> listView = new ListView<>();
        Map<String, String> emailMap = new HashMap<>(); // fullName → email

        // 🔹 جلب الطلاب مع عدد الغيابات من الـ VIEW
        String sql = """
            SELECT u.firstname, u.lastname, u.email, COALESCE(a.absence_count, 0) AS absences
            FROM public."User" u
            JOIN public.study s ON u.email = s.student_email
            LEFT JOIN public.absences a 
                   ON a.course_id = s.course_id AND a.student_email = s.student_email
            WHERE s.course_id = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, courseId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                String email = rs.getString("email");
                int absences = rs.getInt("absences");

                String display = fullName + " - Absences: " + absences;
                listView.getItems().add(display);
                emailMap.put(display, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button removeBtn = new Button("Remove");
        Button banBtn = new Button("Ban");

        removeBtn.setOnAction(ev -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String email = emailMap.get(selected);
                removeStudentFromCourse(courseId, email);
                listView.getItems().remove(selected);
            } else {
                showErrorAlert("No student selected", "Please select a student first.");
            }
        });

        banBtn.setOnAction(ev -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String email = emailMap.get(selected);
                banStudentFromCourse(courseId, email);
                listView.getItems().remove(selected);
            } else {
                showErrorAlert("No student selected", "Please select a student first.");
            }
        });

        HBox buttons = new HBox(10, removeBtn, banBtn);
        buttons.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(listView, buttons);

        Scene scene = new Scene(vbox, 400, 500);
        dialog.setScene(scene);
        dialog.show();
    }

    private void removeStudentFromCourse(int courseId, String studentEmail) {
        String sql = "DELETE FROM public.study WHERE course_id = ? AND student_email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, courseId);
            pst.setString(2, studentEmail);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void banStudentFromCourse(int courseId, String studentEmail) {
        String deleteSql = "DELETE FROM public.study WHERE course_id = ? AND student_email = ?";
        String insertSql = "INSERT INTO public.banned(course_id, student_email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePst = conn.prepareStatement(deleteSql);
                 PreparedStatement insertPst = conn.prepareStatement(insertSql)) {

                deletePst.setInt(1, courseId);
                deletePst.setString(2, studentEmail);
                deletePst.executeUpdate();

                insertPst.setInt(1, courseId);
                insertPst.setString(2, studentEmail);
                insertPst.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = search1.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            for (Node node : flow.getChildren()) {
                node.setVisible(true);
                node.setManaged(true);
            }
            return;
        }

        for (Node node : flow.getChildren()) {
            Label nameLabel = (Label) node.lookup("#courseName");
            if (nameLabel != null) {
                boolean matches = nameLabel.getText().toLowerCase().contains(searchText);
                node.setVisible(matches);
                node.setManaged(matches);
            }
        }
    }
    
    
    	


}