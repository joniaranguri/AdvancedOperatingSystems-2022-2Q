package com.example.sabi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sabi.R;
import com.example.sabi.contract.MainContract;
import com.example.sabi.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView {

    private Button bluetoothButton;
    private Button sensorButton;

    private MainContract.IMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
        bluetoothButton = findViewById(R.id.main_activity_bluetooth_button);
        sensorButton = findViewById(R.id.main_activity_sensor_button);

        bluetoothButton.setOnClickListener(view -> presenter.onBluetoothButtonClick());
        sensorButton.setOnClickListener(view -> presenter.onSensorActivityClick());
    }

    @Override
    public void goToActivity(Class<? extends AppCompatActivity> activityClass) {
        final Context currentContext = getViewContext();
        Intent intent = new Intent(currentContext, activityClass);
        currentContext.startActivity(intent);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public boolean isAttached() {
        return this.hasWindowFocus();
    }
}