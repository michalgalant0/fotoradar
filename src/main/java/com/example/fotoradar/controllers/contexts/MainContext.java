package com.example.fotoradar.controllers.contexts;

import com.example.fotoradar.databaseOperations.*;
import com.example.fotoradar.models.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainContext {

    @Getter
    protected ArrayList<Collection> collections;
    @Getter
    protected ArrayList<Collectible> collectibles;

    @Setter @Getter
    private Collection currentCollection;
    @Setter @Getter
    private Collectible currentCollectible;
    @Setter @Getter
    private Segment currentSegment;
    @Setter @Getter
    private Version currentVersion;
    @Setter @Getter
    private ArrayList<Status> statuses;

    protected final CollectionOperations collectionOperations;
    protected final CollectibleOperations collectibleOperations;
    protected final SegmentOperations segmentOperations;
    protected final VersionOperations versionOperations;
    protected final StatusOperations statusOperations;
    protected final ThumbnailOperations thumbnailOperations;


    @Getter
    private final String rootDirectory = System.getProperty("user.dir");
    private final String photoPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s";
    @Getter
    private final String thumbnailPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/%s";

    public MainContext() {
        try {
            collectionOperations = new CollectionOperations();
            collectibleOperations = new CollectibleOperations();
            segmentOperations = new SegmentOperations();
            versionOperations = new VersionOperations();
            statusOperations = new StatusOperations();
            thumbnailOperations = new ThumbnailOperations();

            collections = collectionOperations.getAllCollections();
            collectibles = collectibleOperations.getAllCollectibles();
            statuses = statusOperations.getAllStatuses();

            System.out.println("MainContext:");
            System.out.println(collections);
            System.out.println(collectibles);
            System.out.println(statuses);
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class InstanceHolder {
        private static final MainContext instance = new MainContext();
    }

    public static MainContext getInstance() {
        return InstanceHolder.instance;
    }

    // todo metody do ścieżek - chuj wie jak
}
