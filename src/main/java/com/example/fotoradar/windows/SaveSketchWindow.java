package com.example.fotoradar.windows;

import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;


public class SaveSketchWindow{
    @FXML
    public TextField fileName;
    @FXML
    public Button saveSketch;
    @FXML
    public Button cancelSave;
    @Setter
    private Collectible collectible;
    @Setter
    private Collection parentCollection;
    private String filePathString;
    private final Canvas theCanvas;

    private ThumbnailOperations thumbnailOperations;

    public SaveSketchWindow(Canvas theCanvas) throws SQLException {
        this.theCanvas = theCanvas;
        this.collectible = new Collectible();
        this.parentCollection = new Collection();
        this.thumbnailOperations = new ThumbnailOperations();

    }

    @FXML
    private void setSaveSketch(ActionEvent e) throws Exception {
        saveSketch();
    }

    @FXML
    private void setCancel(ActionEvent e){
        cancel();
    }

    public void saveSketch() throws Exception {

        String fileNameText = fileName.getText(); // Pobranie tekstu z pola fileName
        // Sprawdź, czy fileNameText nie jest puste
        if (fileNameText != null && !fileNameText.isEmpty()) {
            // Tworzenie ścieżki pliku
            filePathString = Paths.get(
                    System.getProperty("user.dir"), "KOLEKCJE",
                    parentCollection.getTitle(), "OBIEKTY", collectible.getTitle(), "MINIATURY", fileNameText).toString();

            String filePath = filePathString + ".png"; // Dodanie rozszerzenia .png do nazwy pliku
            System.out.printf((filePath) + "%n");
            try {
                WritableImage writableImage = new WritableImage(800, 800);
                theCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                File file = new File(filePath);
                ImageIO.write(renderedImage, "png", file);

                // dodanie szkicu do bazy miniatur
                thumbnailOperations.addThumbnail(
                        new Thumbnail(fileNameText+".png", collectible.getId())
                );

                // Zamknięcie okna SaveSketchWindow po zapisaniu pliku
                //saveSketch.getScene().getWindow().hide();
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        } else{
            System.out.println("Nazwa pliku jest pusta!");
        }
    }

    public void cancel(){
    //Zamknięcie okna SaveSketchWindow
    }



}
