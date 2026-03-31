package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;



import javafx.scene.control.TextField; // Import TextField for link functionality
import javafx.scene.layout.Priority; // Import Priority for HBox.setHgrow
import javafx.scene.layout.VBox;

// Imports for opening links in the browser
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class CCourse {

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
    private void handleAddFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Files");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Documents", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // Get the current stage to open the file chooser dialog
        Stage stage = (Stage) addFileButton.getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                addFileToFlowPane(file);
            }
        }
    }

    private void addFileToFlowPane(File file) {
        HBox fileDisplay = new HBox();
        fileDisplay.setSpacing(10);
        fileDisplay.setPadding(new Insets(10));
        fileDisplay.setStyle("-fx-border-color: #271048; -fx-border-width: 2px; -fx-border-radius: 5px;");
        fileDisplay.setAlignment(Pos.CENTER_LEFT);

        fileDisplay.setPrefWidth(700);
        fileDisplay.setPrefHeight(70);
        FlowPane.setMargin(fileDisplay, new Insets(5, 5, 5, 5));

        fileDisplay.setOnMouseClicked(event -> {
            // Only handle download if the click is not on the delete button
            if (event.getTarget() instanceof Button && ((Button)event.getTarget()).getText().equals("Delete")) {
                // Do nothing, the delete button handles its own action
            } else {
                handleFileDownload(file);
            }
        });

        ImageView fileIcon = new ImageView();
        fileIcon.setFitWidth(48);
        fileIcon.setFitHeight(48);
        fileIcon.setPreserveRatio(true);

        String fileExtension = getFileExtension(file);
        Image iconImage = null;

        try {
            switch (fileExtension) {
                case "pdf":
                    iconImage = new Image(getClass().getResourceAsStream("/application/pdf_icon.png"));
                    break;
                case "png":
                case "jpg":
                case "jpeg":
                case "gif":
                    iconImage = new Image(file.toURI().toString(), 48, 48, true, true);

                    if (iconImage != null) {
                        Rectangle clip = new Rectangle(fileIcon.getFitWidth(), fileIcon.getFitHeight());
                        clip.setArcWidth(10);
                        clip.setArcHeight(10);
                        fileIcon.setClip(clip);
                    }
                    break;
                case "txt":
                    iconImage = new Image(getClass().getResourceAsStream("/application/txt_icon.png"));
                    break;
                default:
                    iconImage = new Image(getClass().getResourceAsStream("/application/default_file_icon.png"));
                    break;
            }
            if (iconImage != null) {
                fileIcon.setImage(iconImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon/thumbnail for file type " + fileExtension + ": " + e.getMessage());
            try {
                iconImage = new Image(getClass().getResourceAsStream("/application/default_file_icon.png"));
                if (iconImage != null) {
                    fileIcon.setImage(iconImage);
                }
            } catch (Exception ex) {
                System.err.println("Error loading default file icon: " + ex.getMessage());
            }
        }

        Label fileNameLabel = new Label(file.getName());
        fileNameLabel.setFont(new Font("System", 12));
        fileNameLabel.setWrapText(true);
        // Set max width for label to prevent it from pushing the HBox beyond its prefWidth
        // Adjusted max width to accommodate the delete button
        fileNameLabel.setMaxWidth(fileDisplay.getPrefWidth() - fileIcon.getFitWidth() - 100 - fileDisplay.getSpacing() - fileDisplay.getPadding().getLeft() - fileDisplay.getPadding().getRight());


        Button deleteFileButton = new Button("Delete");
        deleteFileButton.setFont(new Font("System", 12));
        deleteFileButton.setStyle("-fx-background-color: #e04040; -fx-text-fill: white; -fx-background-radius: 5;");
        deleteFileButton.setOnAction(e -> {
            slidesFlowPane.getChildren().remove(fileDisplay); // Remove the container from FlowPane
        });

        // Add a spacer to push the delete button to the right
        HBox.setHgrow(fileNameLabel, Priority.ALWAYS);


        fileDisplay.getChildren().addAll(fileIcon, fileNameLabel, deleteFileButton); // Add the delete button
        slidesFlowPane.getChildren().add(fileDisplay);
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    private void handleFileDownload(File originalFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        fileChooser.setInitialFileName(originalFile.getName());

        String extension = getFileExtension(originalFile);
        if (!extension.isEmpty()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.toUpperCase() + " Files", "*." + extension));
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        Stage stage = (Stage) slidesFlowPane.getScene().getWindow();
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile != null) {
            try {
                Files.copy(originalFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved successfully to: " + saveFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleAddLinkButtonAction() {
        HBox linkInputContainer = new HBox(10); 
        linkInputContainer.setPadding(new Insets(10));
        linkInputContainer.setStyle("-fx-border-color: #271048; -fx-border-width: 2px; -fx-border-radius: 5px;");
        linkInputContainer.setAlignment(Pos.CENTER_LEFT);
        linkInputContainer.setPrefWidth(700);
        linkInputContainer.setPrefHeight(70);
        FlowPane.setMargin(linkInputContainer, new Insets(5, 5, 5, 5));

        TextField linkTextField = new TextField();
        linkTextField.setPromptText("put the link");
        linkTextField.setPrefWidth(450); 
        linkTextField.setFont(new Font("System", 14));

        Button openLinkButton = new Button("Open"); 
        openLinkButton.setFont(new Font("System", 12));
        openLinkButton.setStyle("-fx-background-color: #c3aad1; -fx-text-fill: white; -fx-background-radius: 5;"); 
        openLinkButton.setOnAction(e -> {
            String url = linkTextField.getText();
            if (url != null && !url.trim().isEmpty()) {
                openWebpage(url.trim());
            } else {
                System.out.println("No link provided to open.");
            }
        });

        Button deleteLinkButton = new Button("Delete");
        deleteLinkButton.setFont(new Font("System", 12));
        deleteLinkButton.setStyle("-fx-background-color: #e04040; -fx-text-fill: white; -fx-background-radius: 5;");
        deleteLinkButton.setOnAction(e -> {
            linksFlowPane.getChildren().remove(linkInputContainer); // Remove the container from FlowPane
        });
        
        HBox.setHgrow(linkTextField, Priority.ALWAYS); // Make the text field grow to push buttons

        linkInputContainer.getChildren().addAll(linkTextField, openLinkButton, deleteLinkButton); // Added Open button
        linksFlowPane.getChildren().add(linkInputContainer);
    }

    private void openWebpage(String url) {
        // Add "http://" if protocol is missing
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            } else {
                System.err.println("Desktop or BROWSE action not supported on this platform.");
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error opening link: " + e.getMessage());
        }
    }

    @FXML
    public void addpane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UAddLecture.fxml"));
            Parent root = loader.load();

            CAddLecture controller = loader.getController();
            controller.setMainController(this);

            Stage popupStage = new Stage();
            controller.setDialogStage(popupStage);
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLectureBox(Node node) {
        flow.getChildren().add(node);
    }

}