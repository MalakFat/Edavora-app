package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CStudentMessage implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    private FlowPane flowPane;

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "malak";
    private final String PASSWORD = "123456";
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
        flowPane = new FlowPane();
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(20, 20, 0, 20));
        flowPane.setPrefWidth(scrollPane.getWidth());
        scrollPane.setContent(flowPane);

        // Home screen
        homeBtn.setOnAction(e -> switchScene("UStudentMain.fxml", homeBtn));
        // Add courses
        addCoursesBtn.setOnAction(e -> switchScene("UStudentAddCourses.fxml", addCoursesBtn));
        // Profile
        profileBtn.setOnAction(e -> switchScene("UStudentProfile.fxml", profileBtn));
        // Messages
        messagesBtn.setOnAction(e -> switchScene("UStudentMessage.fxml", messagesBtn));
        // Logout
        logoutBtn.setOnAction(e -> {
            UserSession.clearSession();
            switchScene("ULogin.fxml", logoutBtn);
        });

        loadMessages();
    }

    private void loadMessages() {
        flowPane.getChildren().clear();

        String currentEmail = UserSession.getInstance().getEmail();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = """
                SELECT m.message_id, m.message_content, m.date
                FROM public.message m
                JOIN public.allmessages am ON m.message_id = am.message_id
                WHERE am.email = ?
                ORDER BY m.date DESC
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, currentEmail);
                try (ResultSet rs = ps.executeQuery()) {

                    Label lb = new Label("Messages from manager");
                    lb.setStyle("-fx-text-fill: #271048; -fx-font-size: 42px; -fx-font-family: 'Monotype Corsiva';");
                    flowPane.getChildren().add(lb);

                    while (rs.next()) {
                        int id = rs.getInt("message_id");
                        String content = rs.getString("message_content");
                        Date date = rs.getDate("date");

                        HBox box = createMessageBox(id, content, date);
                        flowPane.getChildren().add(box);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createMessageBox(int id, String content, Date date) {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-border-color: #271048; -fx-border-width: 2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 15; -fx-background-color: white;");
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setSpacing(20);
        hbox.setPrefWidth(700);

        VBox vbox = new VBox();
        vbox.setSpacing(5);

        Label lblContent = new Label(content);
        lblContent.setWrapText(true);
        lblContent.setPrefHeight(50);
        lblContent.setPrefWidth(700);
        lblContent.setStyle("-fx-font-size: 20px; -fx-font-family: 'System';");

        Label lblDate = new Label("Date: " + date.toString());
        lblDate.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        vbox.getChildren().addAll(lblContent, lblDate);
        hbox.getChildren().add(vbox);

        return hbox;
    }
}
