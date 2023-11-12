package com.example.fotoradar.listeners;

import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.segmenter.Segmenter;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SegmenterListener {
    void onSegmentationFinished(ArrayList<Segmenter.Segment> segments, int segmentedThumbnailId) throws SQLException;
}
