package com.example.a50beees.ui.Entities;

import android.graphics.Rect;
import android.util.Log;

import com.example.a50beees.ui.Activities.SandboxActivity;

public class TestEntity extends Entity {
    public TestEntity(Rect rect) {
        super(SandboxActivity.getBitmaps().get("arrow"), 1, 1, 1, new Rect(rect.left, rect.top, rect.left + 50, rect.top + 50), "test");
    }

    @Override
    public void set_desire_parameters() {
        super.set_desire_parameters();
        direction_angle += Math.PI / 10;
        direction_angle %= 2 * Math.PI;
        Log.i("Test", String.valueOf(direction_angle));
    }
}
