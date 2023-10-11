package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Version {
    private int id;
    private String name;
    private String startDate;
    //private String startTime;
    private String finishDate;
    //private String finishTime;
    private int teamId;
    private int parentSegmentId;
}
