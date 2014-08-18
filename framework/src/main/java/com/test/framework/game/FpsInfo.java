package com.test.framework.game;


import android.os.SystemClock;


public class FpsInfo {

    private int frames;

    private char[] fpsChars;

    private long lastFpsTimestamp;

    public FpsInfo() {
        fpsChars = new char[2];
    }

    public void reset() {
        frames = 0;
    }

    public char[] getFpsChars() {
        return fpsChars;
    }

    public void update() {
        ++frames;
        if (lastFpsTimestamp == 0) {
            lastFpsTimestamp = SystemClock.uptimeMillis();
            return;
        }
        long diff = SystemClock.uptimeMillis() - lastFpsTimestamp;
        if (diff >= 1000) {
            updateFpsChars((int) (frames * 1000 / diff));
            frames = 0;
            lastFpsTimestamp = SystemClock.uptimeMillis();
        }
    }

    private void updateFpsChars(int fps) {
        int rest = fps;
        for (int i = 0; rest > 0; ++i) {
            fpsChars[fpsChars.length - 1 - i] = (char) (rest % 10 + '0');
            rest /= 10;
        }
    }
}
