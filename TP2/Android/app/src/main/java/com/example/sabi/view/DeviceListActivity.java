package com.example.sabi.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sabi.BuildConfig;
import com.example.sabi.R;
import com.example.sabi.view.adapters.DeviceListAdapter;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*********************************************************************************************************
 * Activity que muestra el listado de los dispositivos bluethoot encontrados
 **********************************************************************************************************/

public class DeviceListActivity extends Activity {
    public static final String BLUETOOTH_DEVICE_KEY = "bluetooth_device_key";
    private ListView mListView;
    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private int posicionListBluethoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paired_devices);

        //defino los componentes de layout
        mListView = (ListView) findViewById(R.id.lv_paired);

        //obtengo por medio de un Bundle del intent la lista de dispositivos encontrados
        mDeviceList = getIntent().getExtras().getParcelableArrayList("device.list");

        //defino un adaptador para el ListView donde se van mostrar en la activity los dispositovs encontrados
        mAdapter = new DeviceListAdapter(this);

        //asocio el listado de los dispositovos pasado en el bundle al adaptador del Listview
        mAdapter.setData(mDeviceList);

        //defino un listener en el boton emparejar del listview
        mAdapter.setListener(listenerBotonEmparejar);
        mListView.setAdapter(mAdapter);

        //se definen un broadcastReceiver que captura el broadcast del SO cuando captura los siguientes eventos:
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //Cuando se empareja o desempareja el bluethoot

        //se define (registra) el handler que captura los broadcast anteriormente mencionados.
        registerReceiver(mPairReceiver, filter);

        //broadcast receiver to handle pin action
        IntentFilter pairFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        getApplicationContext().registerReceiver(mPairingRequestReceiver, pairFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);

        super.onDestroy();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void pairDevice(BluetoothDevice device) {
        try {
            device.createBond();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo que actua como Listener de los eventos que ocurren en los componentes graficos de la activty
    private final DeviceListAdapter.OnPairButtonClickListener listenerBotonEmparejar = new DeviceListAdapter.OnPairButtonClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPairButtonClick(int position) {
            //Obtengo los datos del dispostivo seleccionado del listview por el usuario
            BluetoothDevice device = mDeviceList.get(position);

            //Se checkea si el sipositivo ya esta emparejado
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                //Si esta emparejado,quiere decir que se selecciono desemparjar y entonces se le desempareja
                unpairDevice(device);
            } else {
                //Si no esta emparejado,quiere decir que se selecciono conectar y entonces se lo conecta
                showToast("Emparejando");
                posicionListBluethoot = position;
                pairDevice(device);
            }
        }

        @Override
        public void onConnectButtonClick(int position) {
            BluetoothDevice device = mDeviceList.get(position);
            posicionListBluethoot = position;
            pairDevice(device);
            startBTActivity();
        }
    };

    private final BroadcastReceiver mPairingRequestReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                try {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!BuildConfig.HC05_MAC_ADRESS.equals(device.getAddress())) return;
                    device.setPin(BuildConfig.HC05_PASSWORD.getBytes(StandardCharsets.UTF_8));
                    abortBroadcast();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //Handler que captura los brodacast que emite el SO al ocurrir los eventos del bluethoot
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //Atraves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
            String action = intent.getAction();

            //si el SO detecto un emparejamiento o desemparjamiento de bulethoot
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                //Obtengo los parametro, aplicando un Bundle, que me indica el estado del Bluethoot
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                //se analiza si se puedo emparejar o no
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    //Si se detecto que se puedo emparejar el bluethoot
                    showToast("Emparejado");
                    startBTActivity();

                }  //si se detrecto un desaemparejamiento
                else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    showToast("No emparejado");
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private void startBTActivity() {
        BluetoothDevice device = (BluetoothDevice) mAdapter.getItem(posicionListBluethoot);

        //se inicia el Activity de comunicacion con el bluetooth, para transferir los datos.
        //Para eso se le envia como parametro el device del bluethoot Arduino
        Intent i = new Intent(DeviceListActivity.this, BluetoothActivity.class);
        i.putExtra(BLUETOOTH_DEVICE_KEY, device);
        startActivity(i);
        finish();
    }
}


