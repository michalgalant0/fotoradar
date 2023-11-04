package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Photo extends ImageModel {
    private String geoLocation;
    private float fileSize;

    public Photo(int id, String fileName, int parentVersionId) {
        this.setId(id);
        this.setFileName(fileName);
        this.setParentId(parentVersionId);
    }
}
