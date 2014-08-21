package com.test.framework.game;


import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public abstract class GLGameActivity extends GameActivity<GL10, GLSurfaceView> implements GLSurfaceView.Renderer {

    private long startTime;

    @Override
    protected GLSurfaceView prepare() {
        GLSurfaceView view = new GLSurfaceView(this);
        view.getHolder().setFixedSize(getVirtualWidth(), getVirtualHeight());
        view.setRenderer(this);

        return view;
    }

    @Override
    protected DebugInfo<GL10> getDebugInfo() {
        return new GLDebugInfo(getVirtualWidth(), getVirtualHeight());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mainView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainView.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        screen.resume();
        startTime = SystemClock.uptimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // nothing to do here
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float deltaTime = SystemClock.uptimeMillis() - startTime;
        startTime = System.nanoTime();
        update(deltaTime);
        render(gl, deltaTime);
    }
}
