package com.example.sabi.model;

import com.example.sabi.presenter.IAppPresenter;

public class AppModel implements IAppModel{

    private IAppPresenter presenter;
    private String command;

    public AppModel(IAppPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendCommand(String command) {
        presenter.sendCommand(command);
    }
}
