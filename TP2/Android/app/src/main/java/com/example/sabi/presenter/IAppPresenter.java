package com.example.sabi.presenter;

public interface IAppPresenter {
    boolean isViewAttached();
    void sendCommand(String command);
    void showCommand(String command);
}
