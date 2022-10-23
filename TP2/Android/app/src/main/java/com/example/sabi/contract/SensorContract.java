package com.example.sabi.contract;

import android.content.Context;
import android.hardware.SensorEventListener;

public interface SensorContract {

    interface ISensorModel extends SensorEventListener {

    }

    interface ISensorView extends BaseContract.IBaseView {
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

        void setAccelerometerText(String txt);
    }

    interface ISensorPresenter extends BaseContract.IBasePresenter {
        /**
         * Checks if view is currently attached
         *
         * @return true if view is attached, false otherwise.
         */
        boolean isViewAttached();

        void endSensors();

        void initSensors();

        void setAccelerometerResult(String txt);
    }

}
