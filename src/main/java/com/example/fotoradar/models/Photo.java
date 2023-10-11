package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Photo {
    private int id;
    private String fileName;
    private String filePath;
    private int parentVersionId;
}
