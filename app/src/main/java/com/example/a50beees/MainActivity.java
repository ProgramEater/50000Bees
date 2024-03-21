package com.example.a50beees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sandbox_btn = (Button) findViewById(R.id.sandbox_button);
        Button exit_btn = (Button) findViewById(R.id.exit_button);

        sandbox_btn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SandboxActivity.class);

            startActivity(i);
        });

        exit_btn.setOnClickListener(view -> finish());
    }
}