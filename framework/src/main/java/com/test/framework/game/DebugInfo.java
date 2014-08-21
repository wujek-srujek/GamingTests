package com.test.framework.game;


public abstract class DebugInfo<R> {

    protected final int width;

    protected final FpsInfo fpsInfo;

    public DebugInfo(int width, int height) {
        this.width = width;
        fpsInfo = new FpsInfo();
    }

    public void render(R renderer) {
        fpsInfo.update();
        char[] fpsChars = fpsInfo.getFpsChars();
        render(renderer, fpsChars);
    }

    protected abstract void render(R renderer, char[] fpsChars);
}
