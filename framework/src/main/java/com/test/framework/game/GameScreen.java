package com.test.framework.game;


import android.graphics.Canvas;
import android.os.Bundle;


public abstract class GameScreen {

    protected final Game game;

    protected GameScreen(Game game) {
        this.game = game;
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

    protected abstract void render(Canvas canvas, float deltaTime);

    public void saveState(Bundle state) {
        // nothing by defaut
    }

    public void loadState(Bundle state) {
        // nothing by default
    }
}
