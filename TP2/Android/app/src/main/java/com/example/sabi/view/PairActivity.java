package com.example.sabi.view;

import static com.example.sabi.presenter.PairPresenter.MULTIPLE_PERMISSIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabi.R;
import com.example.sabi.contract.PairContract;
import com.example.sabi.presenter.PairPresenter;

import java.util.List;

public class PairActivity extends AppCompatActivity implements PairContract.IPairView{

    private final int permissionsRequestCode = 1000;

    private PairContract.IPairPresenter presenter;
    private TextView btStateTv;
    private Button activateBtBtn;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);
        btStateTv = findViewById(R.id.pair_activity_bt_state_tv);
        activateBtBtn = findViewById(R.id.pair_activity_activate_bt_btn);
        searchBtn = findViewById(R.id.pair_activity_search_btn);
        presenter = new PairPresenter(this);

        activateBtBtn.setOnClickListener(view -> presenter.onActivateBtButtonClick());
        searchBtn.setOnClickListener(view -> presenter.onSearchButtonClick());

        presenter.checkpermissions();
    }

    @Override
    public void updateViewsForBluetoothOn() {
        btStateTv.setVisibility(View.INVISIBLE);
        activateBtBtn.setEnabled(false);
        searchBtn.setEnabled(true);
    }

    @Override
    public void updateViewsForBluetoothOff() {
        btStateTv.setVisibility(View.VISIBLE);
        activateBtBtn.setEnabled(true);
        searchBtn.setEnabled(false);
    }

    @Override
    public void startForResult(Intent intent) {
        startActivityForResult(intent, permissionsRequestCode);
    }

    @Override
    public void requestPermissions(List<String> listPermissionsNeeded) {
        ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                MULTIPLE_PERMISSIONS
        );
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
    public void goToActivity(Class<? extends AppCompatActivity> activityClass) {
        final Context currentContext = getViewContext();
        Intent intent = new Intent(currentContext, activityClass);
        currentContext.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    // enableComponent(); // Now you call here what ever you want :)
                    Toast.makeText(this, "permisos ok", Toast.LENGTH_LONG).show();
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                    Toast.makeText(this, "ATENCION: La aplicacion no funcionara " +
                            "correctamente debido a la falta de Permisos", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}