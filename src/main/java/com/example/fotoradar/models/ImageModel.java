package com.example.fotoradar.models;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class ImageModel {
    private int id;
    private String fileName;
    private int parentId;
}
