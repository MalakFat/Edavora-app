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

public class CCourseSearch implements Initializable{

    @FXML private ComboBox<String> FH, FM, TH, TM;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    }}

   