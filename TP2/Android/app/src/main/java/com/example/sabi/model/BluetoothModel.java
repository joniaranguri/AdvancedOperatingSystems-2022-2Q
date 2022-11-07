package com.example.sabi.model;

import static com.example.sabi.view.BluetoothActivity.UUID_SABI;

import android.bluetooth.BluetoothDevice;

import com.example.sabi.contract.BluetoothContract;

import java.nio.charset.Charset;

public class BluetoothModel implements BluetoothContract.IBluetoothModel {

    private final BluetoothContract.IBluetoothPresenter presenter;
    BluetoothService bluetoothService;

    public BluetoothModel(BluetoothContract.IBluetoothPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendCommand(String command) {
        byte[] bytes = command.getBytes(Charset.defaultCharset());
        this.bluetoothService.write(bytes);
    }

    @Override
    public void initBluetoothService(BluetoothDevice device) {
        this.bluetoothService = new BluetoothService(presenter.getViewContext(), presenter::showMessage);
        this.bluetoothService.startClient(device, UUID_SABI);
    }
}
