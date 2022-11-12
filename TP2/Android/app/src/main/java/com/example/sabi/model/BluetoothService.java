package com.example.sabi.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.example.sabi.R;
import com.example.sabi.contract.ArduinoBTListener;
import com.example.sabi.model.threads.AcceptThread;
import com.example.sabi.model.threads.ConnectThread;
import com.example.sabi.model.threads.ConnectedThread;

import java.util.UUID;

public class BluetoothService {
    public final String TAG = this.getClass().toString();
    public static String appName;
    public static final UUID UUID_SABI = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    public final BluetoothAdapter mBluetoothAdapter;
    public final ArduinoBTListener readBTListener;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    public BluetoothDevice mmDevice;
    public UUID deviceUUID;

    public BluetoothService(Context context, ArduinoBTListener readBTListener) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.readBTListener = readBTListener;
        appName = context.getString(R.string.app_name);
        start();
    }

    /**
     * Start the service. Specifically start AcceptThread to begin a
     * session in listening (server) mode.
     */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(this);
            mInsecureAcceptThread.start();
        }
    }

    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device, final UUID clientUUID) {
        mConnectThread = new ConnectThread(this, device, clientUUID);
        mConnectThread.start();
    }

    public void connected(BluetoothSocket mmSocket) {
        mConnectedThread = new ConnectedThread(this, mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        mConnectedThread.write(out);
    }
}
