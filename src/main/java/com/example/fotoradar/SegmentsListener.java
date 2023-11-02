package com.example.fotoradar;

import com.example.fotoradar.models.Segment;

public interface SegmentsListener {
    void onCurrentSegmentChanged(Segment segment);
}
