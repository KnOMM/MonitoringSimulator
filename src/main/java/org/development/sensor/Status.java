package org.development.sensor;

import com.googlecode.lanterna.TextColor;

public enum Status {
    COOL (TextColor.ANSI.CYAN),
    COLD (TextColor.ANSI.WHITE),
    NORMAL(TextColor.ANSI.GREEN),
    WARM (TextColor.ANSI.YELLOW),
    HOT (TextColor.ANSI.RED);

    TextColor.ANSI color;

    Status(TextColor.ANSI color) {
        this.color = color;
    }

    public TextColor.ANSI getColor() {
        return color;
    }
}
