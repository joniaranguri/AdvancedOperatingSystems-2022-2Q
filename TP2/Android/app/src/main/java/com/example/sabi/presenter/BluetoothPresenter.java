package com.example.sabi.presenter;

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
    public boolean isViewAttached() {
        return view.isAttached();
    }
}
