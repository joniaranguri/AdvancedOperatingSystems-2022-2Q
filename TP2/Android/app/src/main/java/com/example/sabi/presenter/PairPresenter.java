package com.example.sabi.presenter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.sabi.contract.PairContract;
import com.example.sabi.model.PairModel;

import java.util.ArrayList;
import java.util.List;

public class PairPresenter implements PairContract.IPairPresenter {

    public static final int MULTIPLE_PERMISSIONS = 10;

    private final PairContract.IPairView view;
    private final PairContract.IPairModel model;

    private final ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

    public PairPresenter(PairContract.IPairView view) {
        this.view = view;
        this.model = new PairModel();
        if (model.getBluetoothAdapter() != null) {
            if (model.isBluetoothOn()) {
                view.updateViewsForBluetoothOn();
            }
        } else {
            view.updateViewsForBluetoothUnsupported();
        }
    }

    @Override
    public void onActivateBtButtonClick() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        view.startForResult(intent);
    }

    @Override
    public void onSearchButtonClick() {
        model.startDiscovery();
    }

    @Override
    public void onBluetoothStateToggle(boolean isBluetoothOn) {
        if (isBluetoothOn) {
            view.updateViewsForBluetoothOn();
        } else {
            view.updateViewsForBluetoothOff();
        }
    }

    @Override
    public void onProgressDialogCancelled() {
        view.dismissProgressDialog();
        model.cancelDiscovery();
    }

    @Override
    public boolean checkpermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        List<String> listPermissionsNeeded = model.getPermissionsNeeded(view.getViewContext());

        if (!listPermissionsNeeded.isEmpty()) {
            view.requestPermissions(listPermissionsNeeded);
            return false;
        }
        return true;
    }

    @Override
    public void tryRegisterReceiver() {
        if (checkpermissions()) {
            view.registerReceiverForPair(receiver, model.getIntentFilter());
        }
    }

    @Override
    public void onViewPaused() {
        if (model.getBluetoothAdapter() != null) {
            model.cancelDiscovery();
        }
    }

    @Override
    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        private boolean isDiscoveryStarted = false;

        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                onBluetoothStateToggle(state == BluetoothAdapter.STATE_ON);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                view.showProgressDialog();
                isDiscoveryStarted = true;
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && isDiscoveryStarted) {
                view.dismissProgressDialog();
                view.startDeviceListActivity(deviceList);
                deviceList.clear();
                isDiscoveryStarted = false;
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                view.showDeviceFoundToast(device.getName());
            }
        }
    };
}
