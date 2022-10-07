package com.example.sabi.model;

import com.example.sabi.contract.Contract;

public class AppModel implements Contract.IAppModel {

    private Contract.IAppPresenter presenter;

    public AppModel(Contract.IAppPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendCommand(String command) {
        // Send command to device, no call the presenter
    }
}
