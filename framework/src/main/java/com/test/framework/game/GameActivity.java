package com.test.framework.game;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.test.framework.gfx.Graphics;
import com.test.framework.input.Input;
import com.test.framework.io.FileIO;
import com.test.framework.sfx.Audio;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;


public abstract class GameActivity extends Activity implements Game {

    private static final String TAG = GameActivity.class.getName();

    private Point screenSize;

    private SurfaceView view;

    private Input input;

    private FileIO fileIO;

    private Graphics graphics;

    private Audio audio;

    private GameScreen screen;

    private GameLoopThread gameThread;

    private DebugRenderer debugRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        int virtualWidth = getVirtualWidth();
        int virtualHeight = getVirtualHeight();

        view = new SurfaceView(this);
        setContentView(view);
        view.getHolder().setFixedSize(virtualWidth, virtualHeight);
        view.getHolder().addCallback(new SurfaceHolder.Callback() {

            private boolean creation;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                creation = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                screen.resume();
                if (creation) {
                    gameThread = new GameLoopThread(GameActivity.this, holder);
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
        });

        input = new Input((float) virtualWidth / screenSize.x, (float) virtualHeight / screenSize.y);
        view.setOnKeyListener(input);
        view.setOnTouchListener(input);
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

        if (savedInstanceState != null) {
            screen = loadScreen(savedInstanceState);
        }
        if (screen == null) {
            screen = getFirstScreen(this);
        }

        if (debug()) {
            debugRenderer = new DebugRenderer(virtualWidth, virtualHeight);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveScreen(screen, outState);
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
    public void switchToScreen(GameScreen screen) {
        this.screen.pause();
        this.screen.dispose();

        this.screen = screen;
        this.screen.resume();
    }

    @Override
    public void update(float deltaTime) {
        screen.update(deltaTime);
    }

    @Override
    public void render(Canvas canvas, float deltaTime) {
        screen.render(canvas, deltaTime);
        if (debugRenderer != null) {
            debugRenderer.render(canvas);
        }
    }

    protected abstract GameScreen getFirstScreen(Game game);

    protected void saveScreen(GameScreen screen, Bundle state) {
        // nothing by default
    }

    // may return null (the default), in which case #getFirstScreen is called
    protected GameScreen loadScreen(Bundle state) {
        return null;
    }

    protected int getVirtualWidth() {
        return screenSize.x;
    }

    protected int getVirtualHeight() {
        return screenSize.y;
    }

    protected int getMaxSimultaneousSounds() {
        return 10;
    }

    protected boolean debug() {
        return true;
    }
}
