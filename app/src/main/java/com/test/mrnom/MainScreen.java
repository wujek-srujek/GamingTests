package com.test.mrnom;


import android.graphics.Canvas;
import android.graphics.Rect;

import com.test.framework.game.Game;
import com.test.framework.game.GameScreen;
import com.test.framework.gfx.Graphics;
import com.test.framework.input.TouchEvent;

import java.util.List;


public class MainScreen extends GameScreen<Canvas> {

    private final Rect playButtonSrcRect;

    private final Rect playButtonDstRect;

    private final Rect musicButtonSrcRect;

    private final Rect musicButtonDstRect;

    protected MainScreen(Game<Canvas> game) {
        super(game);
        playButtonSrcRect = new Rect(0, 0, 192, 42);
        playButtonDstRect = new Rect(64, 260, 256, 302);
        musicButtonSrcRect = new Rect(0, 0, 64, 64);
        musicButtonDstRect = new Rect(0, 416, 64, 480);
    }

    @Override
    protected void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    protected void update(float deltaTime) {
        game.getInput().clearKeyBuffer();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for (int i = 0; i < len; ++i) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.Type.UP) {
                if (inBounds(event, musicButtonDstRect)) {
                    if (!Settings.soundEnabled) {
                        // this time, we are enabling the sound so let's play
                        Assets.click.play(1F);
                    }
                    Settings.soundEnabled = !Settings.soundEnabled;
                }
                if (inBounds(event, playButtonDstRect)) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1F);
                    }
                    game.switchToScreen(new PlayScreen(game));
                    return;
                }
            }
        }

        if (Settings.soundEnabled) {
            musicButtonSrcRect.left = 0;
            musicButtonSrcRect.right = 64;
        } else {
            musicButtonSrcRect.left = 64;
            musicButtonSrcRect.right = 128;
        }
    }

    @Override
    protected void render(Canvas canvas, float deltaTime) {
        canvas.drawBitmap(Assets.background, 0, 0, null);
        canvas.drawBitmap(Assets.logo, 32, 20, null);
        canvas.drawBitmap(Assets.mainMenu, playButtonSrcRect, playButtonDstRect, null);
        canvas.drawBitmap(Assets.buttons, musicButtonSrcRect, musicButtonDstRect, null);
    }

    private boolean inBounds(TouchEvent event, Rect rect) {
        return event.x > rect.left && event.x < rect.right && event.y > rect.top && event.y < rect.bottom;
    }
}
