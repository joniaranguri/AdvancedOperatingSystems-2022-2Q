package com.example.sabi.contract;

import android.content.Context;

public interface BluetoothContract {

    interface IBluetoothModel {
        /**
         * Sends the current command to the Arduino device
         *
         * @param command the command to send
         */
        void sendCommand(String command);
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
         * Shows the current command in the UI
         *
         * @param command the command to be shown
         */
        void showCommand(String command);
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
    }

}
