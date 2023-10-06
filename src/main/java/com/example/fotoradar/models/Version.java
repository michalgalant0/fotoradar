package com.example.fotoradar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Version {
    private long id;
    private String name;
    private String startDate;
    private String startTime;
    private String finishDate;
    private String finishTime;
    private long parentSegmentId;
}
