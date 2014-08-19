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

    private final int width;

    private final FpsInfo fpsInfo;

    private final Paint paint;

    private final Rect fpsBounds;

    public GameRenderer(Context context, Bitmap frameBuffer, int width, int height, boolean showFps) {
        super(context);
        this.frameBuffer = frameBuffer;
        this.width = width;
        if (showFps) {
            fpsInfo = new FpsInfo();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(height / 20);
            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.MONOSPACE);
            fpsBounds = new Rect();
        } else {
            fpsInfo = null;
            paint = null;
            fpsBounds = null;
        }
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(frameBuffer, 0, 0, null);
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
        int textX = this.width - width - fpsBounds.left;
        int textY = -fpsBounds.top;
        fpsBounds.top = 0;
        fpsBounds.bottom = height;
        fpsBounds.right = this.width;
        fpsBounds.left = fpsBounds.right - width;
        paint.setColor(Color.BLACK);
        canvas.drawRect(fpsBounds, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(fpsChars, 0, fpsChars.length, textX, textY, paint);
    }
}
