package com.test.framework.gfx;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;


public class Graphics {

    private final AssetManager assets;

    private final int width;

    private final int height;

    public Graphics(AssetManager assets, int width, int height) {
        this.assets = assets;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap newBitmap(String assetName, Bitmap.Config preferredConfig) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = preferredConfig;

        InputStream in = null;
        try {
            in = assets.open(assetName);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) {
                throw new RuntimeException("couldn't load bitmap from asset " + assetName);
            }
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException("couldn't load bitmap from asset " + assetName, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // swallow
                }
            }
        }
    }
}
