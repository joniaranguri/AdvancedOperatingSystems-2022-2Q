package com.example.sabi.view;

import static com.example.sabi.commons.Constants.COMMAND_ASK_WATER_LEVEL;
import static com.example.sabi.view.DeviceListActivity.BLUETOOTH_DEVICE_KEY;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sabi.R;
import com.example.sabi.contract.BluetoothContract;
import com.example.sabi.presenter.BluetoothPresenter;

import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements BluetoothContract.IBluetoothView {

    private TextView tankStatusEditText;
    private BluetoothContract.IBluetoothPresenter presenter;
    public static final UUID UUID_SABI = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        tankStatusEditText = findViewById(R.id.stateTank_textview);
        presenter = new BluetoothPresenter(this);
        findViewById(R.id.bluetooth_led_off_button).setOnClickListener(view -> presenter.sendCommand(COMMAND_ASK_WATER_LEVEL));
        final BluetoothDevice device = getIntent().getExtras().getParcelable(BLUETOOTH_DEVICE_KEY);
        presenter.initBluetoothService(device);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public boolean isAttached() {
        return this.hasWindowFocus();
    }

    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(getViewContext(), message, Toast.LENGTH_SHORT).show();
            tankStatusEditText.setText(message);
        });
    }
}
