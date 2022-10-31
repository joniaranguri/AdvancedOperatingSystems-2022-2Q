package com.example.sabi.contract;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

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
         * Launches an Intent to request device's Bluetooth permissions.
         * @param intent the Intent to get Bluetooth permissions with.
         */
        void startForResult(Intent intent);

        /**
         * Request permissions listed in ArrayList param.
         * @param listPermissionsNeeded the permissions to request.
         */
        void requestPermissions(List<String> listPermissionsNeeded);
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
    }
}
