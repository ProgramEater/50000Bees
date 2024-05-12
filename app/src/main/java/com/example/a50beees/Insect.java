package com.example.a50beees;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Pair;

import java.util.ArrayList;

public class Insect extends Entity {
    Pair<Integer, Integer> home_point;

    public Insect(SpriteGroup<Sprite> parent_group, ArrayList<Rect> effectors, Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect) {
        super(parent_group, effectors, frames_image, frame_count_columns, frame_count_rows, frame_count, rect, "insect");
        home_point = new Pair<>(rect.left, rect.top);
    }

    @Override
    public void update_movement_params() {
        String state = getState();
        switch (state) {
            case "idle": {
                desired_speed = MAX_SPEED / 2;

                if (Math.abs(rect.left - home_point.first) > 300 || Math.abs(rect.top - home_point.second) > 300) {
                    desired_angle = Math.acos((home_point.first - rect.centerX()) / Math.hypot(rect.centerX() - home_point.first, rect.centerY() - home_point.second));
                    desired_angle = ((home_point.second - rect.centerY()) < 0 ? desired_angle : 2 * Math.PI - desired_angle);

                } else {
                    desired_angle_speed += (0.5 - Math.random()) * ANGLE_ACCELERATION;
                    desired_angle += desired_angle_speed;
                }

                reach_desire();
            }
            //TODO MAKE OTHER STATES
        }
    }
}
