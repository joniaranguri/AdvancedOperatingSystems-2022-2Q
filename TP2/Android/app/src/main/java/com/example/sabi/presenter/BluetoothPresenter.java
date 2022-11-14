package com.example.sabi.presenter;

import static com.example.sabi.commons.Constants.WATER_LEVEL_2;
import static com.example.sabi.commons.Constants.WATER_LEVEL_3;
import static com.example.sabi.commons.Constants.WATER_LEVEL_4;
import static com.example.sabi.commons.Constants.WATER_LEVEL_5;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.sabi.R;
import com.example.sabi.contract.BluetoothContract;
import com.example.sabi.model.BluetoothModel;

public class BluetoothPresenter implements BluetoothContract.IBluetoothPresenter {

    private final BluetoothContract.IBluetoothView view;
    private final BluetoothContract.IBluetoothModel model;

    public BluetoothPresenter(BluetoothContract.IBluetoothView view) {
        this.view = view;
        model = new BluetoothModel(this);
    }

    @Override
    public void sendCommand(String command) {
        if (isViewAttached()) {
            model.sendCommand(command);
        }
    }

    @Override
    public void showMessage(String arduinoMessageRaw) {
        String finalMessage;
        switch (arduinoMessageRaw) {
            case WATER_LEVEL_2:
                finalMessage = getViewContext().getString(R.string.water_level_2);
                break;
            case WATER_LEVEL_3:
                finalMessage = getViewContext().getString(R.string.water_level_3);
                break;
            case WATER_LEVEL_4:
                finalMessage = getViewContext().getString(R.string.water_level_4);
                break;
            case WATER_LEVEL_5:
                finalMessage = getViewContext().getString(R.string.water_level_5);
                break;
            default:
                finalMessage = getViewContext().getString(R.string.water_level_1);
        }
        view.showMessage(finalMessage);
    }

    @Override
    public void initBluetoothService(BluetoothDevice device) {
        model.initBluetoothService(device);
    }

    @Override
    public Context getViewContext() {
        return view.getViewContext();
    }

    @Override
    public void unpairDevice() {
        this.model.unpairDevice();
    }

    @Override
    public boolean isViewAttached() {
        return view.isAttached();
    }
}
