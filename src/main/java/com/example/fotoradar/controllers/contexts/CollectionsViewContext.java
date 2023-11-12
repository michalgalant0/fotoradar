package com.example.fotoradar.controllers.contexts;

import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter @Setter @ToString
public class CollectionsViewContext extends MainContext {
    protected ArrayList<Thumbnail> thumbnails;

    public CollectionsViewContext() {
        setFields();
    }

    private void setFields() {
        thumbnails = new ArrayList<>();
        for (Collectible collectible : collectibles) {
            thumbnails.add(thumbnailOperations.getMainThumbnail(collectible.getId()));
        }

        System.out.println("CollectionsViewContext.setFields:");
        System.out.println(collections);
        System.out.println(collectibles);
        System.out.println(thumbnails);
        System.out.println();
    }
}
