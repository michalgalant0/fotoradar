package com.example.fotoradar.models;

import lombok.*;

public class Thumbnail extends ImageModel {
    public Thumbnail(int id, String fileName, int parentCollectibleId) {
        this.setId(id);
        this.setFileName(fileName);
        this.setParentId(parentCollectibleId);
    }
    public Thumbnail(String fileName, int parentCollectibleId) {
        this.setFileName(fileName);
        this.setParentId(parentCollectibleId);
    }
}
