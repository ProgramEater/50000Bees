package com.example.a50beees.ui.Entities;

import android.graphics.Bitmap;
import android.graphics.Rect;

import android.util.Pair;

import com.example.a50beees.ui.SandboxView;

public abstract class Insect extends Entity {
    Pair<Integer, Integer> home_point;

    public Insect(Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect, int aggression, int attack_power, int defence, int health, int attacking_range, int sight_distance, double field_of_view, double MAX_SPEED, double ACCELERATION, double ANGLE_ACCELERATION, int rotationRadius) {
        super(frames_image, frame_count_columns, frame_count_rows, frame_count, rect, "insect", aggression, attack_power, defence, health, attacking_range, sight_distance, field_of_view, MAX_SPEED, ACCELERATION, ANGLE_ACCELERATION, rotationRadius);
        home_point = new Pair<>(rect.left, rect.top);
    }

    @Override
    public void set_desire_parameters() {
        super.set_desire_parameters();
        String state = getState();
        switch (state) {
            case "idle": {
                desired_speed = MAX_SPEED / 2;

                if (Math.abs(rect.left - home_point.first) > 300 || Math.abs(rect.top - home_point.second) > 300) {
                    desired_angle = Math.acos((home_point.first - rect.centerX()) / Math.hypot(rect.centerX() - home_point.first, rect.centerY() - home_point.second));
                    desired_angle = ((home_point.second - rect.centerY()) < 0 ? desired_angle : 2 * Math.PI - desired_angle);

                } else {
                    desired_angle_speed += (0.5 - Math.random()) * ANGLE_ACCELERATION;

                    desired_angle += desired_angle_speed * SandboxView.getTime_coefficient();
                }

                break;
            }
            case "chasing": {
                desired_angle = get_angle_to(target);
                desired_speed = MAX_SPEED;

                break;
            }
            case "attacking": {
                desired_angle = get_angle_to(target);
                desired_speed = MAX_SPEED + 1;

                break;
            }
            //TODO MAKE OTHER STATES
        }
    }
}
