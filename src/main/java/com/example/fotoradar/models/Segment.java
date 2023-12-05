package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Segment {
    private int id;
    private String title;
    private String startDate;
    private String finishDate;
    private String description;
    private String coords;
    private Status status;
    private int parentCollectibleId;
    private int thumbnailId;

    public Segment(String title, String coords, int parentCollectibleId, int thumbnailId) {
        this.title = title;
        this.coords = coords;
        this.parentCollectibleId = parentCollectibleId;
        this.thumbnailId = thumbnailId;
    }
}
