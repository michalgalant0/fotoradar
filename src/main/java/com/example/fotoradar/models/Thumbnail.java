package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Thumbnail {
    private int id;
    private String fileName;
    private int parentCollectibleId;

    public Thumbnail(String fileName, int parentCollectibleId) {
        this.fileName = fileName;
        this.parentCollectibleId = parentCollectibleId;
    }
}
