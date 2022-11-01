package com.example.sabi.contract;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public interface PairContract {

    interface IPairModel {
        /**
         * Gets the current Bluetooth state.
         *
         * @return true if Bluetooth is on, false otherwise.
         */
        boolean isBluetoothOn();

        /**
         * Returns the BluetoothAdapter.
         *
         * @return the BluetoothAdapter used by the model.
         */
        BluetoothAdapter getBluetoothAdapter();

        /**
         * Returns the IntentFilter to be used for initiating the BroadcastReceiver in the view.
         * @return the IntentFilter to be used for initiating the BroadcastReceiver in the view.
         */
        IntentFilter getIntentFilter();

        /**
         * Starts the discovery of Bluetooth devices.
         */
        void startDiscovery();

        /**
         * Cancels the discovery of Bluetooth devices.
         */
        void cancelDiscovery();

        /**
         * Gets pending permissions for Bluetooth usage.
         */
        List<String> getPermissionsNeeded(Context context);
    }

    interface IPairView extends BaseContract.IBaseView {
        /**
         * Navigates to the specified activity.
         *
         * @param activityClass the next class activity
         */
        void goToActivity(final Class<? extends AppCompatActivity> activityClass);

        /**
         * Updates views for Bluetooth being turned on.
         */
        void updateViewsForBluetoothOn();

        /**
         * Updates views for Bluetooth being turned off.
         */
        void updateViewsForBluetoothOff();

        /**
         * Updates views for Bluetooth not being supported.
         */
        void updateViewsForBluetoothUnsupported();

        /**
         * Launches an Intent to request device's Bluetooth permissions.
         * @param intent the Intent to get Bluetooth permissions with.
         */
        void startForResult(Intent intent);

        /**
         * Request permissions listed in ArrayList param.
         * @param listPermissionsNeeded the permissions to request.
         */
        void requestPermissions(List<String> listPermissionsNeeded);

        /**
         * Calls registerReceiver with received parameters.
         * @param receiver the BroadcastReceiver to use for the registerReceiver call.
         * @param filter the IntentFilter to use for the registerReceiver call.
         */
        void registerReceiverForPair(BroadcastReceiver receiver, IntentFilter filter);

        /**
         * Shows the progress dialog.
         */
        void showProgressDialog();

        /**
         * Dismisses the progress dialog.
         */
        void dismissProgressDialog();

        /**
         * Starts the DeviceListActivity with a list of found devices.
         * @param deviceList the list of devices to start the DeviceListActivity with.
         */
        void startDeviceListActivity(ArrayList<BluetoothDevice> deviceList);

        /**
         * Shows a Toast indicating a device has been found.
         */
        void showDeviceFoundToast(String deviceName);
    }

    interface IPairPresenter extends BaseContract.IBasePresenter {

        /**
         * Handles a click on the Bluetooth activation button.
         */
        void onActivateBtButtonClick();

        /**
         * Handles a click on the Search devices button.
         */
        void onSearchButtonClick();

        /**
         * Handles a change on Bluetooth state.
         *
         * @param isBluetoothOn indicates if Bluetooth is on or not.
         */
        void onBluetoothStateToggle(final boolean isBluetoothOn);

        /**
         * Checks if device has permissions to use Bluetooth.
         *
         * @return true if Bluetooth permissions are enabled, false otherwise.
         */
        boolean checkpermissions();

        /**
         * Tries to create and register a new BroadcastReceiver for Bluetooth events.
         */
        void tryRegisterReceiver();

        /**
         * Handles the progress dialog being cancelled.
         */
        void onProgressDialogCancelled();

        /**
         * Handles views onPause lifecycle event.
         */
        void onViewPaused();

        /**
         * Returns the BroadcastReceiver used for listening to BluetoothEvents.
         * @return
         */
        BroadcastReceiver getReceiver();
    }
}
