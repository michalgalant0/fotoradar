package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Team {
    private int id;
    private String name;
    private String description;
    private int parentCollectionId;
}
