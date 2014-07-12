package com.test.mrnom;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;


@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements GameRenderer {

    private final GameModel gameModel;

    public GameView(Context context, GameModel gameModel) {
        super(context);
        this.gameModel = gameModel;
    }

    @Override
    public void render() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                render(canvas);
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (GameObject object : gameModel.getObjects()) {
            object.render(canvas);
        }
    }
}
