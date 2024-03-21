package com.example.a50beees;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SandboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        SandboxView sandbox_view = (SandboxView) findViewById(R.id.sandbox_view);

    }
}
