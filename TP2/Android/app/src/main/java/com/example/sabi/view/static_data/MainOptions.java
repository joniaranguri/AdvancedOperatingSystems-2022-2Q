package com.example.sabi.view.static_data;

import com.example.sabi.R;

public class MainOptions {
    public static final int BLUETOOTH_OPTION_INDEX = 0;
    public static final int SENSORS_OPTION_INDEX = 1;


    public static final MainOption[] list = {
            new MainOption(R.string.option_bluetooth_title,
                    R.string.option_bluetooth_subtitle,
                    R.drawable.ic_bluetooth_round),

            new MainOption(R.string.option_sensors_title,
                    R.string.option_sensors_subtitle,
                    R.drawable.ic_arduino_round)};
}
