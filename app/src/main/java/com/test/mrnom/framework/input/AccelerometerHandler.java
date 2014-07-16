package com.test.mrnom.framework.input;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class AccelerometerHandler implements SensorEventListener {

    public volatile float accelerationX;

    public volatile float accelerationY;

    public volatile float accelerationZ;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerationX = event.values[0];
        accelerationY = event.values[1];
        accelerationZ = event.values[2];
    }
}
