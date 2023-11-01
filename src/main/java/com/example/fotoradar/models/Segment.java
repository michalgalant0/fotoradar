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
    private int statusId;
    private int parentCollectibleId;
    private int thumbnailId;
}
