package com.example.a50beees;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public abstract class Entity extends Sprite {
    protected String type;

    // types Entity tracks first
    protected ArrayList<String> preference;
    // types Entity avoids
    protected ArrayList<String> fear;

    protected ArrayList<Rect> effectors;

    // on a scale of 0 to 100;
    protected int aggression;

    // stats
    protected int attack_power, defence, health, attacking_range;

    // speed and movement
    protected int MAX_SPEED, ACCELERATION;
    protected int desired_speed;
    // since player can see everything from above, entities can move with fixed speed in any direction
    // angle is a standard trigonometry "angle" (form 0 to 2*PI)

    // v_x = cur_speed * cos(angle)
    // v_y = cur_speed * sin(angle)
    protected double direction_angle = Math.random() * 2 * Math.PI,
            angle_speed = 0,
            desired_angle = 0,
            desired_angle_speed = 0,
            ANGLE_ACCELERATION;

    // defines a "radius of rotation"
    // bigger the speed easier it is to turn
    // smaller radius means easier rotation
    // so the max rotation angle (angle_speed) is: arcsin(v/2r)
    protected int rotationRadius = 1;
    // an angle speed which is available at any time no matter the speed
    protected double minAvailableAngle = 0;
    protected int current_speed = 0;

    // -------- moving logic -----------
    Entity target;
    Entity last_seen_target;

    public Entity(SpriteGroup<Sprite> parent_group, ArrayList<Rect> effectors, Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect,
                  String type) {
        super(parent_group, frames_image, frame_count_columns, frame_count_rows, frame_count, rect);
        this.type = type;
        this.effectors = effectors;
    }

    @Override
    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.preRotate((float) (-direction_angle / Math.PI * 180));
        Log.i("DRAW", String.valueOf(direction_angle));

        Rect crop_rect = animation_list[(int) current_frame];
        current_frame += animation_speed;
        current_frame %= animation_list.length;

        Bitmap frame = Bitmap.createBitmap(frames_image,
                crop_rect.left, crop_rect.top, crop_rect.width(), crop_rect.height(),
                matrix, false);
        canvas.drawBitmap(frame, rect.left, rect.top, paint);

        paint.setColor(Color.BLUE);
        canvas.drawLine(rect.centerX(), rect.centerY(), (float) (rect.centerX() + 150 * Math.cos(desired_angle)), (float) (rect.centerY() - 150 * Math.sin(desired_angle)), paint);

        paint.setColor(Color.GREEN);
        canvas.drawLine(rect.centerX(), rect.centerY(), (float) (rect.centerX() + 150 * Math.cos(direction_angle)), (float) (rect.centerY() - 150 * Math.sin(direction_angle)), paint);
    }

    public void update() {
        update_movement_params();

        // normalize angles
        direction_angle = (direction_angle + 2 * Math.PI) % (2 * Math.PI);
        desired_angle = (desired_angle + 2 * Math.PI) % (2 * Math.PI);

        // move horizontally and check for collisions with effectors
        move_and_handle_collisions();

    }

    public void update_movement_params() {
        String state = getState();
    }

    public void reach_desire() {
        double max_angle = calculateMaxRotationAngle();

        // false - "-"; true - "+"
        boolean direction = (Math.abs(direction_angle - desired_angle) >= Math.PI ? (direction_angle > desired_angle) : (direction_angle < desired_angle));

        if (type.equals("insect") || current_speed > ACCELERATION) {
            angle_speed = Math.min(Math.min(Math.abs(direction_angle - desired_angle), max_angle), ANGLE_ACCELERATION) * (direction ? 1 : -1);
        } else {
            angle_speed = Math.min(Math.abs(direction_angle - desired_angle), ANGLE_ACCELERATION / 2) * (direction ? 1 : -1);
        }

        // angle speed is basically a delta angle in a time duration
        direction_angle += angle_speed;


        // GET TO DESIRED SPEED
        current_speed += Math.min(ACCELERATION * (desired_speed >= current_speed ? 1 : -1), desired_speed - current_speed);

        // if cur_speed < 0: cur_speed = 0
        current_speed = (current_speed + Math.abs(current_speed)) / 2;
        // if cur_speed > MAX_SPEED: cut off extra speed
        current_speed %= Math.max(MAX_SPEED, 1);
    }

    public String getState() {
        if (!(target == null)) {
            if (this.get_distance_to(target) > attacking_range) return "chasing";
            else return "attacking";
        }
        else if (!(last_seen_target == null)) return "searching";
        else return "idle";
    }

    public void move_and_handle_collisions() {
        // x
        rect.offset((int) (current_speed * Math.cos(direction_angle)), 0);

        // effectors
        for (Rect effector_rect : effectors) {
            if (this.collidesRect(effector_rect)) {

                // move out of the collision
                this.rect.offset(Math.cos(direction_angle) < 0 ?
                                effector_rect.right - this.rect.left + 1 :
                                effector_rect.left - this.rect.right - 1, 0);

                handleHorizontalCollision();
                break;
            }
        }

        // y
        rect.offset(0, (int) (-current_speed * Math.sin(direction_angle)));

        // effectors
        for (Rect effector_rect : effectors) {
            if (this.collidesRect(effector_rect)) {

                // move out of the collision
                this.rect.offset(0, Math.sin(direction_angle) < 0 ?
                        effector_rect.top - this.rect.bottom - 1 :
                        effector_rect.bottom - this.rect.top + 1);

                handleVerticalCollision();
                break;
            }
        }
    }

    public void handleHorizontalCollision() {
        desired_angle = (3 * Math.PI - desired_angle) % (2 * Math.PI);
        direction_angle = (3 * Math.PI - direction_angle) % (2 * Math.PI);
    }

    public void handleVerticalCollision() {
        desired_angle = 2 * Math.PI - desired_angle;
        direction_angle = 2 * Math.PI - direction_angle;
    }

    public double calculateMaxRotationAngle() {
        return Math.asin(Math.min(1, (float)current_speed / (2 * rotationRadius))) + minAvailableAngle;
    }

    public void setRotationRadius(int rotation_radius) {
        if (0 < rotation_radius) {
            this.rotationRadius = rotation_radius;
        } else throw new RuntimeException("rotation radius must be >0");
    }

    public void setMinAvailableAngle(double angle) {
        if (0 <= angle && angle <= Math.PI) {
            this.minAvailableAngle = angle;
        } else throw new RuntimeException("minimal available angle should be 0< and <PI");
    }

}
