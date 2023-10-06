package com.example.fotoradar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Segment {
    private long id;
    private String name;
    private String startDate;
    private String finishDate;
    private String description;
    private long statusId;
    private long parentCollectibleId;
}
