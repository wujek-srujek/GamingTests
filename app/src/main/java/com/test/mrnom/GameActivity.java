package com.test.mrnom;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;


public class GameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GameModel gameModel = new GameModel();
        final GameView gameView = new GameView(getApplicationContext(), gameModel);

        setContentView(gameView);

        final GameLoopThread gameThread = new GameLoopThread(gameModel, gameView);

        gameView.getHolder().addCallback(new SurfaceHolder.Callback2() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                initGame(gameModel, holder.getSurfaceFrame());
                gameThread.setRunning(true);
                gameThread.start();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean finished = false;
                gameThread.setRunning(false);
                while (!finished) {
                    try {
                        gameThread.join();
                        finished = true;
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }

            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {
                gameView.render();
            }
        });
    }

    private void initGame(GameModel gameModel, Rect frame) {
    }

    private Bitmap createBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
}
