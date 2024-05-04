package com.example.a50beees;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

public class SandboxActivity extends AppCompatActivity {
    SandboxView sandbox_view;

    // The "active pointer" is the one moving the object.
    private int mActivePointerId = INVALID_POINTER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        sandbox_view = (SandboxView) findViewById(R.id.sandbox_view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
}
