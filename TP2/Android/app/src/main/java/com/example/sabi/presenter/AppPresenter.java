package com.example.sabi.presenter;

import android.view.View;
import android.widget.Toast;
import com.example.sabi.model.AppModel;
import com.example.sabi.model.IAppModel;

public class AppPresenter implements IAppPresenter {

    private View view;
    private IAppModel model;

    public AppPresenter(View view) {
        this.view = view;
        model = new AppModel(this);
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void sendCommand(String command) {
        if(isViewAttached()) {
            model.sendCommand(command);
        }
    }

    @Override
    public void showCommand(String command) {
        Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
    }
}
