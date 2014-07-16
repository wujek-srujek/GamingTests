package com.test.mrnom.framework.game;


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
        long startTime = System.nanoTime();
        while (running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            game.update(deltaTime);
            game.render(deltaTime);
        }
    }
}
