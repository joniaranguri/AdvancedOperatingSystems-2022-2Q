package com.example.sabi.view;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sabi.R;
import com.example.sabi.contract.SensorContract;
import com.example.sabi.presenter.SensorPresenter;

import java.text.DecimalFormat;

public class SensorActivity extends AppCompatActivity implements SensorContract.ISensorView, SensorEventListener {

    private SensorContract.ISensorPresenter presenter;
    private SensorManager sensorManager;

    private TextView accelerometer;

    DecimalFormat twoDecimals = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        accelerometer = findViewById(R.id.tv_accelerometer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        presenter = new SensorPresenter(this);
    }

    protected void init_sensors() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void end_sensors() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public boolean isAttached() {
        return this.hasWindowFocus();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt;

        synchronized (this)
        {
            Log.d("sensor", event.sensor.getName());

            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER :
                    Toast.makeText(this, "Sensor ACELEROMETRO", Toast.LENGTH_SHORT);
                    txt = "Acelerometro:\n";
                    txt += "x: " + twoDecimals.format(event.values[0]) + " m/seg2 \n";
                    txt += "y: " + twoDecimals.format(event.values[1]) + " m/seg2 \n";
                    txt += "z: " + twoDecimals.format(event.values[2]) + " m/seg2 \n";
                    accelerometer.setText(txt);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStop()
    {

        end_sensors();

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        end_sensors();

        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        end_sensors();

        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        init_sensors();

        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        init_sensors();
    }
}
