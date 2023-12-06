package com.example.fotoradar.painter;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.windows.SaveSketchWindow;
import com.example.fotoradar.windows.Window;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class Painter extends Application {

    private GraphicsContext gcB, gcF, gcI;
    private Stage primaryStage;

    @Setter
    private Stage dialogStage;
    private boolean drawline = false, drawoval = false, drawrectangle = false, erase = false, freedesign = true;

    double startX, startY, lastX, lastY, oldX, oldY;

    double hg;

    //>>>>>>>>>>>>>>>>>>>>>>>FXML Variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML
    private RadioButton strokeRB, fillRB;
    @FXML
    private ColorPicker colorPick;
    @FXML
    private Canvas TheCanvas, canvasGo, imgBack;
    @FXML
    private Button rectButton;
    @FXML
    private Button lineButton;
    @FXML
    private Button ovlButton;
    @FXML
    private Button pencButton;
    @FXML
    private Button eraser;
    @FXML
    private Button open;
    @FXML
    private Button save;
    @FXML
    private Label windowTitle;
    @FXML
    private TextField fileName;
    @FXML
    private Slider sizeSlider;
    @FXML
    private Button saveSketch;
    @FXML
    private Button cancelSave;
    @Setter
    private Collectible collectible = new Collectible();
    @Setter
    private String parentCollectionName;

    private final ThumbnailOperations thumbnailOperations= new ThumbnailOperations();

    private String filePathString;


    public Painter() throws SQLException {
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/painterView.fxml"));
        Painter painterView = new Painter();
        painterView.setParentCollectionName(parentCollectionName);
        painterView.setCollectible(collectible);
        fxmlLoader.setController(painterView);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        painterView.initialize();
    }

    private void initialize() {
        colorPick.setValue(Color.valueOf("black"));
        gcB = canvasGo.getGraphicsContext2D();
        gcF = TheCanvas.getGraphicsContext2D();
        gcI = imgBack.getGraphicsContext2D();

        sizeSlider.setMin(1);
        sizeSlider.setMax(50);

        //////////////////////////////////
        Image imageRect = new Image(Main.class.getResource("icons/rectangle.png").toExternalForm());
        ImageView icR = new ImageView(imageRect);
        icR.setFitWidth(32);
        icR.setFitHeight(32);
        rectButton.setGraphic(icR);

        Image imageLine = new Image(Main.class.getResource("icons/line.png").toExternalForm());
        ImageView icLin = new ImageView(imageLine);
        icLin.setFitWidth(32);
        icLin.setFitHeight(32);
        lineButton.setGraphic(icLin);

        Image imageOval = new Image(Main.class.getResource("icons/oval.png").toExternalForm());
        ImageView icOval = new ImageView(imageOval);
        icOval.setFitWidth(32);
        icOval.setFitHeight(32);
        ovlButton.setGraphic(icOval);

        Image imagePenc = new Image(Main.class.getResource("icons/pencil.png").toExternalForm());
        ImageView icLapiz = new ImageView(imagePenc);
        icLapiz.setFitWidth(32);
        icLapiz.setFitHeight(32);
        pencButton.setGraphic(icLapiz);

        Image imageEraser = new Image(Main.class.getResource("icons/eraser.png").toExternalForm());
        ImageView icEraser = new ImageView(imageEraser);
        icLapiz.setFitWidth(32);
        icLapiz.setFitHeight(32);
        eraser.setGraphic(icEraser);
    }

    public static void main(String[] args) {
        launch(args);
    }


    //////////////////////////PAINTER_FUNCTIONS///////////////////////////////////
    @FXML
    private void onMousePressedListener(MouseEvent e) {
        if (erase) {
            erase(e.getX(), e.getY());
        } else{
            this.startX = e.getX();
            this.startY = e.getY();
            this.oldX = e.getX();
            this.oldY = e.getY();
        }}

    @FXML
    private void onMouseDraggedListener(MouseEvent e) {
        if (erase) {
            erase(e.getX(), e.getY());
        } else{
            this.lastX = e.getX();
            this.lastY = e.getY();

            if (drawrectangle)
                drawRectEffect();
            if (drawoval)
                drawOvalEffect();
            if (drawline)
                drawLineEffect();
            if (freedesign)
                freeDrawing();
        }}

    @FXML
    private void onMouseReleaseListener(MouseEvent e) {
        if (drawrectangle)
            drawRect();
        if (drawoval)
            drawOval();
        if (drawline)
            drawLine();
    }

    //Gumka
    private void erase(double x, double y) {
        gcB.clearRect(x - sizeSlider.getValue() / 2, y - sizeSlider.getValue() / 2, sizeSlider.getValue(), sizeSlider.getValue());
        gcF.clearRect(x - sizeSlider.getValue() / 2, y - sizeSlider.getValue() / 2, sizeSlider.getValue(), sizeSlider.getValue());
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>Draw methods<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private void drawOval() {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());

        if (fillRB.isSelected()) {
            gcF.setFill(colorPick.getValue());
            gcF.fillOval(startX, startY, wh, hg);
        } else {
            gcF.setStroke(colorPick.getValue());
            gcF.strokeOval(startX, startY, wh, hg);
        }
    }

    private void drawRect() {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());

        if (fillRB.isSelected()) {
            gcF.setFill(colorPick.getValue());
            gcF.fillRect(startX, startY, wh, hg);
        } else {
            gcF.setStroke(colorPick.getValue());
            gcF.strokeRect(startX, startY, wh, hg);
        }
    }

    private void drawLine() {
        gcF.setLineWidth(sizeSlider.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.strokeLine(startX, startY, lastX, lastY);
    }

    private void freeDrawing() {
        gcF.setLineWidth(sizeSlider.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.strokeLine(oldX, oldY, lastX, lastY);
        oldX = lastX;
        oldY = lastY;
    }

    //////////////////////////////////////////////////////////////////////
    //>>>>>>>>>>>>>>>>>>>>>>>>>>Draw effects methods<<<<<<<<<<<<<<<<<<<<<<<

    private void drawOvalEffect() {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());

        if (fillRB.isSelected()) {
            gcB.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcB.setFill(colorPick.getValue());
            gcB.fillOval(startX, startY, wh, hg);
        } else {
            gcB.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcB.setStroke(colorPick.getValue());
            gcB.strokeOval(startX, startY, wh, hg);
        }
    }

    private void drawRectEffect() {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());

        if (fillRB.isSelected()) {
            gcB.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcB.setFill(colorPick.getValue());
            gcB.fillRect(startX, startY, wh, hg);
        } else {
            gcB.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcB.setStroke(colorPick.getValue());
            gcB.strokeRect(startX, startY, wh, hg);
        }
    }

    private void drawLineEffect() {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
        gcB.strokeLine(startX, startY, lastX, lastY);
    }


    @FXML
    private void clearCanvas(ActionEvent e) {
        gcB.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
    }

    @FXML
    private void clearImg(ActionEvent e) {
        gcI.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
    }

    /*------- Save & Open ------*/
    // Open
    private void open() {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open File");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        openFile.getExtensionFilters().add(imageFilter);
        File file = openFile.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                InputStream io = new FileInputStream(file);
                Image img = new Image(io);

                double aspectRatio = img.getWidth() / img.getHeight();
                double newWidth, newHeight;

                if (aspectRatio >= 1) {
                    newWidth = 800;
                    newHeight = 800 / aspectRatio;
                } else {
                    newHeight = 800;
                    newWidth = 800 * aspectRatio;
                }

                ImageView imageView = new ImageView(img);
                imageView.setFitWidth(newWidth);
                imageView.setFitHeight(newHeight);

                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.TRANSPARENT);

                WritableImage scaledImage = imageView.snapshot(params, null);

                gcI.drawImage(scaledImage, 0, 0);
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }


    // Save --> przejście do okienka od zapisu
    private void save() throws IOException, SQLException {
        SaveSketchWindow saveSketchWindow = new SaveSketchWindow(TheCanvas);
        saveSketchWindow.setCollectible(collectible);
        saveSketchWindow.setParentCollectionName(parentCollectionName);
        new SwitchScene().displayWindow("SaveSketchWindow", "Zapisz szkic", saveSketchWindow);
    }
    ///////////////////////////////////////////////////////////////////////


    //>>>>>>>>>>>>>>>>>>>>>Buttons control<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML
    private void setOvalAsCurrentShape(ActionEvent e) {
        drawline = false;
        drawoval = true;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }

    @FXML
    private void setLineAsCurrentShape(ActionEvent e) {
        drawline = true;
        drawoval = false;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }

    @FXML
    private void setRectangleAsCurrentShape(ActionEvent e) {
        drawline = false;
        drawoval = false;
        freedesign = false;
        erase = false;
        drawrectangle = true;
    }

    @FXML
    private void setErase(ActionEvent e) {
        drawline = false;
        drawoval = false;
        drawrectangle = false;
        erase = true;
        freedesign = false;
    }

    @FXML
    private void setFreeDesign(ActionEvent e) {
        drawline = false;
        drawoval = false;
        drawrectangle = false;
        erase = false;
        freedesign = true;
    }
    @FXML
    private void setSave(ActionEvent e) throws IOException, SQLException {
        save();
    }

    @FXML
    private void setOpen(ActionEvent e) {
        open();
    }
}
