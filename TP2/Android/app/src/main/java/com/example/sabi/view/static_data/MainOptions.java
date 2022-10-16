package com.example.sabi.view.static_data;

import com.example.sabi.R;

public class MainOptions {
    public static final int BLUETOOTH_OPTION_INDEX = 0;
    public static final int SENSORS_OPTION_INDEX = 1;


    public static final MainOption[] list = {
            new MainOption("Comandos bluetooth",
                    "Envia comandos bluetooth al dispositivo Arduino de SABI",
                    R.drawable.ic_bluetooth_round),

            new MainOption("Visualizar sensores",
                    "Interactua con los sensores del celular para realizar alguna accion en el Arduino SABI",
                    R.drawable.ic_arduino_round)};
}
