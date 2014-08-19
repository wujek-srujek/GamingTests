package com.test.mrnom;


import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.test.framework.game.Game;
import com.test.framework.game.GameActivity;
import com.test.framework.game.GameScreen;

import java.lang.reflect.Constructor;


public class MrNomActivity extends GameActivity {

    private final static String TAG = MrNomActivity.class.getName();

    @Override
    protected GameScreen getFirstScreen(Game game, Canvas canvas) {
        return new LoadingScreen(game, canvas);
    }

    @Override
    protected void saveScreen(GameScreen screen, Bundle state) {
        state.putSerializable("screen", screen.getClass());
    }

    @Override
    protected GameScreen loadScreen(Bundle state, Canvas canvas) {
        @SuppressWarnings("unchecked")
        Class<? extends GameScreen> screenClass = (Class<? extends GameScreen>) state.getSerializable("screen");
        GameScreen screen;
        try {
            Constructor<? extends GameScreen> ctor = screenClass.getDeclaredConstructor(Game.class, Canvas.class);
            screen = ctor.newInstance(this, canvas);
        } catch (Exception exc) {
            Log.w(TAG, "cannot load screen, game will be restarted", exc);
            return null;
        }
        screen.loadState(state);
        return screen;
    }

    @Override
    protected int getVirtualWidth() {
        return 320;
    }

    @Override
    protected int getVirtualHeight() {
        return 480;
    }
}
