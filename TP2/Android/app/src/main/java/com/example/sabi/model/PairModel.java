package com.example.sabi.model;

import android.bluetooth.BluetoothAdapter;

import com.example.sabi.contract.PairContract;

public class PairModel implements PairContract.IPairModel {

    private final BluetoothAdapter bluetoothAdapter;

    public PairModel() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean isBluetoothOn() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }
}
