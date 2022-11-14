package com.example.sabi.model.threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.sabi.model.BluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
 * receiving incoming data through input/output streams respectively.
 **/
public class ConnectedThread extends Thread {
    private final BluetoothService bluetoothService;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothService bluetoothService, BluetoothSocket socket) {
        this.bluetoothService = bluetoothService;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        String incomingMessage;
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            // Read from the InputStream
            try {
                bytes = mmInStream.read(buffer);
                incomingMessage = new String(buffer, 0, bytes);
                bluetoothService.readBTListener.onArduinoMessage(incomingMessage);
            } catch (IOException e) {
                Log.e(bluetoothService.TAG, "write: Error reading Input Stream. " + e.getMessage());
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(bluetoothService.TAG, "write: Error writing to output stream. " + e.getMessage());
        }
    }

}
