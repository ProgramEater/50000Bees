package com.example.a50beees.ui.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a50beees.R;
import com.example.a50beees.ui.Recycler.RecyclerViewClickListener;
import com.example.a50beees.ui.Recycler.SandboxRecyclerAdapter;
import com.example.a50beees.ui.SandboxView;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.TreeMap;

public class SandboxActivity extends AppCompatActivity implements RecyclerViewClickListener {
    SandboxView sandbox_view;
    Slider slider;
    private String spawn_entity_type = "bee";

    private final static TreeMap<String, Bitmap> bitmaps = new TreeMap<>();
    private final ArrayList<Pair<String, Bitmap>> recycler_view_bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        // fill bitmaps with data
        fill_bitmaps();

        // recycler view
        // initialize recycler_view_bitmaps
        fill_recycler_view_bitmaps();
        RecyclerView sandbox_recyclerview = findViewById(R.id.sandbox_recycler_view);
        sandbox_recyclerview.setAdapter(new SandboxRecyclerAdapter(recycler_view_bitmaps, this));

        slider = findViewById(R.id.time_slider);
        slider.addOnChangeListener((slider, v, b) -> sandbox_view.setTime_coefficient(v));

        sandbox_view = findViewById(R.id.sandbox_view);
    }

    private void fill_recycler_view_bitmaps() {
        recycler_view_bitmaps.add(new Pair<>("bee", bitmaps.get("bee")));
        recycler_view_bitmaps.add(new Pair<>("rabbit", bitmaps.get("rabbit")));
    }

    protected void fill_bitmaps() {
        bitmaps.put("bee", BitmapFactory.decodeResource(getResources(), R.drawable.bee_3_3_9));
        bitmaps.put("rabbit", BitmapFactory.decodeResource(getResources(), R.drawable.rabbit_2_2_4));
        bitmaps.put("arrow", BitmapFactory.decodeResource(getResources(), R.drawable.arrow));
        bitmaps.put("grass", BitmapFactory.decodeResource(getResources(), R.drawable.sandbox_grass_background_image1));
    }

    public static TreeMap<String, Bitmap> getBitmaps() {
        return bitmaps;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        spawn_entity_type = recycler_view_bitmaps.get(position).first;
    }

    public String getSpawn_entity_type() {
        return spawn_entity_type;
    }
}
