package com.example.a50beees.ui.Entities;

import android.graphics.Rect;
import android.util.Pair;

import com.example.a50beees.ui.Activities.SandboxActivity;

public class Bee extends Insect {
    public Bee(Rect rect) {
        super(SandboxActivity.getBitmaps().get("bee"), 3, 3, 9,
                new Rect(rect.left, rect.top, rect.left + 50, rect.top + 90),
                60,
                100,
                0,
                10,
                0,
                1500,
                Math.PI / 2,
                100,
                1,
                .12,
                20);
    }

    @Override
    public boolean attack_target() {
        if (super.attack_target()) {
            die();
            return true;
        }
        return false;
    }

    @Override
    public void target_lost() {
        super.target_lost();
        home_point = new Pair<>(rect.left, rect.top);
    }
}
