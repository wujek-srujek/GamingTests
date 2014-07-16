package com.test.mrnom;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.test.mrnom.framework.game.Game;
import com.test.mrnom.framework.game.GameActivity;
import com.test.mrnom.framework.game.GameScreen;
import com.test.mrnom.framework.input.Input;
import com.test.mrnom.framework.input.TouchEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestActivity extends GameActivity {

    @Override
    protected GameScreen getFirstScreen(Game game, Canvas canvas) {
        return new TestGameScreen(game, canvas);
    }
}

class TestGameScreen extends GameScreen {

    public TestGameScreen(Game game, Canvas canvas) {
        super(game, canvas);
    }

    String[] touches = new String[game.getInput().getMaxTouches()];

    {
        Arrays.fill(touches, "");
    }

    List<String> buffer = new ArrayList<String>();

    Paint paint = new Paint();

    @Override
    public void update(float deltaTime) {
        Input input = game.getInput();
        for (int i = 0; i < input.getMaxTouches(); ++i) {
            touches[i] = String.format("touched: %b, x, y = %.2f, %.2f",
                    input.isTouchedDown(i), input.getTouchX(i), input.getTouchY(i));
        }

        buffer.clear();
        for (TouchEvent e : input.getTouchEvents()) {
            buffer.add(e.toString());
        }
    }

    @Override
    public void render(float deltaTime) {
        canvas.drawRGB(0, 0, 0);
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 10; ++i) {
            canvas.drawText(touches[i], 0, -paint.ascent() + i * paint.getTextSize(), paint);
        }

        for (int i = 0; i < buffer.size(); ++i) {
            canvas.drawText(buffer.get(i), 0, -paint.ascent() + (i + 12) * paint.getTextSize(), paint);
        }
    }
}
