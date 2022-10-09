package com.example.sabi.presenter;

import com.example.sabi.contract.MainContract;
import com.example.sabi.view.BluetoothActivity;

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
        // ToDo: Abrir activity para ver valores de un sensor del teléfono.
    }
}
