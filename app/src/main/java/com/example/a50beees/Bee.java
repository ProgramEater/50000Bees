package com.example.a50beees;

import android.graphics.Rect;

import java.util.ArrayList;

public class Bee extends Insect {
    public Bee(SpriteGroup<Sprite> parent_group, ArrayList<Rect> effectors, Rect rect) {
        super(parent_group, effectors, SandboxActivity.bitmaps.get("bee"), 2, 2, 4, new Rect(rect.left, rect.top, rect.left + 50, rect.top + 50));
        attacking_range = 10;
        MAX_SPEED = 100;
        ACCELERATION = 10;
        ANGLE_ACCELERATION = .12;
        setRotationRadius(20);
    }
}
