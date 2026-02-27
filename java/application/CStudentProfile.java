package application;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CStudentProfile implements Initializable {

    @FXML
    private TextField emailF, fnameF, lnameF;
    @FXML
    private PasswordField passF;
    @FXML
    private DatePicker dateF;
    @FXML
    private RadioButton maleF, femaleF;
    @FXML
    private ImageView profileImageView;

    private byte[] profileImageBytes = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
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
        fnameF.setEditable(true);
        lnameF.setEditable(true);

    }

    private Connection connectDB() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "malak",
                "123456"
        );
    }

    private void loadUserData() {
        String email = UserSession.getInstance().getEmail();

        try (Connection conn = connectDB();
             PreparedStatement stet = conn.prepareStatement(
                     "SELECT * FROM public.\"User\" WHERE email = ?")) {

            stet.setString(1, email);
            ResultSet rs = stet.executeQuery();

            if (rs.next()) {
                emailF.setText(rs.getString("email"));
                fnameF.setText(rs.getString("firstname"));
                lnameF.setText(rs.getString("lastname"));
                passF.setText(rs.getString("password"));

                Date birthdate = rs.getDate("birthdate");
                if (birthdate != null) {
                    dateF.setValue(birthdate.toLocalDate());
                }

                String gen = rs.getString("gender");
                if (gen != null) {
                    if (gen.equalsIgnoreCase("Male")) {
                        maleF.setSelected(true);
                    } else if (gen.equalsIgnoreCase("Female")) {
                        femaleF.setSelected(true);
                    }
                }

                profileImageBytes = rs.getBytes("profile_image");
                if (profileImageBytes != null && profileImageBytes.length > 0) {
                    profileImageView.setImage(new Image(new ByteArrayInputStream(profileImageBytes)));
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "User not found", ButtonType.OK).showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleChangeProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                profileImageBytes = readFileToBytes(selectedFile);
                profileImageView.setImage(new Image(new ByteArrayInputStream(profileImageBytes)));

                String email = UserSession.getInstance().getEmail();
                String sql = "UPDATE public.\"User\" SET profile_image=? WHERE email=?";

                try (Connection conn = connectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setBytes(1, profileImageBytes);
                    pstmt.setString(2, email);
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    @FXML
    private void handleSave() {
        String email = UserSession.getInstance().getEmail();

        String sql = "UPDATE public.\"User\" SET firstname=?, lastname=?, birthdate=?, gender=?, profile_image=? WHERE email=?";

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fnameF.getText());
            pstmt.setString(2, lnameF.getText());

            LocalDate date = dateF.getValue();
            if (date != null) {
                pstmt.setDate(3, Date.valueOf(date));
            } else {
                pstmt.setNull(3, Types.DATE);
            }

            String gender = maleF.isSelected() ? "Male" : "Female";
            pstmt.setString(4, gender);

            if (profileImageBytes != null) {
                pstmt.setBytes(5, profileImageBytes);
            } else {
                pstmt.setNull(5, Types.BINARY);
            }

            pstmt.setString(6, email);

            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profile Updated");
            alert.setHeaderText(null);
            alert.setContentText("Your profile information has been updated successfully.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleChangePassword() {
        try {
            Stage stage = (Stage) emailF.getScene().getWindow();
            stage.close();

            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("UChangePassword.fxml"));
            newStage.setScene(new Scene(root));
            newStage.setTitle("Change Password");
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int read;
            while ((read = fis.read(buf)) != -1) {
                bos.write(buf, 0, read);
            }
            return bos.toByteArray();
        }
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

  
}
