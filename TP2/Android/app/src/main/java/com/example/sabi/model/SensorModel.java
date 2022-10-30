package com.example.sabi.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.example.sabi.contract.SensorContract;

import java.text.DecimalFormat;

public class SensorModel implements SensorContract.ISensorModel {

    private final SensorContract.ISensorPresenter presenter;

    DecimalFormat twoDecimals = new DecimalFormat("###.###");

    public SensorModel(SensorContract.ISensorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt;

        synchronized (this) {
            Log.d("sensor", event.sensor.getName());

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    txt = "x: " + twoDecimals.format(event.values[0]) + " m/seg2 \n";
                    txt += "y: " + twoDecimals.format(event.values[1]) + " m/seg2 \n";
                    txt += "z: " + twoDecimals.format(event.values[2]) + " m/seg2 \n";
                    presenter.setAccelerometerResult(txt);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing
    }

}
