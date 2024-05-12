package com.example.a50beees;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.TreeMap;

public class SandboxActivity extends AppCompatActivity {
    SandboxView sandbox_view;

    static TreeMap<String, Bitmap> bitmaps = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        fill_bitmaps();

        sandbox_view = findViewById(R.id.sandbox_view);
    }

    protected void fill_bitmaps() {
        bitmaps.put("bee", BitmapFactory.decodeResource(getResources(), R.drawable.bee1_2_2_4));
        bitmaps.put("rabbit", BitmapFactory.decodeResource(getResources(), R.drawable.rabbit_2_2_4));
        bitmaps.put("arrow", BitmapFactory.decodeResource(getResources(), R.drawable.arrow));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
}
