package com.test.mrnom.framework.game;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

import com.test.mrnom.framework.gfx.Graphics;
import com.test.mrnom.framework.input.Input;
import com.test.mrnom.framework.io.FileIO;
import com.test.mrnom.framework.sfx.Audio;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;


public abstract class GameActivity extends Activity implements Game {

    private static final String TAG = GameActivity.class.getName();

    private GameRenderer gameRenderer;

    private Input input;

    private FileIO fileIO;

    private Graphics graphics;

    private Audio audio;

    private GameScreen screen;

    private GameLoopThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final int virtualWidth = getVirtualWidth();
        final int virtualHeight = getVirtualHeight();
        Bitmap frameBuffer = Bitmap.createBitmap(virtualWidth, virtualHeight, getBitmapConfig());

        gameRenderer = new GameRenderer(this, frameBuffer);
        gameRenderer.getHolder().addCallback(new SurfaceHolder.Callback2() {

            private boolean creation;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                creation = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                gameRenderer.sizeChanged(width, height);
                input.setScale((float) virtualWidth / width, (float) virtualHeight / height);
                screen.resume();
                if (creation) {
                    gameThread = new GameLoopThread(GameActivity.this);
                    gameThread.start();
                    creation = false;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameThread.finish();
                while (true) {
                    try {
                        gameThread.join();
                        break;
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
                screen.pause();

                if (isFinishing()) {
                    screen.dispose();
                }
            }

            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {
                gameRenderer.render();
            }
        });

        input = new Input();
        gameRenderer.setOnKeyListener(input);
        gameRenderer.setOnTouchListener(input);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> accelerometers = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!accelerometers.isEmpty()) {
            Sensor accelerometer = accelerometers.get(0);
            manager.registerListener(input, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.w(TAG, "no accelerometer, values will always be 0");
        }

        fileIO = new FileIO(getAssets(), new Callable<File>() {

            @Override
            public File call() {
                return getExternalFilesDir(null);
            }
        });

        graphics = new Graphics(getAssets(), virtualWidth, virtualHeight);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audio = new Audio(getAssets(), new SoundPool(getMaxSimultaneousSounds(), AudioManager.STREAM_MUSIC, 0));

        setContentView(gameRenderer);

        screen = getFirstScreen(this, new Canvas(frameBuffer));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void update(float deltaTime) {
        screen.update(deltaTime);
    }

    @Override
    public void render(float deltaTime) {
        screen.render(deltaTime);
        gameRenderer.render();
    }

    @Override
    public void switchToScreen(GameScreen screen) {
        this.screen.pause();
        this.screen.dispose();

        this.screen = screen;
        this.screen.resume();
    }

    protected abstract GameScreen getFirstScreen(Game game, Canvas canvas);

    protected Bitmap.Config getBitmapConfig() {
        return Bitmap.Config.RGB_565;
    }

    protected int getVirtualWidth() {
        return 320;
    }

    protected int getVirtualHeight() {
        return 480;
    }

    protected int getMaxSimultaneousSounds() {
        return 10;
    }
}
