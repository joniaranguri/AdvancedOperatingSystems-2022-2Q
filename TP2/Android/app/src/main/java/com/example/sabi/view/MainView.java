package com.example.sabi.view;

import com.example.sabi.contract.Contract;

public interface MainView extends Contract.IAppView{

    /**
     * Shows the current command in the UI
     *
     * @param command the command to be shown
     */
    void showCommand(String command);
}
