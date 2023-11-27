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

    public Team(String name, String description) {
        this.setName(name);
        this.setDescription(description);
    }

    public Team(String name, String descripton, int parentCollectionId) {
        this.setName(name);
        this.setDescription(description);
        this.setParentCollectionId(parentCollectionId);
    }
}


