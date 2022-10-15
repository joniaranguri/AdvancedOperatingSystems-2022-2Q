package com.example.sabi.presenter;

import com.example.sabi.contract.MainContract;
import com.example.sabi.view.BluetoothActivity;
import com.example.sabi.view.SensorActivity;

public class MainPresenter implements MainContract.IMainPresenter {

    private MainContract.IMainView view;

    public MainPresenter(MainContract.IMainView view){
        this.view = view;
    }

    @Override
    public void onBluetoothButtonClick() {
        view.goToActivity(BluetoothActivity.class);
    }

    @Override
    public void onSensorActivityClick() {
        view.goToActivity(SensorActivity.class);
    }
}
