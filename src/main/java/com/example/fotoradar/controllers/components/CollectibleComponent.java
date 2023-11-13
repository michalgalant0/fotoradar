package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.controllers.views.View;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.controllers.views.CollectibleView;
import com.example.fotoradar.models.Thumbnail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public class CollectibleComponent extends AnchorPane implements View {

    @FXML
    public Label headerLabel;
    @FXML
    private ImageView imageView;
    @FXML
    public TextField startDateTextField;
    @FXML
    public TextField finishDateTextField;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public TextField statusTextField;

    @Setter
    private Collectible collectible;
    @Setter
    private Thumbnail mainThumbnail;

    public CollectibleComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectibleComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    private void setHeaderLabel(String name) {
        headerLabel.setText(name);
    }

    public void fillComponent() {
        System.out.println("CollectibleComponent.fillComponent: "+collectible);
        setHeaderLabel(collectible.getTitle());
        startDateTextField.setText(collectible.getStartDate());
        finishDateTextField.setText(collectible.getFinishDate());
        descriptionTextArea.setText(collectible.getDescription());
        // todo pobranie statusu z bazy
        statusTextField.setText("do pobrania");
    }

    public void setThumbnailPath(String collectionName) throws SQLException {
        if (mainThumbnail == null)
            return;
        String thumbnailPath = String.format(context.getThumbnailPath(),
                context.getRootDirectory(), collectionName, collectible.getTitle(),
                mainThumbnail.getFileName());
        Image image = new Image("file://"+thumbnailPath);
        imageView.setImage(image);
    }

    @FXML
    public void goToCollectible(ActionEvent event) throws IOException, SQLException {
        System.out.println("przejscie do obiektu");
        System.out.println("CollectibleComponent.goToCollectible: "+collectible);
        context.setCurrentCollectible(collectible);
        new SwitchScene().switchScene(event, "collectibleView");
    }
}
