package com.example.a50beees;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bee extends Entity {
    public Bee(Context context, Rect rect) {
        super(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.bee1_2_2_4), 2, 2, 4, rect, "insect");
        attacking_range = 10;
        MAX_SPEED = 150;
        ACCELERATION = 10;
        ANGLE_ACCELERATION = .05;
        setRotationRadius(20);
    }
}
