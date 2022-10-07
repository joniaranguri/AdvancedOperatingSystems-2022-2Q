package com.example.sabi.presenter;

import com.example.sabi.contract.Contract;
import com.example.sabi.model.AppModel;

public class AppPresenter implements Contract.IAppPresenter {

    private final Contract.IAppView view;
    private final Contract.IAppModel model;

    public AppPresenter(Contract.IAppView view) {
        this.view = view;
        model = new AppModel(this);
    }

    @Override
    public void sendCommand(String command) {
        if (isViewAttached()) {
            model.sendCommand(command);
        }
    }

    @Override
    public boolean isViewAttached() {
        return view.isAttached();
    }
}
