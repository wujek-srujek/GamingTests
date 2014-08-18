package com.test.framework.game;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceView;


@SuppressLint("ViewConstructor")
public class GameRenderer extends SurfaceView {

    private final Bitmap frameBuffer;

    private final Rect canvasBounds;

    private final FpsInfo fpsInfo;

    private final Paint paint;

    private final Rect fpsBounds;

    public GameRenderer(Context context, Bitmap frameBuffer, boolean showFps) {
        super(context);
        this.frameBuffer = frameBuffer;
        canvasBounds = new Rect();
        if (showFps) {
            fpsInfo = new FpsInfo();
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.MONOSPACE);
            fpsBounds = new Rect();
        } else {
            fpsInfo = null;
            paint = null;
            fpsBounds = null;
        }
    }

    public void sizeChanged(int width, int height) {
        canvasBounds.right = width;
        canvasBounds.bottom = height;
        if (fpsInfo != null) {
            paint.setTextSize(height / 20);
            fpsInfo.reset();
        }
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(frameBuffer, null, canvasBounds, null);
                if (fpsInfo != null) {
                    fpsInfo.update();
                    showFps(canvas, fpsInfo.getFpsChars());
                }
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void showFps(Canvas canvas, char[] fpsChars) {
        paint.getTextBounds(fpsChars, 0, fpsChars.length, fpsBounds);
        int width = fpsBounds.width();
        int height = fpsBounds.height();
        int textX = canvasBounds.right - width - fpsBounds.left;
        int textY = -fpsBounds.top;
        fpsBounds.top = 0;
        fpsBounds.bottom = height;
        fpsBounds.right = canvasBounds.right;
        fpsBounds.left = fpsBounds.right - width;
        paint.setColor(Color.BLACK);
        canvas.drawRect(fpsBounds, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(fpsChars, 0, fpsChars.length, textX, textY, paint);
    }
}
