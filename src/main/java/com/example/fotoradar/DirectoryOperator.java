package com.example.fotoradar;

import com.example.fotoradar.models.*;

import java.io.File;
import java.nio.file.Paths;

public class DirectoryOperator {
    private static DirectoryOperator instance;
    private final String currentDir = Main.getDefPath();

    public static synchronized DirectoryOperator getInstance() {
        if (instance == null)
            instance = new DirectoryOperator();
        return instance;
    }

    // dla kolekcji (wszystkich)
    public void createStructure() {
        File collectionsDirectory = new File(currentDir, "KOLEKCJE");
        createDirectory(collectionsDirectory, "kolekcje(all)");

        // utworzenie katalogu do raportów
        File reportsDirectory = new File(currentDir, "RAPORTY");
        createDirectory(reportsDirectory, "raporty(all)");
    }

    // dla kolekcji
    public void createStructure(Collection collection) {
        String currentCollectionsDir = Paths.get(currentDir,"KOLEKCJE").toString();
        File collectionDirectory = new File(currentCollectionsDir, collection.getTitle());
        File collectiblesDirectory = new File(collectionDirectory, "OBIEKTY");

        // utworzenie głównego katalogu kolekcji
        createDirectory(collectionDirectory, String.format("/%s", collection.getTitle()));

        // utworzenie podkatalogu kolekcji
        createDirectory(collectiblesDirectory, String.format("/%s/OBIEKTY", collection.getTitle()));
    }

    public void removeStructure(Collection collection) {
        String currentCollectionsDir = Paths.get(currentDir, "KOLEKCJE",collection.getTitle()).toString();
        removeStructure(currentCollectionsDir);
    }

    // dla obiektu
    public void createStructure(Collectible collectible, String parentCollectionName) {
        String currentCollectiblesDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY").toString();
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

    public void removeStructure(Collectible collectible, String parentCollectionName) {
        String currentCollectiblesDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY", collectible.getTitle()).toString();
        removeStructure(currentCollectiblesDir);
    }

    // dla segmentu
    public void createStructure(Segment segment, String parentCollectionName, String parentCollectibleName) {
        String currentSegmentsDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY", parentCollectibleName, "SEGMENTY").toString();
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

    public void removeStructure(Segment segment, String parentCollectionName, String parentCollectibleName) {
        String currentSegmentsDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY", parentCollectibleName, "SEGMENTY", segment.getTitle()).toString();
        removeStructure(currentSegmentsDir);
    }

    // dla wersji
    public void createStructure(Version version, String parentCollectionName, String parentCollectibleName, String parentSegmentName) {
        String currentVersionsDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY", parentCollectibleName, "SEGMENTY", parentSegmentName, "WERSJE").toString();
        File versionDirectory = new File(currentVersionsDir, version.getName());
        createDirectory(versionDirectory,
                String.format("/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s",
                        parentCollectionName, parentCollectibleName, parentSegmentName, version.getName())
        );

        File versionTmpDirectory = new File(Paths.get(currentVersionsDir, version.getName()).toString(), ".tmp");
        createDirectory(versionTmpDirectory, "katalog dla zmniejszonych dla wersji");
    }

    public void removeStructure(Version version, String parentCollectionName, String parentCollectibleName, String parentSegmentName) {
        String currentVersionsDir = Paths.get(currentDir, "KOLEKCJE", parentCollectionName, "OBIEKTY", parentCollectibleName, "SEGMENTY", parentSegmentName, "WERSJE", version.getName()).toString();

        removeStructure(currentVersionsDir);
    }

    public void removeStructure(Photo photo, String parentDirectory) {
        // usuniecie podstawowego zdjęcia
        String photoToRemovePath = Paths.get(parentDirectory, photo.getFileName()).toString();
        removeStructure(photoToRemovePath);
        // usunięcie miniaturki zdjęcia z katalogu .tmp
        String tmpPhotoToRemovePath = Paths.get(parentDirectory, ".tmp", photo.getFileName()).toString();
        removeStructure(tmpPhotoToRemovePath);
    }

    public void removeStructure(Thumbnail thumbnail, String parentDirectory) {
        String thumbnailToRemovePath = parentDirectory + thumbnail.getFileName();
        removeStructure(thumbnailToRemovePath);
    }

    /**
     * @param directoryToCreate obiekt dla którego struktura ma zostać utworzona
     * @param type typ obiektu - dla debugowania
     */
    private void createDirectory(File directoryToCreate, String type) {
        System.out.println("DirectoryOperator.createDirectory directoryToCreate "+directoryToCreate);
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

    private void removeStructure(String path) {
        File structure = new File(path);

        if (!structure.exists()) {
            System.out.println("Struktura nie istnieje: " + path);
            return;
        }

        try {
            removeDirectory(structure);
        } catch (Exception e) {
            System.out.println("Błąd podczas usuwania plików: " + e.getMessage());
        }

        if (structure.delete()) {
            System.out.println("Katalog usunięty: " + path);
        } else {
            System.out.println("Nie udało się usunąć katalogu: " + path);
        }
    }

    private boolean removeDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!removeDirectory(file)) {
                        return false;
                    }
                } else {
                    if (!file.delete()) {
                        System.out.println("Nie udało się usunąć pliku: " + file);
                        return false;
                    }
                }
            }
        }

        // Usuwamy sam katalog nadrzędny po usunięciu wszystkich plików i podkatalogów
        return directory.delete();
    }

    public boolean updateDirectoryName(String currentPath, String newName) {
        // Tworzenie obiektu reprezentującego katalog
        File file = new File(currentPath);

        // Sprawdzenie, czy katalog istnieje
        if (!file.exists()) {
            System.out.println("Katalog o nazwie " + currentPath + " nie istnieje.");
            return false;
        }

        // Tworzenie obiektu reprezentującego nową nazwę katalogu
        File updatedFile = new File(file.getParent(), newName);

        // Zmiana nazwy katalogu
        boolean success = file.renameTo(updatedFile);

        if (success) {
            System.out.println("Nazwa katalogu została pomyślnie zmieniona na " + newName);
        } else {
            System.out.println("Nie udało się zmienić nazwy katalogu.");
        }

        return success;
    }
}
