package com.example.a50beees.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a50beees.R;
import com.google.android.material.slider.Slider;

import java.util.TreeMap;

public class SandboxActivity extends AppCompatActivity {
    SandboxView sandbox_view;
    Slider slider;

    static TreeMap<String, Bitmap> bitmaps = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        fill_bitmaps();

        slider = (Slider) findViewById(R.id.time_slider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float v, boolean b) {
                SandboxView.time_coefficient = v;
            }
        });

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
