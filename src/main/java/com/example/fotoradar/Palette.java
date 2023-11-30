package com.example.fotoradar;

import javafx.scene.paint.Color;
import lombok.Getter;

public enum Palette {
    MAIN(Color.TURQUOISE),
    SECONDARY(Color.LIGHTPINK);
    @Getter
    private final Color color;
    Palette(Color color) {
        this.color = color;
    }
}
