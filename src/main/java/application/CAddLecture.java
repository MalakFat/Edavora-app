package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CAddLecture {

    @FXML private DatePicker datePicker;
    @FXML private Button Add;

    private CCourse mainController;
    private Stage dialogStage;
    private boolean saved = false;

    public void setMainController(CCourse controller) {
        this.mainController = controller;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public boolean isSaved() {
        return saved;
    }

 

    @FXML
    public void addL() {
        try {
            if (datePicker.getValue() == null ) {
                new Alert(Alert.AlertType.ERROR, "Please fill Date's field.").showAndWait();
                return;
            }
          

            String dateText = "Date: " + datePicker.getValue().toString();
            String timeText = "Duration: " + "09" + ":" + "00" + " - " +"10" + ":" + "30";

            Label dateLabel = new Label(dateText);
            Label timeLabel = new Label(timeText);
            Button attendanceBtn = new Button("attendance");
            attendanceBtn.setStyle("-fx-background-color: #C1A3D3; -fx-text-fill: #271048; -fx-background-radius: 20;-fx-font-size: 16px; -fx-pref-width: 120px; -fx-pref-height: 40px;");
            HBox buttonBox = new HBox(attendanceBtn);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            VBox box = new VBox(10, dateLabel, timeLabel, buttonBox);
            box.setPrefSize(300, 150);
            box.setStyle("-fx-border-color: #271048; -fx-border-width: 2; -fx-border-radius: 20; -fx-background-radius: 20; -fx-background-color: white; -fx-padding: 10;");
          
            String fontStyle = "-fx-font-size: 16px;";

            dateLabel.setStyle(fontStyle);
            timeLabel.setStyle(fontStyle);

            mainController.addLectureBox(box);

            saved = true;
            dialogStage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input.").showAndWait();
        }
    }
    @FXML
    public void cancel() {
        saved = false;

    	 dialogStage.close();
    }
}
