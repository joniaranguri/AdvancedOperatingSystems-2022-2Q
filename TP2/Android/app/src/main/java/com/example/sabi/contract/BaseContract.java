package com.example.sabi.contract;

import android.content.Context;

public interface BaseContract {

    interface IBaseView {
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

    // ToDo: Ver si esto va a servir para comportamientos repetidos entre presenters; si no, borrar
    interface IBasePresenter {

    }
}
