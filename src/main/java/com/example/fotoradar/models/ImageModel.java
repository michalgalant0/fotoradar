package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class ImageModel {
    private int id;
    private String fileName;
    private int parentId;

    public Thumbnail imageModelToThumbnail() {
        return new Thumbnail(this.getId(), this.getFileName(), this.getParentId());
    }

    public Photo imageModelToPhoto() {
        return new Photo(this.getId(), this.getFileName(), this.getParentId());
    }
}
