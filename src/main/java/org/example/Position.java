package org.example;

import com.googlecode.lanterna.TerminalPosition;

public class Position extends TerminalPosition {
    int x;
    int y;

    public Position(int x, int y) {
        super(x,y);
        this.x = x;
        this.y = y;
    }
}
