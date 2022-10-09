package com.example.sabi.model;

import com.example.sabi.contract.BluetoothContract;

public class BluetoothModel implements BluetoothContract.IBluetoothModel {

    private BluetoothContract.IBluetoothPresenter presenter;

    public BluetoothModel(BluetoothContract.IBluetoothPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendCommand(String command) {
        // Send command to device, no call the presenter
    }
}
