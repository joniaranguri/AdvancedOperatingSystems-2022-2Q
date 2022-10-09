package com.example.sabi.presenter;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sabi.contract.MainContract;
import com.example.sabi.view.BluetoothActivity;

public class MainPresenter implements MainContract.IMainPresenter {

    private MainContract.IMainView view;

    public MainPresenter(MainContract.IMainView view){
        this.view = view;
    }

    @Override
    public void goToActivity(Class<? extends AppCompatActivity> activityClass) {
        final Context currentContext = view.getViewContext();
        Intent intent = new Intent(currentContext, activityClass);
        currentContext.startActivity(intent);
    }

    @Override
    public void onBluetoothButtonClick() {
        goToActivity(BluetoothActivity.class);
    }

    @Override
    public void onSensorActivityClick() {
        // ToDo: Abrir activity para ver valores de un sensor del teléfono.
    }
}
