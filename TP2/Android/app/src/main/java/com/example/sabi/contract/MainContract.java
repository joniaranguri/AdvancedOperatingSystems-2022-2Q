package com.example.sabi.contract;

import androidx.appcompat.app.AppCompatActivity;

public interface MainContract {

    interface IMainView extends BaseContract.IBaseView {
        /**
         * Navigates to the specified activity.
         *
         * @param activityClass the next class activity
         */
        void goToActivity(final Class<? extends AppCompatActivity> activityClass);
    }

    interface IMainPresenter extends BaseContract.IBasePresenter {
        /**
         * Handles a click on the button to open the [BluetoothActivity].
         */
        void onBluetoothButtonClick();

        /**
         * Handles a click on the button to open the [SensorActivity].
         */
        void onSensorActivityClick();
    }
}
