package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.views.CollectibleView;
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
import java.util.ArrayList;

public class CollectibleComponent extends AnchorPane {

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

    private String thumbnailPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/%s";

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
        statusTextField.setText(collectible.getStatus().getName());
    }


    public void setThumbnailPath(String collectionName) throws SQLException {
        ArrayList<Thumbnail> allThumbnails = new ThumbnailOperations().getAllThumbnails(collectible.getId());
        if (!allThumbnails.isEmpty()) {
            thumbnailPath = String.format(thumbnailPath,
                    Main.getDefPath(), collectionName, collectible.getTitle(),
                    allThumbnails.get(0).getFileName());
            setObjectThumbnailImageView();
        }
        else
            imageView.setImage(null);
    }

    public void setObjectThumbnailImageView() {
        Image image = new Image("file://"+thumbnailPath);
        imageView.setImage(image);
    }

    @FXML
    public void goToCollectible(ActionEvent event) throws IOException, SQLException {
        System.out.println("przejscie do obiektu");
        System.out.println("CollectibleComponent.goToCollectible: "+collectible);
        // utworzenie kontrolera widoku docelowego
        CollectibleView collectibleView = new CollectibleView();
        collectibleView.setCollectible(collectible);
        // załadowanie nowej sceny z przekazanym kontrolerem
        new SwitchScene().switchScene(event, "collectibleView", collectibleView);
    }
}
