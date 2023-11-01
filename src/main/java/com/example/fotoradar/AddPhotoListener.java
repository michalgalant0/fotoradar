package com.example.fotoradar;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AddPhotoListener {
    void onAddingPhotosFinished(List<File> selectedFiles) throws IOException;
}
