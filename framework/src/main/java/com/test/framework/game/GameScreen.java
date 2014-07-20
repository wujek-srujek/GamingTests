package com.test.framework.game;


import android.graphics.Canvas;


public abstract class GameScreen {

    protected final Game game;

    protected final Canvas canvas;

    protected GameScreen(Game game, Canvas canvas) {
        this.game = game;
        this.canvas = canvas;
    }

    protected void dispose() {
        // nothing by default
    }

    protected void pause() {
        // nothing by default
    }

    protected void resume() {
        // nothing by default
    }

    protected abstract void update(float deltaTime);

    protected abstract void render(float deltaTime);
}
