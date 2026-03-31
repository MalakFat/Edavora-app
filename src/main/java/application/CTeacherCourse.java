package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CTeacherCourse implements Initializable {

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
    @FXML
    private FlowPane slidesFlowPane;
    @FXML
    private Button addFileButton;
    @FXML
    private FlowPane linksFlowPane;
    @FXML
    private Button addLinkButton;
    @FXML
    private Button addLinkButton4;
    @FXML
    private FlowPane flow;
    @FXML
    private Button homeBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Button messagesBtn;
    @FXML
    private Button logoutBtn;

    private int course_id;

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
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
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	  setNotic();
        homeBtn.setOnAction(e -> switchScene("UTeacherMain.fxml", homeBtn));
        profileBtn.setOnAction(e -> switchScene("UTeacherProfile.fxml", profileBtn));
        messagesBtn.setOnAction(e -> switchScene("UTeacherMessage.fxml", messagesBtn));
        logoutBtn.setOnAction(e -> {
            UserSession.clearSession();
            switchScene("ULogin.fxml", logoutBtn);
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(col -> new TableCell<LectureRow, String>() {
            private final Button btn = new Button();
            {
                btn.setMaxWidth(Double.MAX_VALUE);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    LectureRow row = getTableRow().getItem();
                    boolean entered = isLectureEntered(row.getDate());
                    if (entered) {
                        btn.setText("Entered");
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
                        btn.setText("Not entered");
                        btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                    }
                    btn.setOnAction(ev -> openAttendanceWindow(row.getDate(), btn, row));
                    setGraphic(btn);
                }
            }
        });
      

        addDeleteColumn();
    }

    public void setCourseId(int courseId) {
        loadSlides(courseId);
        loadLinks(courseId);
        course_id = courseId;
        loadLectures();
  	  setNotic();

    }

    private VBox createCard(String title, Hyperlink link, String fileUrl) {
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
        box.setPrefWidth(300);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setEllipsisString("...");
        titleLabel.setWrapText(false);
        Button deleteBtn = new Button("❌");
        deleteBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: red;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: red;" +
            "-fx-border-radius: 15px;" +
            "-fx-cursor: hand;"
        );
        deleteBtn.setMinSize(30, 30);
        deleteBtn.setMaxSize(30, 30);
        deleteBtn.setOnAction(e -> {
            deleteSlideFromDatabase(course_id, title, fileUrl);
            slidesFlowPane.getChildren().remove(box);
        });
        HBox header = new HBox(10, titleLabel, deleteBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        link.setMaxWidth(Double.MAX_VALUE);
        link.setWrapText(false);
        link.setEllipsisString("...");
        box.getChildren().addAll(header, link);
        return box;
    }

    private void loadSlides(int courseId) {
        slidesFlowPane.getChildren().clear();
        slidesFlowPane.setAlignment(Pos.TOP_CENTER);
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
                VBox card = createCard(title, link, url);
                slidesFlowPane.getChildren().add(card);
                adjustCardWidth(scroll, slidesFlowPane, card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setNotic() {
    	 String sql = "SELECT notic FROM public.course WHERE course_id = ?";
         try (Connection conn = connectDB();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setInt(1, course_id);
             ResultSet rs = pstmt.executeQuery();

             if (rs.next()) {
                 String notes = rs.getString("notic");
                 text.setText(notes != null ? notes : "");
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
    }


    private boolean slideExists(int courseId, String title, String fileUrl) {
        String sql = "SELECT 1 FROM public.slide WHERE course_id = ? AND slide_title = ? AND slide_link = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, fileUrl);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void deleteSlideFromDatabase(int courseId, String title, String fileUrl) {
        String sql = "DELETE FROM public.slide WHERE course_id = ? AND slide_title = ? AND slide_link = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, fileUrl);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Slide File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Documents", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        Stage stage = (Stage) addFileButton.getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                TextInputDialog dialog = new TextInputDialog(file.getName());
                dialog.setTitle("Slide Title");
                dialog.setHeaderText("Enter the title for the slide:");
                dialog.setContentText("Title:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().trim().isEmpty()) {
                    String title = result.get().trim();
                    String fileUrl = file.toURI().toString();
                    if (slideExists(course_id, title, fileUrl)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Duplicate Slide");
                        alert.setHeaderText(null);
                        alert.setContentText("This slide already exists.");
                        alert.showAndWait();
                    } else {
                        saveSlideToDatabase(course_id, title, fileUrl);
                        loadSlides(course_id);
                    }
                }
            }
        }
    }

    private void saveSlideToDatabase(int courseId, String title, String fileUrl) {
        String sql = "INSERT INTO public.slide (course_id, slide_title, slide_link) VALUES (?, ?, ?)";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, fileUrl);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLinks(int courseId) {
        linksFlowPane.getChildren().clear();
        linksFlowPane.setAlignment(Pos.TOP_CENTER);
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
                VBox card = createLinkCard(title, link, url);
                linksFlowPane.getChildren().add(card);
                adjustCardWidth(linkScroll, linksFlowPane, card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createLinkCard(String title, Hyperlink link, String url) {
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
        box.setPrefWidth(300);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setEllipsisString("...");
        titleLabel.setWrapText(false);
        Button deleteBtn = new Button("❌");
        deleteBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: red;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-font-family: 'Arial';" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: red;" +
            "-fx-border-radius: 15px;" +
            "-fx-cursor: hand;"
        );
        deleteBtn.setMinSize(30, 30);
        deleteBtn.setMaxSize(30, 30);
        deleteBtn.setOnAction(e -> {
            deleteLinkFromDatabase(course_id, title, url);
            linksFlowPane.getChildren().remove(box);
        });
        HBox header = new HBox(60, titleLabel, deleteBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        link.setMaxWidth(Double.MAX_VALUE);
        link.setWrapText(false);
        link.setEllipsisString("...");
        box.getChildren().addAll(header, link);
        return box;
    }

    private void deleteLinkFromDatabase(int courseId, String title, String url) {
        String sql = "DELETE FROM public.link WHERE course_id = ? AND link_title = ? AND link_url = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, url);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddLinkButtonAction() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Link Title");
        titleDialog.setHeaderText("Enter the title for the link:");
        titleDialog.setContentText("Title:");
        Optional<String> titleResult = titleDialog.showAndWait();
        if (titleResult.isPresent() && !titleResult.get().trim().isEmpty()) {
            String title = titleResult.get().trim();
            TextInputDialog urlDialog = new TextInputDialog("https://");
            urlDialog.setTitle("Link URL");
            urlDialog.setHeaderText("Enter the URL:");
            urlDialog.setContentText("URL:");
            Optional<String> urlResult = urlDialog.showAndWait();
            if (urlResult.isPresent() && !urlResult.get().trim().isEmpty()) {
                String url = urlResult.get().trim();
                if (linkExists(course_id, title, url)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Duplicate Link");
                    alert.setHeaderText(null);
                    alert.setContentText("This link already exists.");
                    alert.showAndWait();
                } else {
                    saveLinkToDatabase(course_id, title, url);
                    loadLinks(course_id);
                }
            }
        }
    }

    private void adjustCardWidth(ScrollPane scrollPane, FlowPane flowPane, VBox card) {
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue() - 80;
            double cardWidth = (totalWidth - flowPane.getHgap()) / 2;
            card.setPrefWidth(cardWidth);
        });
        setCardWidthNow(scrollPane, flowPane, card);
    }

    private void setCardWidthNow(ScrollPane scrollPane, FlowPane flowPane, VBox card) {
        double totalWidth = scrollPane.getWidth() - 80;
        if (totalWidth > 0) {
            double cardWidth = (totalWidth - flowPane.getHgap()) / 2;
            card.setPrefWidth(cardWidth);
        }
    }

    private void saveLinkToDatabase(int courseId, String title, String url) {
        String sql = "INSERT INTO public.link (course_id, link_title, link_url) VALUES (?, ?, ?)";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, url);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean linkExists(int courseId, String title, String url) {
        String sql = "SELECT 1 FROM public.link WHERE course_id = ? AND link_title = ? AND link_url = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, url);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    @FXML
    public void handleSave() {
        String sql = "Update public.course set notic=? where course_id=?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text.getText());
            pstmt.setInt(2, course_id);
            pstmt.executeUpdate();
            setNotic();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLectures() {
        ObservableList<LectureRow> data = FXCollections.observableArrayList();
        String q = "SELECT lecture_date FROM public.lecture WHERE course_id=? ORDER BY lecture_date";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, course_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String d = rs.getDate("lecture_date").toString();
                String s = isLectureEntered(d) ? "Entered" : "Not entered";
                data.add(new LectureRow(d, s));
            }
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isLectureEntered(String lectureDate) {
        int total = getStudentCount();
        int att = getAttendanceCount(lectureDate);
        return total > 0 && att >= total;
    }

    private int getStudentCount() {
        String q = "SELECT COUNT(*) FROM public.study WHERE course_id=?";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, course_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    private int getAttendanceCount(String lectureDate) {
        String q = "SELECT COUNT(*) FROM public.attendance WHERE course_id=? AND lecture_date=?";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, course_id);
            ps.setDate(2, Date.valueOf(lectureDate));
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    private void openAttendanceWindow(String lectureDate, Button btn, LectureRow row) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(12));
        List<CheckBox> boxes = new ArrayList<>();
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT u.firstname, u.lastname, u.email FROM public.study s JOIN public.\"User\" u ON s.student_email=u.email WHERE s.course_id=? ORDER BY u.firstname,u.lastname")) {
            ps.setInt(1, course_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                String email = rs.getString("email");
                CheckBox cb = new CheckBox(name);
                cb.setUserData(email);
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "SELECT attendance_status FROM public.attendance WHERE student_email=? AND course_id=? AND lecture_date=?")) {
                    ps2.setString(1, email);
                    ps2.setInt(2, course_id);
                    ps2.setDate(3, Date.valueOf(lectureDate));
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        String st = rs2.getString("attendance_status");
                        cb.setSelected("absent".equalsIgnoreCase(st));
                    }
                }
                boxes.add(cb);
                vbox.getChildren().add(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HBox buttons = new HBox(10);
        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        buttons.getChildren().addAll(save, cancel);
        vbox.getChildren().add(buttons);
        save.setOnAction(ev -> {
            try (Connection conn = connectDB()) {
                for (CheckBox cb : boxes) {
                    String email = (String) cb.getUserData();
                    String status = cb.isSelected() ? "absent" : "present";
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO public.attendance (student_email, course_id, lecture_date, attendance_status) VALUES (?,?,?,?) " +
                                    "ON CONFLICT (student_email, course_id, lecture_date) DO UPDATE SET attendance_status=EXCLUDED.attendance_status")) {
                        ps.setString(1, email);
                        ps.setInt(2, course_id);
                        ps.setDate(3, Date.valueOf(lectureDate));
                        ps.setString(4, status);
                        ps.executeUpdate();
                    }
                }
                boolean entered = isLectureEntered(lectureDate);
                if (entered) {
                    btn.setText("Entered");
                    btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                    row.setStatus("Entered");
                } else {
                    btn.setText("Not entered");
                    btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                    row.setStatus("Not entered");
                }
                stage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        cancel.setOnAction(ev -> stage.close());
        Scene scene = new Scene(new ScrollPane(vbox), 420, 520);
        stage.setScene(scene);
        stage.setTitle("Attendance");
        stage.showAndWait();
    }

    @FXML
    private void handleAddLecture(ActionEvent event) {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Add Lecture");
        dialog.setHeaderText("Select lecture date");
        DatePicker datePicker = new DatePicker();
        dialog.getDialogPane().setContent(datePicker);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> btn == addButtonType ? datePicker.getValue() : null);
        Optional<LocalDate> res = dialog.showAndWait();
        if (res.isPresent() && res.get() != null) {
            LocalDate ld = res.get();
            try (Connection conn = connectDB()) {
                try (PreparedStatement chk = conn.prepareStatement("SELECT 1 FROM public.lecture WHERE course_id=? AND lecture_date=?")) {
                    chk.setInt(1, course_id);
                    chk.setDate(2, Date.valueOf(ld));
                    ResultSet rs = chk.executeQuery();
                    if (rs.next()) {
                        Alert a = new Alert(Alert.AlertType.WARNING, "Lecture already exists!", ButtonType.OK);
                        a.showAndWait();
                        return;
                    }
                }
                try (PreparedStatement ins = conn.prepareStatement("INSERT INTO public.lecture (course_id, lecture_date) VALUES (?,?)")) {
                    ins.setInt(1, course_id);
                    ins.setDate(2, Date.valueOf(ld));
                    ins.executeUpdate();
                }
                loadLectures();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        public void setStatus(String s) { this.status.set(s); }
    }


    private void addDeleteColumn() {
        TableColumn<LectureRow, Void> deleteCol = new TableColumn<>("");
        deleteCol.setPrefWidth(35);
        deleteCol.setMaxWidth(35);   
        deleteCol.setMinWidth(35);    
        deleteCol.setStyle("-fx-alignment: CENTER;");

        deleteCol.setCellFactory(col -> new TableCell<LectureRow, Void>() {
            private final Button delBtn = new Button("❌");
            {
                delBtn.setStyle("-fx-background-color: white;-fx-font-size: 10px; -fx-text-fill: red; -fx-font-weight: bold; -fx-border-color: red; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                delBtn.setOnAction(e -> {
                    LectureRow row = getTableView().getItems().get(getIndex());
                    if (row == null) return;

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this lecture and its attendance?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> r = confirm.showAndWait();
                    if (!r.isPresent() || r.get() != ButtonType.YES) return;

                    if (deleteLecture(row.getDate())) {
                        getTableView().getItems().remove(row);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete lecture.", ButtonType.OK).showAndWait();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : delBtn);
            }
        });

        table.getColumns().add(deleteCol);
    }

    private boolean deleteLecture(String lectureDate) {
        String sql = "DELETE FROM public.lecture WHERE course_id=? AND lecture_date=?";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, course_id);
            ps.setDate(2, Date.valueOf(lectureDate));
            int affected = ps.executeUpdate();
            return affected > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
