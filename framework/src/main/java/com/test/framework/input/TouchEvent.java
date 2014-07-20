package com.test.framework.input;


import java.util.Locale;


public class TouchEvent {

    public enum Type {
        DOWN, UP, MOVE
    }

    public Type type;

    public int pointer;

    public float x;

    public float y;

    @Override
    public String toString() {
        return String.format(Locale.US, "type: %s, pointer: %d, x: %.2f, y: %.2f", type, pointer, x, y);
    }
}
