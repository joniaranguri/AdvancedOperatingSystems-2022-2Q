package com.example.sabi.view;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sabi.R;
import com.example.sabi.contract.SensorContract;
import com.example.sabi.presenter.SensorPresenter;

public class SensorActivity extends AppCompatActivity implements SensorContract.ISensorView {

    private SensorContract.ISensorPresenter presenter;


    private TextView accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        accelerometer = findViewById(R.id.tv_accelerometer);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        presenter = new SensorPresenter(this, sensorManager);
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
    public void setAccelerometerText(String txt) {
        accelerometer.setText(txt);
    }

    @Override
    protected void onPause() {
        presenter.endSensors();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presenter.initSensors();
        super.onResume();
    }


}
