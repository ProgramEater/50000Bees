package com.example.a50beees;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

public class Bee extends Entity {
    public Bee(SpriteGroup<Sprite> parent_group, Context context, ArrayList<Rect> effectors, Rect rect) {
        super(parent_group, context, effectors, BitmapFactory.decodeResource(context.getResources(), R.drawable.bee1_2_2_4), 2, 2, 4, rect, "insect");
        attacking_range = 10;
        MAX_SPEED = 100;
        ACCELERATION = 10;
        ANGLE_ACCELERATION = .05;
        setRotationRadius(20);
    }
}
