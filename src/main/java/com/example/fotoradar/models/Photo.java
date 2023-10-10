package com.example.fotoradar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Photo {
    private int id;
    private String fileName;
    private String filePath;
    private int parentVersionId;
}
