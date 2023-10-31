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
    private int statusId;
    private int parentCollectibleId;
    private String coords;

    public Segment(
            int id, String title, String startDate, String finishDate,
            String description, int statusId, int parentCollectibleId
    ) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.description = description;
        this.statusId = statusId;
        this.parentCollectibleId = parentCollectibleId;
    }
}
