package com.example.sabi.presenter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.sabi.contract.PairContract;
import com.example.sabi.model.PairModel;

import java.util.ArrayList;
import java.util.List;

public class PairPresenter implements PairContract.IPairPresenter {

    public static final int MULTIPLE_PERMISSIONS = 10;

    private final String[] permissions= new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final PairContract.IPairView view;
    private final PairContract.IPairModel model;

    public PairPresenter(PairContract.IPairView view) {
        this.view = view;
        this.model = new PairModel();
        if (model.isBluetoothOn()) {
            view.updateViewsForBluetoothOn();
        }
    }

    @Override
    public void onActivateBtButtonClick() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        view.startForResult(intent);
    }

    @Override
    public void onSearchButtonClick() {

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
    public boolean checkpermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(view.getViewContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            view.requestPermissions(listPermissionsNeeded);
            return false;
        }
        return true;
    }
}
