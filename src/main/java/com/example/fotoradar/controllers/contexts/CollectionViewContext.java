package com.example.fotoradar.controllers.contexts;

import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.util.ArrayList;

@Getter @Setter @ToString
public class CollectionViewContext extends MainContext {
    protected ArrayList<Thumbnail> thumbnails;
    protected ArrayList<Collectible> collectibles;

    public CollectionViewContext() {
        System.out.println(getCurrentCollection());
        setFields();
    }

    private void setFields() {
        try {
            collectibles = collectibleOperations.getAllCollectiblesByParentId(getCurrentCollection().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        thumbnails = new ArrayList<>();
        for (Collectible collectible : collectibles) {
            thumbnails.add(thumbnailOperations.getMainThumbnail(collectible.getId()));
        }
    }
}
