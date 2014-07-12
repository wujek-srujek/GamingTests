package com.test.mrnom;


public class GameLoopThread extends Thread {

    public static final int UPDATES_PER_SECOND = 50;

    public static final long MILLIS_PER_UPDATE = 1000 / UPDATES_PER_SECOND;

    private final GameUpdater gameUpdater;

    private final GameRenderer gameRenderer;

    private volatile boolean running;

    public GameLoopThread(GameUpdater gameUpdater, GameRenderer gameRenderer) {
        setName(getClass().getSimpleName());
        this.gameUpdater = gameUpdater;
        this.gameRenderer = gameRenderer;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long previous = System.currentTimeMillis();
        long lag = 0;
        while (running) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            while (lag >= MILLIS_PER_UPDATE) {
                gameUpdater.update();
                lag -= MILLIS_PER_UPDATE;
            }

            gameRenderer.render();
        }
    }
}
