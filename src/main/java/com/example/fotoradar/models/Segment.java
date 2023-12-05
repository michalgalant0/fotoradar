package com.example.fotoradar.models;

import com.example.fotoradar.segmenter.Segmenter;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class Segment {
    private int id;
    private String title;
    private String startDate;
    private String finishDate;
    private String description;
    private String coords;
    private Status status;
    private int parentCollectibleId;
    private int thumbnailId;

    public Segment(String title, String coords, int parentCollectibleId, int thumbnailId) {
        this.title = title;
        this.coords = coords;
        this.parentCollectibleId = parentCollectibleId;
        this.thumbnailId = thumbnailId;
    }

    public Segmenter.Segment toSegmenterSegment() {
        Segmenter.Segment segmenterSegment = new Segmenter.Segment();

        // Wykorzystanie wyrażenia regularnego do wyciągnięcia współrzędnych z pola coords
        Pattern pattern = Pattern.compile("\\((\\d+\\.\\d+),(\\d+\\.\\d+)\\)");
        Matcher matcher = pattern.matcher(coords);

        while (matcher.find()) {
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(2));
            segmenterSegment.addPoint(x, y);
        }

        return segmenterSegment;
    }
}
