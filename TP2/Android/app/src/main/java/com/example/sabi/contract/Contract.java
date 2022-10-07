package com.example.sabi.contract;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public interface Contract {

    interface IAppModel {
        /**
         * Sends the current command to the Arduino device
         *
         * @param command the command to send
         */
        void sendCommand(String command);
    }

    interface IAppView {
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
         * Navigates to the specified activity.
         *
         * @param activityClass the next class activity
         */
        default void goToActivity(final Class<AppCompatActivity> activityClass) {
            final Context currentContext = getViewContext();
            Intent intent = new Intent(currentContext, activityClass);
            currentContext.startActivity(intent);
        }
    }

    interface IAppPresenter {
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
