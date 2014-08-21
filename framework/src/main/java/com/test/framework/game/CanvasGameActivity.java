package com.test.framework.game;


import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public abstract class CanvasGameActivity extends GameActivity<Canvas, SurfaceView> {

    private GameLoopThread gameThread;

    @Override
    protected SurfaceView prepare() {
        SurfaceView view = new SurfaceView(this);
        setContentView(view);
        view.getHolder().setFixedSize(getVirtualWidth(), getVirtualHeight());
        view.getHolder().addCallback(new SurfaceHolder.Callback() {

            private boolean creation;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                creation = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (creation) {
                    gameThread = new GameLoopThread(CanvasGameActivity.this, holder);
                    gameThread.start();
                    creation = false;
                }
                screen.resume();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                screen.pause();
                gameThread.finish();
                while (true) {
                    try {
                        gameThread.join();
                        break;
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }

                if (isFinishing()) {
                    screen.dispose();
                }
            }
        });

        return view;
    }

    @Override
    protected DebugInfo<Canvas> getDebugInfo() {
        return new CanvasDebugInfo(getVirtualWidth(), getVirtualHeight());
    }
}
