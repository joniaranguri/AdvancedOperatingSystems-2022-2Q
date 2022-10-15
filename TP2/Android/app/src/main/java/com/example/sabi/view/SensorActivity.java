package com.example.sabi.view;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sabi.R;
import com.example.sabi.contract.SensorContract;
import com.example.sabi.presenter.SensorPresenter;

public class SensorActivity extends AppCompatActivity implements SensorContract.ISensorView {

    private SensorContract.ISensorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        presenter = new SensorPresenter(this);
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
