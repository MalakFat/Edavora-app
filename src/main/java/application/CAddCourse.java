package application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CAddCourse implements Initializable{

    @FXML
    private Button addButton;
    
    @FXML
    private Spinner<Integer> maxSpinner;
    @FXML private ComboBox<String> FH, FM, TH, TM,teacher;
    @FXML TextField name;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 20);
        valueFactory.setValue(40);
        maxSpinner.setValueFactory(valueFactory);
        ObservableList<String> hours = FXCollections.observableArrayList();
        ObservableList<String> minutes = FXCollections.observableArrayList();

        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i += 5) {
            minutes.add(String.format("%02d", i));
        }

        FH.setItems(hours);
        FM.setItems(minutes);
        TH.setItems(hours);
        TM.setItems(minutes);
    
        FH.setVisibleRowCount(5);
        FM.setVisibleRowCount(5);
        TH.setVisibleRowCount(5);
        TM.setVisibleRowCount(5);

    }

    @FXML
    void handleAddButton(ActionEvent event) {
        addButton.setPrefWidth(300);   
        addButton.setPrefHeight(189);
        addButton.setMinSize(300, 189);
        addButton.setMaxSize(300, 189);
       
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());

            ImageView imageView = new ImageView(image);
            
            imageView.setFitWidth(addButton.getPrefWidth());
            imageView.setFitHeight(addButton.getPrefHeight());
            imageView.setPreserveRatio(false);  
            Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
            clip.setArcWidth(50);  
            clip.setArcHeight(50);  
            imageView.setClip(clip);
            
            addButton.setGraphic(imageView);
            addButton.setText(""); 
        }
    }
    @FXML
    public void addL() {
        try {
            if (FH.getValue() == null || FM.getValue() == null || TH.getValue() == null || TM.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields.").showAndWait();
                return;
            }
          

            int from = Integer.parseInt(FH.getValue()) * 60 + Integer.parseInt(FM.getValue());
            int to = Integer.parseInt(TH.getValue()) * 60 + Integer.parseInt(TM.getValue());

            if (to <= from) {
                new Alert(Alert.AlertType.ERROR, "End time must be after start time.").showAndWait();
                return;
            }

          
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input.").showAndWait();
        }
    }

    }
