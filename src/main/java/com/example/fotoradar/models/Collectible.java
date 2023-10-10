package com.example.fotoradar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Collectible {
    private int id;
    private String name;
    private String startDate;
    private String finishDate;
    private String description;
    private int parentCollectionId;
}