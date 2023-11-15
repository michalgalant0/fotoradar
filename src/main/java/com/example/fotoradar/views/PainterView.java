package com.example.fotoradar.views;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.fotoradar.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;


public class PainterView implements Initializable {
    //>>>>>>>>>>>>>>>>>>>>>>>Other variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private GraphicsContext gcB, gcF, gcI;
    private Stage primaryStage;
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
    private Slider sizeSlider;



    //////////////////////////////////////////////////////////////////////////////

    public PainterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public PainterView(){
        // Pusty konstruktor bezparametrowy
    }


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

    // New method for erasing
    private void erase(double x, double y) {
        gcB.clearRect(x - sizeSlider.getValue() / 2, y - sizeSlider.getValue() / 2, sizeSlider.getValue(), sizeSlider.getValue());
        gcF.clearRect(x - sizeSlider.getValue() / 2, y - sizeSlider.getValue() / 2, sizeSlider.getValue(), sizeSlider.getValue());

    }

//    @FXML
//    private void onMouseExitedListener(MouseEvent event) {
//        System.out.println("Nie możesz rysować poza płótnem");
//    }

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

    /*------- Save & Open ------*/
    // Open
    private void open() {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open File");
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


    // Save
    private void save(){
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");

        File file = savefile.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(800, 800);
                TheCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////

    @FXML
    private void clearCanvas(ActionEvent e) {
        gcB.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
    }


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
    private void setSave(ActionEvent e) {
        drawline = false;
        drawoval = false;
        drawrectangle = false;
        erase = false;
        freedesign = false;
        save();
    }

    @FXML
    private void setOpen(ActionEvent e) {
        drawline = false;
        drawoval = false;
        drawrectangle = false;
        erase = false;
        freedesign = false;
        open();
    }

    //////////////////////////////////////////////////////////////////


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        Image imageLinea = new Image(Main.class.getResource("icons/line.png").toExternalForm());
        ImageView icLin = new ImageView(imageLinea);
        icLin.setFitWidth(32);
        icLin.setFitHeight(32);
        lineButton.setGraphic(icLin);

        Image imageOvalo = new Image(Main.class.getResource("icons/oval.png").toExternalForm());
        ImageView icOval = new ImageView(imageOvalo);
        icOval.setFitWidth(32);
        icOval.setFitHeight(32);
        ovlButton.setGraphic(icOval);

        Image imageLapiz = new Image(Main.class.getResource("icons/pencil.png").toExternalForm());
        ImageView icLapiz = new ImageView(imageLapiz);
        icLapiz.setFitWidth(32);
        icLapiz.setFitHeight(32);
        pencButton.setGraphic(icLapiz);

        Image imageEraser = new Image(Main.class.getResource("icons/eraser.png").toExternalForm());
        ImageView icEraser = new ImageView(imageEraser);
        icLapiz.setFitWidth(32);
        icLapiz.setFitHeight(32);
        eraser.setGraphic(icEraser);
    }
}
