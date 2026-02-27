package application;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CStudentMain implements Initializable{
	 @FXML private Label LBL;
	 @FXML private FlowPane flow;
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
	    private TextField search;
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
	     UserSession session = UserSession.getInstance();
	 	search.setDisable(false);
		// أو
		search.setEditable(true);

	     if (session != null) {
	         LBL.setText("Hello " + session.getFullName());
	     } else {
	         LBL.setText("Hello Guest");
	     }
	     loadCourses() ;

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
	 void loadCourses() {
		    String email = UserSession.getInstance().getEmail();

		    String queryStudy = "SELECT course_id FROM public.study WHERE student_email = ?";
		    String queryCourse = """
		        SELECT c.name_course, c.start_time, c.end_time, c.photo, u.firstname || ' ' || u.lastname AS teacher_name
		        FROM public.course c
		        JOIN public.teacher t ON c.email_teacher = t.email
		        JOIN public."User" u ON t.email = u.email
		        WHERE c.course_id = ?
		    """;

		    boolean hasCourses = false;

		    try (Connection conn = DriverManager.getConnection(
		            "jdbc:postgresql://localhost:5432/postgres", "malak", "123456");
		         PreparedStatement pstStudy = conn.prepareStatement(queryStudy);
		         PreparedStatement pstCourse = conn.prepareStatement(queryCourse)) {

		        pstStudy.setString(1, email);
		        ResultSet rsStudy = pstStudy.executeQuery();

		        while (rsStudy.next()) {
		            hasCourses = true; // وجدنا كورس واحد على الأقل
		            int courseId = rsStudy.getInt("course_id");

		            pstCourse.setInt(1, courseId);
		            ResultSet rsCourse = pstCourse.executeQuery();

		            if (rsCourse.next()) {
		                String name = rsCourse.getString("name_course");
		                Time start = rsCourse.getTime("start_time");
		                Time end = rsCourse.getTime("end_time");
		                byte[] photoBytes = rsCourse.getBytes("photo");
		                String teacherName = rsCourse.getString("teacher_name");

		                addCourseCard(courseId,name, teacherName, start, end, photoBytes);
		            }
		        }

		        if (!hasCourses) {
		            Label noCoursesLabel = new Label("You are not enrolled in any courses yet");
		            noCoursesLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
		            flow.getChildren().add(noCoursesLabel);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		private void addCourseCard(int id,String name, String Tname, Time start, Time end, byte[] photoBytes) {
		    try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("UCourseInStudent.fxml"));
		        Node card = loader.load();

		        ImageView imgView = (ImageView) card.lookup("#imgView");
		        Label nameLabel = (Label) card.lookup("#courseName");
		        Label timeLabel = (Label) card.lookup("#courseTime");
		        Label teacher = (Label) card.lookup("#TeacherName");
		        Button arrowButton = (Button) card.lookup("#arrowButton"); // اسم الـ fx:id للزر

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
		                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("UStudentCourse.fxml"));
		                Parent root = loader2.load();

		                CStudentCourse controller = loader2.getController();
		                controller.setCourseId(id);

		                Stage stage = (Stage) arrowButton.getScene().getWindow();
		                stage.setScene(new Scene(root));
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		        });


		        flow.getChildren().add(card);

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		@FXML
	    private void handleSearch(ActionEvent event) {
	        String searchText = search.getText().trim().toLowerCase();

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
