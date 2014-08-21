package com.test.framework.game;


import javax.microedition.khronos.opengles.GL10;


public class GLDebugInfo extends DebugInfo<GL10> {

    public GLDebugInfo(int width, int height) {
        super(width, height);
    }

    @Override
    protected void render(GL10 gl, char[] fpsChars) {
    }
}
