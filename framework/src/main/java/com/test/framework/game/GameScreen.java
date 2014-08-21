package com.test.framework.game;


import android.os.Bundle;


public abstract class GameScreen<R> {

    protected final Game<R> game;

    protected GameScreen(Game<R> game) {
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

    protected abstract void render(R renderer, float deltaTime);

    public void saveState(Bundle state) {
        // nothing by defaut
    }

    public void loadState(Bundle state) {
        // nothing by default
    }
}
