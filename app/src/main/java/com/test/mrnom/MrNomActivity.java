package com.test.mrnom;


import android.graphics.Canvas;

import com.test.mrnom.framework.game.Game;
import com.test.mrnom.framework.game.GameActivity;
import com.test.mrnom.framework.game.GameScreen;


public class MrNomActivity extends GameActivity {

    @Override
    protected GameScreen getFirstScreen(Game game, Canvas canvas) {
        return new LoadingScreen(game, canvas);
    }
}
