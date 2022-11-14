package com.example.sabi.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.sabi.contract.PairContract;

import java.util.ArrayList;
import java.util.List;

public class PairModel implements PairContract.IPairModel {

    private final String[] permissions= new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.S)
    private final String[] android12Permissions = new String[]{
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,};

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

    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        return filter;
    }

    public List<String> getPermissionsNeeded(Context context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            for (String p : android12Permissions) {
                result = ContextCompat.checkSelfPermission(context, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }
        return listPermissionsNeeded;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void startDiscovery() {
        bluetoothAdapter.startDiscovery();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void cancelDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }
}
