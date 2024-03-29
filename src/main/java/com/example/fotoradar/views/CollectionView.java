package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.RemoveStructureListener;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectiblesComponent;
import com.example.fotoradar.components.IndicatorsComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.windows.CollectibleFormWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionView implements RemoveStructureListener, OnWindowClosedListener {
    @FXML
    private Label windowLabel;
    @FXML
    private CollectiblesComponent collectiblesComponent;
    @FXML
    private IndicatorsComponent indicatorsComponent;

    @Setter
    private Collection collection;

    private CollectibleOperations collectibleOperations;
    private ArrayList<Collectible> collectibles;

    public CollectionView() {
        try {
            collectibleOperations = new CollectibleOperations();
            collection = new Collection();
            collectibles = new ArrayList<>();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException, IOException {
        System.out.println("CollectionView.initialize: "+collection);
        // ustawienie nagłówka okna
        setWindowLabel(collection.getTitle());
        // wypełnienie listy obiektów danymi pobranymi z bazy
        collectibles = collectibleOperations.getAllCollectibles(collection.getId());
        collectiblesComponent.setCollectibles(collectibles);
        collectiblesComponent.setCollectionName(collection.getTitle());
        collectiblesComponent.fillCollectiblesHBox();

        double[] indicatorData = new CollectionOperations().fetchProgressAndSizeInfo(collection.getId());
        fillIndicator(indicatorData[0], indicatorData[1]);

        DirectoryOperator.getInstance().createStructure(collection);
    }

    private void setWindowLabel(String collectionName) {
        windowLabel.setText("kolekcje/ "+collectionName);
    }

    @FXML
    private void goToParameters(ActionEvent event) throws IOException {
        System.out.println("przejscie do parametrow kolekcji");
        System.out.println("CollectionView.goToParameters: "+collection);

        // utworzenie kontrolera widoku docelowego i ustawienie jego pól
        ParametersView parametersView = new ParametersView();
        parametersView.setCollection(collection);

        // przejście do widoku docelowego
        SwitchScene.getInstance().switchScene(event, "parametersView", parametersView);
    }
    @FXML
    private void addCollectible(ActionEvent event) throws IOException {
        System.out.println("dodaj obiekt");

        // Przygotuj okno CollectibleFormWindow
        CollectibleFormWindow collectibleFormWindow = new CollectibleFormWindow();
        collectibleFormWindow.setParentCollection(collection); // Przekazujemy kolekcję
        collectibleFormWindow.setOnWindowClosedListener(this);

        // Wywołaj metodę do wyświetlenia okna
        SwitchScene.getInstance().displayWindow("CollectibleFormWindow", "Dodaj obiekt", collectibleFormWindow);
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void removeCollection(ActionEvent event) throws IOException {
        System.out.println("usun kolekcje");
        ConfirmDeletePopup confirmDeletePopup = new ConfirmDeletePopup();
        confirmDeletePopup.setRemoveStructureListener(this);
        confirmDeletePopup.setSourceEvent(event);
        // widok nadrzedny do powrotu
        CollectionsView collectionsView = new CollectionsView();
        confirmDeletePopup.setParentView(collectionsView);

        SwitchScene.getInstance().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie", confirmDeletePopup);
    }
    @FXML
    private void backToCollections(ActionEvent event) throws IOException {
        System.out.println("powrot do kolekcji");
        SwitchScene.getInstance().switchScene(event, "collectionsView");
    }

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object view) {
        System.out.println("CollectionView.onDeleteConfirmed: "+collection);

        // usuwanie w bazie
        try {
            if (new CollectionOperations().deleteCollectionWithSubstructures(collection.getId()))
                System.out.println("usunieto kolekcje z bazy");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // usuwanie katalogów
        DirectoryOperator.getInstance().removeStructure(collection);

        // Spróbuj odświeżyć scenę główną
        try {
            SwitchScene.getInstance().switchScene(event, "collectionsView");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh() throws SQLException, IOException {
        // ustawienie nagłówka okna
        setWindowLabel(collection.getTitle());
        // wypełnienie listy obiektów danymi pobranymi z bazy
        collectibles = collectibleOperations.getAllCollectibles(collection.getId());
        collectiblesComponent.setCollectibles(collectibles);
        collectiblesComponent.setCollectionName(collection.getTitle());
        collectiblesComponent.fillCollectiblesHBox();
    }

    @Override
    public void onWindowClosed() {
        try {
            refresh();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillIndicator(double progressValue, double sizeValue) {
        indicatorsComponent.setProgress(progressValue);
        indicatorsComponent.setSize(sizeValue);
        indicatorsComponent.addWaveAnimation();
    }
}
