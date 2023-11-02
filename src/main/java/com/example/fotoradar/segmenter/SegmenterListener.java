package com.example.fotoradar.segmenter;

import com.example.fotoradar.models.Thumbnail;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SegmenterListener {
    void onSegmentationFinished(ArrayList<Segmenter.Segment> segments, int segmentedThumbnailId) throws SQLException;
}
