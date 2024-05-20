package com.example.a50beees.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50beees.R;
import com.example.a50beees.data.source.CredentialsDataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CredentialsDataSource credentialsDataSource = CredentialsDataSource.getInstance();

        Button sandbox_btn = (Button) findViewById(R.id.sandbox_button);
        Button exit_btn = (Button) findViewById(R.id.exit_button);

        TextView login_textview = findViewById(R.id.login_textview);
        login_textview.setOnClickListener(view -> {
            if (credentialsDataSource.getAuthData() == null) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            } else Toast.makeText(this, "logged", Toast.LENGTH_SHORT).show();
        });

        if (credentialsDataSource.getAuthData() == null) login_textview.setText("login");
        else login_textview.setText(credentialsDataSource.getLogin());

        sandbox_btn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SandboxActivity.class);

            startActivity(i);
        });

        exit_btn.setOnClickListener(view -> finish());
    }
}