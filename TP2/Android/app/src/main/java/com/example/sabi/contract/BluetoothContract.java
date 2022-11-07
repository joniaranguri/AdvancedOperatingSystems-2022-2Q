package com.example.sabi.contract;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public interface BluetoothContract {

    interface IBluetoothModel {
        /**
         * Sends the current command to the Arduino device
         *
         * @param command the command to send
         */
        void sendCommand(String command);

        void initBluetoothService(BluetoothDevice device);
    }

    interface IBluetoothView extends BaseContract.IBaseView {
        /**
         * Gets View context.
         *
         * @return view context.
         */
        Context getViewContext();

        /**
         * Checks if view is currently attached
         *
         * @return true if view is attached, false otherwise.
         */
        boolean isAttached();

        /**
         * Show the message obtained from the Arduino device
         *
         * @param message the message to be shown
         */
        void showMessage(String message);
    }

    interface IBluetoothPresenter extends BaseContract.IBasePresenter {

        /**
         * Checks if view is currently attached
         *
         * @return true if view is attached, false otherwise.
         */
        boolean isViewAttached();

        /**
         * Sends the current command to the Arduino device
         *
         * @param command the command to send
         */
        void sendCommand(String command);

        /**
         * Show the message obtained from the Arduino device
         *
         * @param message the message to be shown
         */
        void showMessage(String message);

        void initBluetoothService(BluetoothDevice device);

        /**
         * Gets View context.
         *
         * @return view context.
         */
        Context getViewContext();
    }

}
