package com.example.fotoradar;

import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Version;

import java.io.File;

public class DirectoryOperator {
    private final String currentDir = Main.getDefPath();

    // dla kolekcji (wszystkich)
    public void createStructure() {
        File collectionsDirectory = new File(currentDir, "KOLEKCJE");
        createDirectory(collectionsDirectory, "kolekcje(all)");
    }

    // dla kolekcji
    public void createStructure(Collection collection) {
        String currentCollectionsDir = currentDir+"/KOLEKCJE";
        File collectionDirectory = new File(currentCollectionsDir, collection.getTitle());
        File collectiblesDirectory = new File(collectionDirectory, "OBIEKTY");

        // utworzenie głównego katalogu kolekcji
        createDirectory(collectionDirectory, String.format("/%s", collection.getTitle()));

        // utworzenie podkatalogu kolekcji
        createDirectory(collectiblesDirectory, String.format("/%s/OBIEKTY", collection.getTitle()));
    }

    // dla obiektu
    public void createStructure(Collectible collectible, String parentCollectionName) {
        String currentCollectiblesDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY", currentDir, parentCollectionName);
        File collectibleDirectory = new File(currentCollectiblesDir, collectible.getTitle());
        File thumbnailsDir = new File(collectibleDirectory, "MINIATURY");
        File segmentsDir = new File(collectibleDirectory, "SEGMENTY");

        // utworzenie głównego katalogu obiektu
        createDirectory(collectibleDirectory,
                String.format("/%s/OBIEKTY/%s", parentCollectionName, collectible.getTitle())
        );

        // utworzenie podkatalogów obiektu
        createDirectory(thumbnailsDir,
                String.format("/%s/OBIEKTY/%s/MINIATURY", parentCollectionName, collectible.getTitle())
        );
        createDirectory(segmentsDir,
                String.format("/%s/OBIEKTY/%s/SEGMENTY", parentCollectionName, collectible.getTitle())
        );
    }

    // dla segmentu
    public void createStructure(Segment segment, String parentCollectionName, String parentCollectibleName) {
        String currentSegmentsDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY",
                        currentDir, parentCollectionName, parentCollectibleName);
        File segmentDirectory = new File(currentSegmentsDir, segment.getTitle());
        File versionsDir = new File(segmentDirectory, "WERSJE");

        // utworzenie głównego katalogu segmentu
        createDirectory(segmentDirectory,
                String.format("/%s/OBIEKTY/%s/SEGMENTY/%s",
                        parentCollectionName, parentCollectibleName, segment.getTitle())
        );

        // utworzenie podkatalogu segmentu
        createDirectory(versionsDir,
                String.format("/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE",
                        parentCollectionName, parentCollectibleName, segment.getTitle())
        );
    }

    // dla wersji
    public void createStructure(Version version, String parentCollectionName, String parentCollectibleName, String parentSegmentName) {
        String currentVersionsDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE",
                        currentDir, parentCollectionName, parentCollectibleName, parentSegmentName);
        File versionDirectory = new File(currentVersionsDir, version.getName());
        createDirectory(versionDirectory,
                String.format("/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s",
                        parentCollectionName, parentCollectibleName, parentSegmentName, version.getName())
        );
    }

    /**
     * @param directoryToCreate obiekt dla którego struktura ma zostać utworzona
     * @param type typ obiektu - dla debugowania
     */
    private void createDirectory(File directoryToCreate, String type) {
        if (!directoryToCreate.exists()) {
            boolean directoryCreated = directoryToCreate.mkdir();
            System.out.println(
                    directoryCreated ?
                            "utworzono katalog "+type
                            : "nie utworzono katalogu "+type
            );
        } else
            System.out.println("katalog "+type+" istnieje");
    }
}
