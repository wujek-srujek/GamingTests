package com.test.framework.game;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;


public class DebugRenderer {

    private final int width;

    private final FpsInfo fpsInfo;

    private final Paint paint;

    private final Rect fpsBounds;

    public DebugRenderer(int width, int height) {
        this.width = width;
        fpsInfo = new FpsInfo();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize((float) height / 50);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.MONOSPACE);
        fpsBounds = new Rect();
    }

    public void render(Canvas canvas) {
        fpsInfo.update();
        char[] fpsChars = fpsInfo.getFpsChars();
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
