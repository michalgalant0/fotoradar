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

    // konstruktor na potrzeby tworzenia obiektu kolekcji bez id
    // (etap przejściowy przy dodawaniu kolekcji do bazy)
    public Collectible(String title, String startDate, String finishDate, String description, int parentCollectionId) {
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setFinishDate(finishDate);
        this.setDescription(description);
        this.setParentCollectionId(parentCollectionId);
    }
}