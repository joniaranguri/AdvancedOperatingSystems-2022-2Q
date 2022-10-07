package com.example.sabi.view;

import static com.example.sabi.commons.Constants.MESSAGE_COMMAND_SHOULD_BE_NOT_EMPTY;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sabi.R;
import com.example.sabi.contract.Contract;
import com.example.sabi.presenter.AppPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    private Button sendCommandButton;
    private EditText editText;
    private Contract.IAppPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendCommandButton = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        presenter = new AppPresenter(this);
        sendCommandButton.setOnClickListener(getCommandButtonListener());
    }

    private View.OnClickListener getCommandButtonListener() {
        return view -> {
            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(getViewContext(), MESSAGE_COMMAND_SHOULD_BE_NOT_EMPTY, Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.sendCommand(editText.getText().toString());
        };
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
    public void showCommand(String command) {
        Toast.makeText(getViewContext(), command, Toast.LENGTH_SHORT).show();
    }
}
