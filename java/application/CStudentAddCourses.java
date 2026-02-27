package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ResourceBundle;

public class CStudentAddCourses implements Initializable {

    @FXML
    private FlowPane flow;

    private Connection conn;
    private String studentEmail;
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
        homeBtn.setOnAction(e -> switchScene("UStudentMain.fxml", homeBtn));
        addCoursesBtn.setOnAction(e -> switchScene("UStudentAddCourses.fxml", addCoursesBtn));
        profileBtn.setOnAction(e -> switchScene("UStudentProfile.fxml", profileBtn));
        messagesBtn.setOnAction(e -> switchScene("UStudentMessage.fxml", messagesBtn));
        logoutBtn.setOnAction(e -> {
            UserSession.clearSession();
            switchScene("ULogin.fxml", logoutBtn);
        });
        studentEmail = UserSession.getInstance().getEmail();
        connectToDB();
        loadCourses();
    }

    private void connectToDB() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "malak",
                    "123456"
            );
        } catch (SQLException e) {
            showError("Database Connection Failed: " + e.getMessage());
        }
    }

    private void loadCourses() {
        String sql = """
                SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.photo,
                       (u.firstname || ' ' || u.lastname) AS teacher_name
                FROM public.course c
                LEFT JOIN public.teacher t ON c.email_teacher = t.email
                LEFT JOIN public."User" u ON t.email = u.email
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String name = rs.getString("name_course");
                Time startTime = rs.getTime("start_time");
                Time endTime = rs.getTime("end_time");
                byte[] photoBytes = rs.getBytes("photo");
                
                String teacherName = rs.getString("teacher_name");
                boolean isRegistered = isStudentRegistered(courseId);
                boolean isBanned = isStudentBanned(courseId);
                Pane card = loadCardFXML(courseId, name, teacherName, startTime, endTime, photoBytes, isRegistered, isBanned);
                flow.getChildren().add(card);
            }
        } catch (SQLException e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }

    private Pane loadCardFXML(int courseId, String name, String teacher, Time start, Time end,
                              byte[] photoBytes, boolean isRegistered, boolean isBanned) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UCard.fxml"));
            Pane card = loader.load();
            CCard cardController = loader.getController();
            Image img = null;
            if (photoBytes != null) {
                img = new Image(new ByteArrayInputStream(photoBytes));
            }
            else {
          		 byte[] defaultPhotoBytes = Files.readAllBytes(Paths.get("C:/Users/Hp/OneDrive/Desktop/default.jpg"));
          		 img = new Image(new ByteArrayInputStream(defaultPhotoBytes));
          	 }
            String timeStr = start.toLocalTime().toString().substring(0, 5) + " - " + end.toLocalTime().toString().substring(0, 5);
            Image finalImg = img;
            cardController.setData(
                    name,
                    teacher != null ? teacher : "Unknown",
                    timeStr,
                    finalImg,
                    isRegistered,
                    isBanned,
                    () -> {
                        if (isBanned) {
                            showError("You are banned from registering for this course.");
                            return;
                        }
                        if (!isRegistered) {
                            if (hasTimeConflict(courseId)) {
                                showError("Time conflict with another registered course.");
                                return;
                            }
                            if (isCourseFull(courseId)) {
                                showError("This course has reached its maximum capacity.");
                                return;
                            }
                            if (addStudentToCourse(courseId)) {
                                cardController.setData(name,teacher != null ? teacher : "Unknown", timeStr,finalImg,true,isBanned, () -> {} );
                            }
                        }
                    }
            );
            return card;
        } catch (IOException e) {
            showError("Error loading card FXML: " + e.getMessage());
        }
        return null;
    }

    private boolean isStudentRegistered(int courseId) {
        String sql = "SELECT 1 FROM public.study WHERE course_id = ? AND student_email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, studentEmail);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showError("Error checking registration: " + e.getMessage());
        }
        return false;
    }

    private boolean isStudentBanned(int courseId) {
        String sql = "SELECT 1 FROM public.banned WHERE course_id = ? AND student_email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, studentEmail);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showError("Error checking ban list: " + e.getMessage());
        }
        return false;
    }

    private boolean hasTimeConflict(int newCourseId) {
        String sql = """
                SELECT 1 FROM public.study s
                JOIN public.course c1 ON s.course_id = c1.course_id
                JOIN public.course c2 ON c2.course_id = ?
                WHERE s.student_email = ?
                AND ((c2.start_time < c1.end_time) AND (c2.end_time > c1.start_time))
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newCourseId);
            ps.setString(2, studentEmail);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showError("Error checking time conflict: " + e.getMessage());
        }
        return false;
    }

    private boolean isCourseFull(int courseId) {
        String sql = """
                SELECT COUNT(*) AS enrolled, c.max_capacity
                FROM public.course c
                LEFT JOIN public.study s ON c.course_id = s.course_id
                WHERE c.course_id = ?
                GROUP BY c.max_capacity
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int enrolled = rs.getInt("enrolled");
                int maxCapacity = rs.getInt("max_capacity");
                return enrolled >= maxCapacity;
            }
        } catch (SQLException e) {
            showError("Error checking course capacity: " + e.getMessage());
        }
        return false;
    }

    private boolean addStudentToCourse(int courseId) {
        String sql = "INSERT INTO public.study (course_id, student_email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, studentEmail);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            showError("Error adding to course: " + e.getMessage());
        }
        return false;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
    @FXML
    private TextField search1;
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
