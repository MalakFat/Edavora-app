package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	

	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("ULogin.fxml"));
	        primaryStage.setTitle("Edvora");
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
	    }
	
	public static void main(String[] args) {
		launch(args);
	}

}

 class UserSession {
    private static UserSession instance;
    private String email;
    private String fullName;

    private UserSession(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public static void createSession(String email, String fullName) {
        instance = new UserSession(email, fullName);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public static void clearSession() {
        instance = null;
    }
}
 