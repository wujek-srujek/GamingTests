package com.test;


import com.test.framework.game.GLGameActivity;
import com.test.framework.game.Game;
import com.test.framework.game.GameScreen;
import com.test.framework.input.Input;
import com.test.framework.input.TouchEvent;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;


public class GLTestActivity extends GLGameActivity {

    @Override
    protected GameScreen<GL10> getFirstScreen(Game<GL10> game) {
        return new GameScreen<GL10>(game) {

            private float r;

            private float g;

            private final String[] touches = new String[game.getInput().getMaxTouches()];

            @Override
            public void update(float deltaTime) {
                Input input = game.getInput();
                input.clearKeyBuffer();
                List<TouchEvent> recentTouches = input.getTouchEvents();
                TouchEvent lastTouch = recentTouches.isEmpty() ? null : recentTouches.get(recentTouches.size() - 1);
                if (lastTouch != null) {
                    r = lastTouch.x / game.getGraphics().getWidth();
                    g = lastTouch.y / game.getGraphics().getHeight();
                }
                for (int i = 0; i < input.getMaxTouches(); ++i) {
                    touches[i] = String.format("touched: %b, x, y = %.2f, %.2f",
                            input.isTouchedDown(i), input.getTouchX(i), input.getTouchY(i));
                }
            }

            @Override
            public void render(GL10 gl, float deltaTime) {
                gl.glClearColor(r, g, 0F, 1F);
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
                for (int i = 0; i < touches.length; ++i) {
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
