package com.example.fotoradar;

import java.io.File;
import java.util.List;

public interface AddPhotoListener {
    void onAddingPhotosFinished(List<File> selectedFiles);
}
