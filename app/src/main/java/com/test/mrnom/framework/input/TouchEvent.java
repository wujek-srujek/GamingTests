package com.test.mrnom.framework.input;


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
        return String.format("type: %s, pointer: %d, x: %.2f, y: %.2f", type, pointer, x, y);
    }
}
