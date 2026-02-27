package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class CManagerCourse implements Initializable{

    @FXML
    private FlowPane slidesFlowPane, linksFlowPane;

    @FXML
    private ScrollPane scroll, linkScroll;

    @FXML
    private TextArea text;

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

    // ✅ جدول الغيابات
    @FXML private TableView<LectureRow> table;
    @FXML private TableColumn<LectureRow, String> dateColumn;
    @FXML private TableColumn<LectureRow, Integer> statusColumn;

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

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
    }
    @FXML
    private void handleBanned(ActionEvent event) {
        loadScene(event, "UBanned.fxml", "Banned Students");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ربط الجدول
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLectureDate()));
        statusColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAbsenceCount()).asObject());

        // تلوين الخلايا
        statusColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LectureRow, Integer> call(TableColumn<LectureRow, Integer> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Integer count, boolean empty) {
                        super.updateItem(count, empty);
                        if (empty || count == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.valueOf(count));
                            setAlignment(Pos.CENTER);
                            if (count > 5) {
                                setStyle("-fx-background-color: lightcoral; -fx-font-weight: bold;");
                            } else if (count > 0) {
                                setStyle("-fx-background-color: khaki;");
                            } else {
                                setStyle("-fx-background-color: lightgreen;");
                            }
                        }
                    }
                };
            }
        });
    }

    public void setCourseId(int courseId) {
        loadSlides(courseId);
        loadLinks(courseId);
        loadNotes(courseId);
        loadTable(courseId); // ✅ تحميل الغيابات
    }

    private void styleFlowPane(FlowPane pane, ScrollPane scrollPane) {
        pane.setPadding(new Insets(30, 10, 30, 0));
        pane.setHgap(40);
        pane.setVgap(30);
        pane.setAlignment(Pos.TOP_CENTER);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private VBox createCard(String title, Hyperlink link) {
        VBox box = new VBox(15);
        box.setStyle(
                "-fx-padding: 20;" +
                "-fx-background-color: white;" +
                "-fx-border-color: #271048;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        box.getChildren().addAll(titleLabel, link);
        return box;
    }

    private void adjustCardWidth(ScrollPane scrollPane, FlowPane flowPane, VBox card) {
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue() - 80;
            double cardWidth = (totalWidth - flowPane.getHgap()) / 2;
            card.setPrefWidth(cardWidth);
        });
    }

    private void loadSlides(int courseId) {
        styleFlowPane(slidesFlowPane, scroll);

        String sql = "SELECT slide_title, slide_link FROM public.slide WHERE course_id = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("slide_title");
                String url = rs.getString("slide_link");

                Hyperlink link = new Hyperlink("📄 Open Slide");
                link.setStyle("-fx-text-fill: #0078D7; -fx-font-size: 14px;");
                link.setOnAction(e -> {
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                VBox card = createCard(title, link);
                slidesFlowPane.getChildren().add(card);
                adjustCardWidth(scroll, slidesFlowPane, card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLinks(int courseId) {
        styleFlowPane(linksFlowPane, linkScroll);

        String sql = "SELECT link_title, link_url FROM public.link WHERE course_id = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("link_title");
                String url = rs.getString("link_url");

                Hyperlink link = new Hyperlink(url);
                link.setStyle("-fx-text-fill: #0078D7; -fx-font-size: 14px;");
                link.setOnAction(e -> {
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                VBox card = createCard(title, link);
                linksFlowPane.getChildren().add(card);
                adjustCardWidth(linkScroll, linksFlowPane, card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes(int courseId) {
        String sql = "SELECT notic FROM public.course WHERE course_id = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String notes = rs.getString("notic");
                text.setText(notes != null ? notes : "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ دالة تحميل الغيابات
    private void loadTable(int courseId) {
        ObservableList<LectureRow> data = FXCollections.observableArrayList();

        String sql = """
            SELECT l.lecture_date, COUNT(a.attendance_status) AS absence_count
            FROM public.lecture l
            LEFT JOIN public.attendance a
                ON a.course_id = l.course_id
                AND a.lecture_date = l.lecture_date
                AND a.attendance_status = 'absent'
            WHERE l.course_id = ?
            GROUP BY l.lecture_date
            ORDER BY l.lecture_date;
        """;

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getDate("lecture_date").toString();
                int count = rs.getInt("absence_count");
                data.add(new LectureRow(date, count));
            }

            table.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ كلاس يمثل الصفوف
    public static class LectureRow {
        private final SimpleStringProperty lectureDate;
        private final SimpleIntegerProperty absenceCount;

        public LectureRow(String lectureDate, int absenceCount) {
            this.lectureDate = new SimpleStringProperty(lectureDate);
            this.absenceCount = new SimpleIntegerProperty(absenceCount);
        }

        public String getLectureDate() { return lectureDate.get(); }
        public int getAbsenceCount() { return absenceCount.get(); }
    }
}
