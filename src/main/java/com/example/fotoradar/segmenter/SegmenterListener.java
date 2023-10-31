package com.example.fotoradar.segmenter;

import java.util.ArrayList;

public interface SegmenterListener {
    void onSegmentationFinished(ArrayList<Segmenter.Segment> segments);
}
