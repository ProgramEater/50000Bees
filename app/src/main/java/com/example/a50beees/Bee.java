package com.example.a50beees;

import android.graphics.Rect;
import android.util.Pair;

public class Bee extends Insect {
    public Bee(Rect rect) {
        super(SandboxActivity.bitmaps.get("bee"), 2, 2, 4,
                new Rect(rect.left, rect.top, rect.left + 50, rect.top + 50),
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
