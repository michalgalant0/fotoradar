package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Collection {
    private int id;
    private String title;
    private String startDate;
    private String finishDate;
    private String description;

    // konstruktor na potrzeby tworzenia obiektu kolekcji bez id
    // (etap przejściowy przy dodawaniu kolekcji do bazy)
    public Collection(String title, String startDate, String finishDate, String description) {
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setFinishDate(finishDate);
        this.setDescription(description);
    }
}
