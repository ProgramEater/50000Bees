package com.example.a50beees;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;

public abstract class Entity extends Sprite {
    protected String type;

    // types Entity tracks first
    protected ArrayList<String> preference;
    // types Entity avoids
    protected ArrayList<String> fear;

    // on a scale of 0 to 100;
    protected int aggression;

    // stats
    protected int attack_power, defence, health, attacking_range;

    // speed and movement
    protected int MAX_SPEED, ACCELERATION;
    protected int desired_speed;
    // since player can see everything from above, entities can move with fixed speed in any direction
    // angle is a standard trigonometry "angle"

    //   0 => to right
    // < 0 => partially (or fully) down
    // > 0 => partially (or fully) up
    // = pi => to left

    // v_x = cur_speed * cos(angle)
    // v_y = cur_speed * sin(angle)
    protected double direction_angle = (1 - Math.random() * 2) * Math.PI,
            angle_speed = 0,
            ANGLE_ACCELERATION;

    // defines a "radius of rotation"
    // bigger the speed easier it is to turn
    // smaller radius means easier rotation
    // so the max rotation angle (angle_speed) is: arcsin(v/2r)
    private int rotation_radius;
    protected int current_speed = 0;

    // -------- moving logic -----------
    Entity target;
    Entity last_seen_target;

    public Entity(Context context, Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect,
                  String type) {
        super(context, frames_image, frame_count_columns, frame_count_rows, frame_count, rect);
        this.type = type;
    }

    @Override
    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        float a = (float) ((-direction_angle + (direction_angle > 0 ? Math.PI * 2 : 0)) / Math.PI * 180);
        matrix.postRotate((float) ((-direction_angle + (direction_angle > 0 ? Math.PI * 2 : 0)) / Math.PI * 180));

        Rect crop_rect = animation_list[current_frame];
        current_frame++;
        current_frame %= animation_list.length;
        Bitmap frame = Bitmap.createBitmap(frames_image,
                crop_rect.left, crop_rect.top, crop_rect.width(), crop_rect.height(),
                matrix, false);
        canvas.drawBitmap(frame, rect.left, rect.top, paint);

    }

    public void update() {
        update_movement_params();
        rect.offset((int) (current_speed * Math.cos(direction_angle)),
                    (int) (-current_speed * Math.sin(direction_angle)));
    }

    public void update_movement_params() {
        String state = getState();
        switch (state) {
            case "idle": {
                desired_speed = MAX_SPEED / 2;

                // change angle speed and angle
                double max_angle = Math.asin(Math.min(1, (float)current_speed / (2 * rotation_radius)));

                angle_speed += ((0.5 - Math.random()) - (angle_speed / (2 * max_angle))) * ANGLE_ACCELERATION;
                angle_speed = max_angle == 0 ? 0 : angle_speed % max_angle;
                direction_angle += angle_speed;

                if (Math.PI < direction_angle || direction_angle < -Math.PI) {
                    // if angle > PI or < -PI : return to normal angle (-PI < a < PI)
                    boolean sign = direction_angle > 0;

                    direction_angle = Math.abs(direction_angle) % Math.PI - Math.PI;
                    direction_angle *= sign ? 1 : -1;
                }


                double rel_distance_to_desired_speed = ((float) Math.abs(desired_speed - current_speed) / ACCELERATION - 1) *
                        (desired_speed > current_speed ? 1 : -1);

                // change speed
                current_speed += (0.5 - Math.random() + rel_distance_to_desired_speed / 10) * ACCELERATION / 2;

                // if cur_speed < 0: cur_speed = 0
                current_speed = (current_speed + Math.abs(current_speed)) / 2;
                // if cur_speed > MAX_SPEED: cut off extra speed
                current_speed %= MAX_SPEED;
            }
        //TODO MAKE OTHER STATES
        }
    }

    public String getState() {
        if (!(target == null)) {
            if (this.get_distance_to(target) > attacking_range) return "chasing";
            else return "attacking";
        }
        else if (!(last_seen_target == null)) return "searching";
        else return "idle";
    }

    public void setRotationRadius(int rotation_radius) {
        // radius can vary from 0 (not 0) to 2 * MAX_SPEED (then the max rotation angle is PI (180)
        // the bigger on is not needed
        if (0 < rotation_radius && rotation_radius < 2 * MAX_SPEED) {
            this.rotation_radius = rotation_radius;
        }
    }

}
