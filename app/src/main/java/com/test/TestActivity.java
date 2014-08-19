package com.test;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.test.framework.game.Game;
import com.test.framework.game.GameActivity;
import com.test.framework.game.GameScreen;
import com.test.framework.input.Input;


public class TestActivity extends GameActivity {

    @Override
    protected GameScreen getFirstScreen(Game game, Canvas canvas) {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(game.getGraphics().getHeight() / 50);
        paint.setTypeface(Typeface.MONOSPACE);
        return new GameScreen(game, canvas) {

            private final String[] touches = new String[game.getInput().getMaxTouches()];

            @Override
            public void update(float deltaTime) {
                Input input = game.getInput();
                for (int i = 0; i < input.getMaxTouches(); ++i) {
                    touches[i] = String.format("touched: %b, x, y = %.2f, %.2f",
                            input.isTouchedDown(i), input.getTouchX(i), input.getTouchY(i));
                }
            }

            @Override
            public void render(float deltaTime) {
                canvas.drawRGB(0, 0, 0);
                paint.setColor(Color.WHITE);
                for (int i = 0; i < touches.length; ++i) {
                    canvas.drawText(touches[i], 0, -paint.ascent() + i * paint.getTextSize(), paint);
                }
            }
        };
    }

    @Override
    protected int getVirtualWidth() {
        return super.getVirtualWidth() / 2;
    }

    @Override
    protected int getVirtualHeight() {
        return super.getVirtualHeight() / 2;
    }
}
