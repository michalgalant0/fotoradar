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

    public Version(String name, String startDate, String finishDate, int teamId, int parentSegmentId) {
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.teamId = teamId;
        this.parentSegmentId = parentSegmentId;
    }
}
