package com.example.sabi.model.threads;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.sabi.model.BluetoothService;

import java.io.IOException;

/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class AcceptThread extends Thread {
    private final BluetoothService bluetoothService;
    // The local server socket
    private final BluetoothServerSocket mmServerSocket;

    @SuppressLint("MissingPermission")
    public AcceptThread(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
        BluetoothServerSocket tmp = null;

        // Create a new listening server socket
        try {
            tmp = bluetoothService.mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(BluetoothService.appName, BluetoothService.UUID_SABI);
        } catch (IOException e) {
            Log.e(bluetoothService.TAG, "AcceptThread: IOException: " + e.getMessage());
        }

        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;

        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            socket = mmServerSocket.accept();

        } catch (IOException e) {
            Log.e(bluetoothService.TAG, "AcceptThread: IOException: " + e.getMessage());
        }
        if (socket != null) {
            bluetoothService.connected(socket);
        }

    }

}
