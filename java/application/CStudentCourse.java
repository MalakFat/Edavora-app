package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CStudentCourse implements Initializable{

    @FXML
    private FlowPane slidesFlowPane, linksFlowPane;

    @FXML
    private ScrollPane scroll, linkScroll;

    @FXML
    private TextArea text;

    @FXML
    private Label lbl;
    
    @FXML
    private TableView<LectureRow> table;

    @FXML
    private TableColumn<LectureRow, String> dateColumn;

    @FXML
    private TableColumn<LectureRow, String> statusColumn;

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
    }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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


    public void setCourseId(int courseId) {
        loadSlides(courseId);
        loadLinks(courseId);
        loadNotes(courseId);
        loadTable(courseId);
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

    private void loadTable(int courseId) {
        ObservableList<LectureRow> data = FXCollections.observableArrayList();

        String sql = """
            SELECT l.lecture_date, a.attendance_status
            FROM public.lecture l
            LEFT JOIN public.attendance a
                ON a.course_id = l.course_id
                AND a.lecture_date = l.lecture_date
                AND a.student_email = ?
            WHERE l.course_id = ?
            ORDER BY l.lecture_date
        """;

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, UserSession.getInstance().getEmail());
            pstmt.setInt(2, courseId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getDate("lecture_date").toString();
                String status = rs.getString("attendance_status");
                data.add(new LectureRow(date, status != null ? status : "Not Recorded"));
            }
            String sqlAbsences = "SELECT absence_count FROM public.absences WHERE course_id = ? AND student_email = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlAbsences)) {
                pstmt2.setInt(1, courseId);
                pstmt2.setString(2, UserSession.getInstance().getEmail());
                try (ResultSet rs2 = pstmt2.executeQuery()) {
                    if (rs2.next()) {
                        lbl.setText("Total Absences: " + rs2.getInt("absence_count"));
                    } else {
                        lbl.setText("Total Absences: 0");
                    }
                }
            }

            table.setItems(data);
            
            table.setPadding(new Insets(-0.5, 0, 0, -0.5));
            
            table.setStyle("-fx-border-color: #271048; -fx-pref-width: 530; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;-fx-font-size: 16px;");
            dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
            dateColumn.setCellFactory(tc -> {
                TableCell<LectureRow, String> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                        setAlignment(Pos.CENTER);
                        setStyle("-fx-font-size: 14px;");
                    }
                };
                return cell;
            });
            dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            // تلوين عمود الحالة فقط
            statusColumn.setCellFactory(new Callback<>() {
                @Override
                public TableCell<LectureRow, String> call(TableColumn<LectureRow, String> param) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(String status, boolean empty) {
                            super.updateItem(status, empty);
                            if (empty || status == null) {
                                setText(null);
                                setStyle("");
                            } else {
                                setText(status);
                                if ("Present".equalsIgnoreCase(status)) {
                                    setStyle("-fx-background-color: lightgreen; -fx-alignment: CENTER;");
                                } else if ("Absent".equalsIgnoreCase(status)) {
                                    setStyle("-fx-background-color: lightcoral; -fx-alignment: CENTER;");
                                } else {
                                    setStyle("-fx-background-color: transparent; -fx-alignment: CENTER;");
                                }
                            }
                        }
                    };
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public static class LectureRow {
        private final SimpleStringProperty date;
        private final SimpleStringProperty status;

        public LectureRow(String date, String status) {
            this.date = new SimpleStringProperty(date);
            this.status = new SimpleStringProperty(status);
        }

        public String getDate() { return date.get(); }
        public String getStatus() { return status.get(); }
    }
    
}
