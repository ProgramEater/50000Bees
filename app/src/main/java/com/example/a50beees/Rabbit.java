package com.example.a50beees;

import android.graphics.Rect;

import java.util.ArrayList;

public class Rabbit extends Entity{
    final int JUMP_COOLDOWN = 30, JUMP_DURATION = 10;
    int last_jump = -31;

    public Rabbit(SpriteGroup<Sprite> parent_group, ArrayList<Rect> effectors, Rect rect) {
        super(parent_group, effectors, SandboxActivity.bitmaps.get("rabbit"), 2, 2, 4,
                new Rect(rect.left, rect.top, rect.left + 100, rect.top + 100), "Rabbit");
        this.setAnimation_speed(.3);
        attacking_range = 20;
        MAX_SPEED = 20;
        ACCELERATION = 15;
        ANGLE_ACCELERATION = .05;
        setRotationRadius(100);
    }

    @Override
    public void update_movement_params() {
        String state = getState();
        switch (state) {
            case "idle": {
                if (last_jump + JUMP_COOLDOWN < SandboxView.get_time()) last_jump = SandboxView.get_time() + JUMP_DURATION;

                if (last_jump + JUMP_COOLDOWN + ((0.5 - Math.random()) * 15) > SandboxView.get_time() && SandboxView.get_time() > last_jump) {
                    // no jump
                    desired_speed = 0;
                } else {
                    desired_speed = (int) (MAX_SPEED * 0.6);

                    desired_angle_speed += (0.5 - Math.random()) * ANGLE_ACCELERATION;
                    desired_angle_speed = Math.min(desired_angle_speed, calculateMaxRotationAngle());

                    desired_angle += desired_angle_speed;
                }

                reach_desire();
            }
            //TODO MAKE OTHER STATES
        }
    }
}
