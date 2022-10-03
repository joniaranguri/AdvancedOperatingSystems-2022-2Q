package com.example.sabi.view;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.sabi.R;
import com.example.sabi.presenter.AppPresenter;
import com.example.sabi.presenter.IAppPresenter;

public class MainActivity extends AppCompatActivity implements IAppActivity {

    private Button b;
    private EditText editText;
    private IAppPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        presenter = new AppPresenter(this.getCurrentFocus());

        b.setOnClickListener(view -> {
            if(!editText.getText().toString().isEmpty()) {
//                presenter.sendCommand(editText.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "El comando no debe estar vacio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void goToSecondActivity() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }
}