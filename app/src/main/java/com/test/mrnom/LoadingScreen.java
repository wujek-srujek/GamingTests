package com.test.mrnom;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.test.framework.game.Game;
import com.test.framework.game.GameScreen;
import com.test.framework.gfx.Graphics;


public class LoadingScreen extends GameScreen<Canvas> {

    protected LoadingScreen(Game<Canvas> game) {
        super(game);
    }

    @Override
    protected void update(float deltaTime) {
        Graphics g = game.getGraphics();
        if (Assets.background == null) {
            // load the background and return as soon as possible to render it
            Assets.background = g.newBitmap("background.png", Bitmap.Config.RGB_565);
        } else if (Assets.bitten == null) {
            // load everything else
            Assets.logo = g.newBitmap("logo.png", Bitmap.Config.ARGB_4444);
            Assets.mainMenu = g.newBitmap("mainmenu.png", Bitmap.Config.ARGB_4444);
            Assets.buttons = g.newBitmap("buttons.png", Bitmap.Config.ARGB_4444);
            Assets.help1 = g.newBitmap("help1.png", Bitmap.Config.ARGB_4444);
            Assets.help2 = g.newBitmap("help2.png", Bitmap.Config.ARGB_4444);
            Assets.help3 = g.newBitmap("help3.png", Bitmap.Config.ARGB_4444);
            Assets.numbers = g.newBitmap("numbers.png", Bitmap.Config.ARGB_4444);
            Assets.ready = g.newBitmap("ready.png", Bitmap.Config.ARGB_4444);
            Assets.pause = g.newBitmap("pausemenu.png", Bitmap.Config.ARGB_4444);
            Assets.gameOver = g.newBitmap("gameover.png", Bitmap.Config.ARGB_4444);
            Assets.headUp = g.newBitmap("headup.png", Bitmap.Config.ARGB_4444);
            Assets.headLeft = g.newBitmap("headleft.png", Bitmap.Config.ARGB_4444);
            Assets.headDown = g.newBitmap("headdown.png", Bitmap.Config.ARGB_4444);
            Assets.headRight = g.newBitmap("headright.png", Bitmap.Config.ARGB_4444);
            Assets.tail = g.newBitmap("tail.png", Bitmap.Config.ARGB_4444);
            Assets.food1 = g.newBitmap("food1.png", Bitmap.Config.ARGB_4444);
            Assets.food2 = g.newBitmap("food2.png", Bitmap.Config.ARGB_4444);
            Assets.food3 = g.newBitmap("food3.png", Bitmap.Config.ARGB_4444);

            Assets.click = game.getAudio().newSound("click.ogg");
            Assets.eat = game.getAudio().newSound("eat.ogg");
            Assets.bitten = game.getAudio().newSound("bitten.ogg");

            Settings.load(game.getFileIO());
        }

        if (Assets.bitten != null) {
            game.switchToScreen(new MainScreen(game));
        }
    }

    @Override
    protected void render(Canvas canvas, float deltaTime) {
        if (Assets.background != null) {
            canvas.drawBitmap(Assets.background, 0F, 0F, null);
        }
    }
}
