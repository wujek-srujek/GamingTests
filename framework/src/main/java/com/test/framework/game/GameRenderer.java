package com.test.framework.game;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;


public class GameRenderer {

    public interface FrameRenderer {

        void renderFrame(Canvas canvas, float deltaTime);
    }

    private final FrameRenderer frameRenderer;

    private final int width;

    private final FpsInfo fpsInfo;

    private final Paint paint;

    private final Rect fpsBounds;

    public GameRenderer(FrameRenderer frameRenderer, int width, int height, boolean showFps) {
        this.frameRenderer = frameRenderer;
        this.width = width;
        if (showFps) {
            fpsInfo = new FpsInfo();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(height / 50);
            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.MONOSPACE);
            fpsBounds = new Rect();
        } else {
            fpsInfo = null;
            paint = null;
            fpsBounds = null;
        }
    }

    public void render(Canvas canvas, float deltaTime) {
        frameRenderer.renderFrame(canvas, deltaTime);
        if (fpsInfo != null) {
            fpsInfo.update();
            showFps(canvas, fpsInfo.getFpsChars());
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
