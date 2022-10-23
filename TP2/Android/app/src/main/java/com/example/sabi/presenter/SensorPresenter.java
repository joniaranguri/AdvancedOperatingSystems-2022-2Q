package com.example.sabi.presenter;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.example.sabi.contract.SensorContract;
import com.example.sabi.model.SensorModel;

public class SensorPresenter implements SensorContract.ISensorPresenter {

    private final SensorContract.ISensorView view;
    private final SensorContract.ISensorModel model;
    private SensorManager sensorManager;

    public SensorPresenter(SensorContract.ISensorView view, SensorManager sensorManager) {
        this.view = view;
        model = new SensorModel(this);
        this.sensorManager = sensorManager;
    }

    @Override
    public boolean isViewAttached() {
        return view.isAttached();
    }

    @Override
    public void endSensors() {
        if (isViewAttached())
            sensorManager.unregisterListener(model, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void initSensors() {
        if (isViewAttached())
            sensorManager.registerListener(model, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void setAccelerometerResult(String txt) {
        view.setAccelerometerText(txt);
    }
}
