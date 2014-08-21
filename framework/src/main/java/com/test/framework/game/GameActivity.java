package com.test.framework.game;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.test.framework.gfx.Graphics;
import com.test.framework.input.Input;
import com.test.framework.io.FileIO;
import com.test.framework.sfx.Audio;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;


public abstract class GameActivity<R, V extends View> extends Activity implements Game<R> {

    private static final String TAG = GameActivity.class.getName();

    private Point screenSize;

    protected V mainView;

    private DebugInfo<R> debugInfo;

    private Input input;

    private FileIO fileIO;

    private Graphics graphics;

    private Audio audio;

    protected GameScreen<R> screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        mainView = prepare();
        setContentView(mainView);
        if (debug()) {
            debugInfo = getDebugInfo();
        }

        input = new Input((float) getVirtualWidth() / screenSize.x, (float) getVirtualHeight() / screenSize.y);
        mainView.setOnKeyListener(input);
        mainView.setOnTouchListener(input);
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

        graphics = new Graphics(getAssets(), getVirtualWidth(), getVirtualHeight());

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audio = new Audio(getAssets(), new SoundPool(getMaxSimultaneousSounds(), AudioManager.STREAM_MUSIC, 0));

        if (savedInstanceState != null) {
            screen = loadScreen(savedInstanceState);
        }
        if (screen == null) {
            screen = getFirstScreen(this);
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
    public void switchToScreen(GameScreen<R> screen) {
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
    public void render(R renderer, float deltaTime) {
        screen.render(renderer, deltaTime);
        if (debug()) {
            debugInfo.render(renderer);
        }
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

    protected abstract V prepare();

    protected abstract DebugInfo<R> getDebugInfo();

    protected abstract GameScreen<R> getFirstScreen(Game<R> game);

    protected void saveScreen(GameScreen<R> screen, Bundle state) {
        // nothing by default
    }

    // may return null (the default), in which case #getFirstScreen is called
    protected GameScreen<R> loadScreen(Bundle state) {
        return null;
    }
}
