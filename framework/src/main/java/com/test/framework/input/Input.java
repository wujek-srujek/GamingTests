package com.test.framework.input;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;


public class Input implements View.OnKeyListener, View.OnTouchListener, SensorEventListener {

    private static final int MAX_TOUCHES = 10;

    private final KeyHandler keyHandler;

    private volatile TouchHandler touchHandler;

    private final AccelerometerHandler accelerometerHandler;

    public Input() {
        keyHandler = new KeyHandler();
        touchHandler = new TouchHandler(MAX_TOUCHES, 1.F, 1.F);
        accelerometerHandler = new AccelerometerHandler();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return keyHandler.onKey(v, keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return touchHandler.onTouch(v, event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerometerHandler.onSensorChanged(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        accelerometerHandler.onAccuracyChanged(sensor, accuracy);
    }

    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }

    public void clearKeyBuffer() {
        keyHandler.getKeyEvents();
    }

    public int getMaxTouches() {
        return MAX_TOUCHES;
    }

    public boolean isTouchedDown(int pointer) {
        return touchHandler.isTouchedDown(pointer);
    }

    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    public void clearTouchBuffer() {
        touchHandler.getTouchEvents();
    }

    public float getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public float getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public float getAccelerationX() {
        return accelerometerHandler.accelerationX;
    }

    public float getAccelerationY() {
        return accelerometerHandler.accelerationY;
    }

    public float getAccelerationZ() {
        return accelerometerHandler.accelerationZ;
    }

    public void setScale(float scaleX, float scaleY) {
        // just replace the handler, all of its info is invalid anyways
        touchHandler = new TouchHandler(MAX_TOUCHES, scaleX, scaleY);
    }
}
