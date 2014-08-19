package com.test.framework.game;


import android.os.SystemClock;


public class GameLoopThread extends Thread {

    private final Game game;

    private volatile boolean running;

    public GameLoopThread(Game game) {
        setName(getClass().getSimpleName());
        this.game = game;
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
            game.render(deltaTime);
        }
    }
}
