package com.example.fotoradar;

import com.example.fotoradar.models.*;

import java.io.File;

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
        String currentCollectionsDir = currentDir+"/KOLEKCJE";
        File collectionDirectory = new File(currentCollectionsDir, collection.getTitle());
        File collectiblesDirectory = new File(collectionDirectory, "OBIEKTY");

        // utworzenie głównego katalogu kolekcji
        createDirectory(collectionDirectory, String.format("/%s", collection.getTitle()));

        // utworzenie podkatalogu kolekcji
        createDirectory(collectiblesDirectory, String.format("/%s/OBIEKTY", collection.getTitle()));
    }

    public void removeStructure(Collection collection) {
        String currentCollectionsDir = currentDir+"/KOLEKCJE/"+collection.getTitle();
        removeStructure(currentCollectionsDir);
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

    public void removeStructure(Collectible collectible, String parentCollectionName) {
        String currentCollectiblesDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY/%s", currentDir, parentCollectionName, collectible.getTitle());
        removeStructure(currentCollectiblesDir);
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

    public void removeStructure(Segment segment, String parentCollectionName, String parentCollectibleName) {
        String currentSegmentsDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s",
                        currentDir, parentCollectionName, parentCollectibleName, segment.getTitle());
        removeStructure(currentSegmentsDir);
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

    public void removeStructure(Version version, String parentCollectionName, String parentCollectibleName, String parentSegmentName) {
        String currentVersionsDir =
                String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s",
                        currentDir, parentCollectionName, parentCollectibleName, parentSegmentName, version.getName());
        removeStructure(currentVersionsDir);
    }

    public void removeStructure(Photo photo, String parentDirectory) {
        String photoToRemovePath = parentDirectory + photo.getFileName();
        removeStructure(photoToRemovePath);
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
