package com.example.sabi.model.threads;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.sabi.model.BluetoothService;

import java.io.IOException;
import java.util.UUID;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 */
public class ConnectThread extends Thread {
    private final BluetoothService bluetoothService;
    private BluetoothSocket mmSocket;

    public ConnectThread(BluetoothService bluetoothService, BluetoothDevice device, UUID uuid) {
        this.bluetoothService = bluetoothService;
        bluetoothService.mmDevice = device;
        bluetoothService.deviceUUID = uuid;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        BluetoothSocket tmp = null;
        // Get a BluetoothSocket for a connection with the given BluetoothDevice
        try {
            tmp = bluetoothService.mmDevice.createRfcommSocketToServiceRecord(bluetoothService.deviceUUID);
        } catch (IOException e) {
            Log.e(bluetoothService.TAG, "ConnectThread: Could not create RfcommSocket " + e.getMessage());
        }

        mmSocket = tmp;

        // Always cancel discovery because it will slow down a connection
        bluetoothService.mBluetoothAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mmSocket.close();
                Log.d(bluetoothService.TAG, "run: Closed Socket.");
            } catch (IOException e1) {
                Log.e(bluetoothService.TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
            }
            Log.d(bluetoothService.TAG, "run: ConnectThread: Could not connect to UUID: " + BluetoothService.UUID_SABI);
        }

        bluetoothService.connected(mmSocket);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(bluetoothService.TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
        }
    }
}
