package com.example.sabi.presenter;

import com.example.sabi.contract.SensorContract;
import com.example.sabi.model.SensorModel;

public class SensorPresenter implements SensorContract.ISensorPresenter {

    private final SensorContract.ISensorView view;
    private final SensorContract.ISensorModel model;

    public SensorPresenter(SensorContract.ISensorView view) {
        this.view = view;
        model = new SensorModel(this);
    }

    @Override
    public boolean isViewAttached() {
        return view.isAttached();
    }
}
