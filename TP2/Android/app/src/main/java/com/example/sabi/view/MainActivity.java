package com.example.sabi.view;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.sabi.R;
import com.example.sabi.contract.MainContract;
import com.example.sabi.presenter.MainPresenter;
import com.example.sabi.view.adapters.OptionsAdapter;
import com.example.sabi.view.static_data.MainOptions;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView {
    private MainContract.IMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);

        OptionsAdapter adapter = new OptionsAdapter(this);
        final ListView list = findViewById(R.id.list_options);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> goToOption(position));
    }

    private void goToOption(int position) {
        switch (position) {
            case MainOptions.BLUETOOTH_OPTION_INDEX:
                presenter.onBluetoothButtonClick();
                break;
            case MainOptions.SENSORS_OPTION_INDEX:
                presenter.onSensorActivityClick();
                break;
            default:
                // Do nothing
        }
    }

    @Override
    public void goToActivity(Class<? extends AppCompatActivity> activityClass) {
        final Context currentContext = getViewContext();
        Intent intent = new Intent(currentContext, activityClass);
        currentContext.startActivity(intent);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public boolean isAttached() {
        return this.hasWindowFocus();
    }
}