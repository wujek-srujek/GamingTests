package com.test.framework.game;


import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;


public class GameLoopThread extends Thread {

    private final Game game;

    private final SurfaceHolder holder;

    private volatile boolean running;

    public GameLoopThread(Game game, SurfaceHolder holder) {
        setName(getClass().getSimpleName());
        this.game = game;
        this.holder = holder;
    }

    @Override
    public synchronized void start() {
        running = true;
        super.start();
    }

    public void finish() {
        running = false;
    }

    @Override
    public void run() {
        long startTime = SystemClock.uptimeMillis();
        while (running) {
            float deltaTime = (SystemClock.uptimeMillis() - startTime) / 1000.0f;
            startTime = SystemClock.uptimeMillis();
            game.update(deltaTime);
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    game.render(canvas, deltaTime);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
