package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CCard {
    @FXML private ImageView image;
    @FXML private Label courseName;
    @FXML private Label teacherName;
    @FXML private Label time;
    @FXML private Button add;

    public void setData(String name, String teacher, String timeStr, Image img, boolean isRegistered, boolean isBanned, Runnable onAdd) {
        courseName.setText(name);
        teacherName.setText(teacher);
        time.setText(timeStr);
        if (img != null) image.setImage(img);
        add.setText(isRegistered ? "Added" : "Add");
        add.setDisable(isBanned);
        add.setOnAction(e -> onAdd.run());
    }
}
