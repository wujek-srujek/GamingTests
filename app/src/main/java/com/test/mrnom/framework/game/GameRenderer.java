package com.test.mrnom.framework.game;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;


@SuppressLint("ViewConstructor")
public class GameRenderer extends SurfaceView {

    private final Bitmap frameBuffer;

    private final Rect canvasBounds;

    public GameRenderer(Context context, Bitmap frameBuffer) {
        super(context);
        this.frameBuffer = frameBuffer;
        canvasBounds = new Rect();
    }

    public void sizeChanged(int width, int height) {
        canvasBounds.right = width;
        canvasBounds.bottom = height;
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(frameBuffer, null, canvasBounds, null);
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
