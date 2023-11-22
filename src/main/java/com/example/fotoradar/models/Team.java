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

    public Team(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Team(String name, String description, int parentCollectionId) {
        this.name = name;
        this.description = description;
        this.parentCollectionId = parentCollectionId;

    }
}
