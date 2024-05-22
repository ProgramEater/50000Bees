package com.example.a50beees.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.a50beees.R;
import com.example.a50beees.data.source.CredentialsDataSource;

public class MainActivity extends AppCompatActivity {
    CredentialsDataSource credentialsDataSource;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credentialsDataSource = CredentialsDataSource.getInstance();
        Button sandbox_btn = (Button) findViewById(R.id.sandbox_button);
        Button exit_btn = (Button) findViewById(R.id.exit_button);

        login_button = findViewById(R.id.login_textview);
        login_button.setOnClickListener(view -> {
            if (credentialsDataSource.getAuthData() == null) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        if (credentialsDataSource.getAuthData() == null) login_button.setText("login");
        else login_button.setText(credentialsDataSource.getLogin());

        sandbox_btn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SandboxActivity.class);

            startActivity(i);
        });

        exit_btn.setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (credentialsDataSource.getAuthData() == null) login_button.setText("login");
        else login_button.setText(credentialsDataSource.getLogin());
    }
}