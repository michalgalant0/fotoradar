package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Collectible {
    private int id;
    private String title;
    private String startDate;
    private String finishDate;
    private String description;
    private int parentCollectionId;
}