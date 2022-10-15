package com.example.sabi.model;

import com.example.sabi.contract.SensorContract;

public class SensorModel implements SensorContract.ISensorModel {

    private SensorContract.ISensorPresenter presenter;

    public SensorModel(SensorContract.ISensorPresenter presenter) {
        this.presenter = presenter;
    }

}
