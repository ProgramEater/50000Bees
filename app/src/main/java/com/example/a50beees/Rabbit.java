package com.example.a50beees;

import android.graphics.Rect;

public class Rabbit extends Entity{
    final int JUMP_COOLDOWN = 30, JUMP_DURATION = 10;
    int last_jump = -31;

    public Rabbit(Rect rect) {
        super(SandboxActivity.bitmaps.get("rabbit"), 2, 2, 4,
                new Rect(rect.left, rect.top, rect.left + 100, rect.top + 100), "Rabbit",
                50,
                30,
                5,
                100,
                0,
                1200,
                Math.PI,
                20,
                15,
                .05,
                100);
        this.setAnimation_speed(.3);
    }

    @Override
    public void set_desire_parameters() {
        super.set_desire_parameters();

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
            }
            //TODO MAKE OTHER STATES
        }
    }
}
