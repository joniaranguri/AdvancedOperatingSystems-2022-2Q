package com.example.sabi.contract;

import android.content.Context;

public interface SensorContract {

    interface ISensorModel {
//        void sendCommand(String command);
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
    }

    interface ISensorPresenter extends BaseContract.IBasePresenter {
        /**
         * Checks if view is currently attached
         *
         * @return true if view is attached, false otherwise.
         */
        boolean isViewAttached();
    }

}
