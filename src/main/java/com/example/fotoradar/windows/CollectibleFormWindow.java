package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;

public class CollectibleFormWindow implements Window {
    @FXML
    private CollectibleFormComponent collectibleForm;
    @FXML
    private Label windowLabel;

    @Setter
    private Stage dialogStage;
    @Setter
    private Collection parentCollection;

    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    public void initialize() {
        setWindowLabel(parentCollection.getTitle());
    }

    private void setWindowLabel(String collectionName) {
        windowLabel.setText(String.format("Dodaj obiekt do kolekcji %s", collectionName));
    }

    @FXML
    public void saveCollectible(ActionEvent event) throws SQLException {
        System.out.println("zapisz obiekt");

        String title = collectibleForm.titleTextField.getText();

        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (title.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = collectibleForm.titleTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole nie może być puste!");
            return;
        }

        String startDate = collectibleForm.startDatePicker.getValue() != null ?
                collectibleForm.startDatePicker.getValue().toString() : null;

        String finishDate = collectibleForm.finishDatePicker.getValue() != null ?
                collectibleForm.finishDatePicker.getValue().toString() : null;

        String description = collectibleForm.descriptionTextArea.getText() ;

        Collectible collectibleToAdd = new Collectible(
                title,
                startDate,
                finishDate,
                description != null && !description.isEmpty() ? description : null,
                collectibleForm.statusComboBox.getValue(),
                parentCollection.getId()
        );

        System.out.println("dane z formularza: " + collectibleToAdd);

        // dodanie obiektu do bazy
        CollectibleOperations collectibleOperations = new CollectibleOperations();
        collectibleOperations.addCollectible(collectibleToAdd);

        // utworzenie katalogu obiektu i podkatalogów MINIATURY i SEGMENTY
        DirectoryOperator.getInstance().createStructure(collectibleToAdd, parentCollection.getTitle());

        // po wykonanej operacji zamknij okienko
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }


    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }
}
