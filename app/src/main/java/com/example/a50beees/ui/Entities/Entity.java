package com.example.a50beees.ui.Entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.CallSuper;

import com.example.a50beees.ui.SandboxView;

import java.util.ArrayList;


public abstract class Entity extends Sprite {
    protected String type;

    // types Entity tracks first
    protected ArrayList<String> preference;
    // types Entity avoids
    protected ArrayList<String> fear;

    // on a scale of 0 to 100;
    protected int aggression = 50;

    // stats
    protected int attack_power, defence, health, attacking_range, sight_distance;
    double field_of_view;

    // speed and movement
    protected double MAX_SPEED, ACCELERATION;
    protected double desired_speed;
    // since player can see everything from above, entities can move with fixed speed in any direction
    // angle is a standard trigonometry "angle" (form 0 to 2*PI)

    // v_x = cur_speed * cos(angle)
    // v_y = cur_speed * sin(angle)
    protected double direction_angle = Math.random() * 2 * Math.PI,
            angle_speed = 0,
            desired_angle = 0,
            desired_angle_speed = 0,
            ANGLE_ACCELERATION;

    protected int max_search_time = 5000;
    // how far can entity feel without even seeing (hear/feel)
    protected int awareness_radius = 100;

    // defines a "radius of rotation"
    // bigger the speed easier it is to turn
    // smaller radius means easier rotation
    // so the max rotation angle (angle_speed) is: arcsin(v/2r)
    protected int rotationRadius = 1;
    // an angle speed which is available at any time no matter the speed
    protected double minAvailableAngle = 0;
    protected double current_speed = 0;
    protected boolean valid = true;

    // -------- moving logic -----------
    Entity target;
    SimpleTarget last_seen_target;

    /**
     * Every Entity must have
     * @param health health points
     * @param attack_power damage points
     * @param defence amount of points which is subtracted from taken damage
     * @param attacking_range useless probably
     * @param sight_distance distance on which entity can see
     * @param field_of_view angle of view available (in radians)
     * @param MAX_SPEED
     * @param ACCELERATION
     * @param ANGLE_ACCELERATION**/
    public Entity(Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect, String type, int aggression, int attack_power, int defence, int health, int attacking_range, int sight_distance, double field_of_view, double MAX_SPEED, double ACCELERATION, double ANGLE_ACCELERATION, int rotationRadius) {
        super(frames_image, frame_count_columns, frame_count_rows, frame_count, rect);
        this.type = type;
        this.aggression = aggression;
        this.attack_power = attack_power;
        this.defence = defence;
        this.health = health;
        this.attacking_range = attacking_range;
        this.sight_distance = sight_distance;
        this.field_of_view = field_of_view;
        this.MAX_SPEED = MAX_SPEED;
        this.ACCELERATION = ACCELERATION;
        this.ANGLE_ACCELERATION = ANGLE_ACCELERATION;
        this.rotationRadius = rotationRadius;
    }

    public Entity(Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count, Rect rect,
                  String type) {
        super(frames_image, frame_count_columns, frame_count_rows, frame_count, rect);

        this.type = type;
    }

    @Override
    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.preRotate((float) (-direction_angle / Math.PI * 180));

        Rect crop_rect = animation_list[(int) current_frame];
        current_frame += animation_speed;
        current_frame %= animation_list.length;

        Bitmap frame = Bitmap.createBitmap(frames_image,
                crop_rect.left, crop_rect.top, crop_rect.width(), crop_rect.height(),
                matrix, false);
        canvas.drawBitmap(frame, rect.left, rect.top, paint);

        //paint.setColor(Color.BLUE);
        //canvas.drawLine(rect.centerX(), rect.centerY(), (float) (rect.centerX() + 150 * Math.cos(desired_angle)), (float) (rect.centerY() - 150 * Math.sin(desired_angle)), paint);
//
        //paint.setColor(Color.GREEN);
        //canvas.drawLine(rect.centerX(), rect.centerY(), (float) (rect.centerX() + 150 * Math.cos(direction_angle)), (float) (rect.centerY() - 150 * Math.sin(direction_angle)), paint);
    }

    public void update() {
        set_desire_parameters();

        reach_desire();

        // normalize angles
        direction_angle = (direction_angle + 2 * Math.PI) % (2 * Math.PI);
        desired_angle = (desired_angle + 2 * Math.PI) % (2 * Math.PI);
        desired_angle_speed = Math.min(Math.abs(desired_angle_speed), Math.PI / 2) * (desired_angle_speed > 0 ? 1 : -1);

        // move horizontally and check for collisions with effectors
        move_and_handle_collisions();

    }

    @CallSuper
    public void set_desire_parameters() {
        String state = getState();
        switch (state) {
            case "idle": pick_a_target(); break;
            case "chasing": desired_angle = get_angle_to(target); desired_speed = MAX_SPEED; break;
            case "attacking": attack_target(); break;
            case "searching": {
                Pair<Integer, Integer> possible_location = last_seen_target.get_possible_location();
                desired_angle = get_angle_to_point(possible_location.first, possible_location.second);
                desired_speed = MAX_SPEED;

                // if the search time is exceeded give up
                if (SandboxView.get_time() - last_seen_target.last_time > max_search_time) {
                    last_seen_target = null;
                    target_lost();
                } else if (this.sees_entity(target) || this.get_distance_to(target) <= awareness_radius) {
                    // if we see a target, all good
                    last_seen_target = null;
                }

                break;
            }
        }

        // if a target dies we need to invalidate it
        if (!state.equals("idle")) if (!target.isValid()) {
            target_lost();
            last_seen_target = null;
        }
    }

    @CallSuper
    public void target_lost() {
        target = null;
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
        direction_angle += angle_speed * SandboxView.getTime_coefficient();


        // GET TO DESIRED SPEED
        current_speed += Math.min(ACCELERATION * (desired_speed >= current_speed ? 1 : -1), desired_speed - current_speed);

        // if cur_speed < 0: cur_speed = 0
        current_speed = (current_speed + Math.abs(current_speed)) / 2;
        // if cur_speed > MAX_SPEED: cut off extra speed
        current_speed = Math.min(MAX_SPEED, current_speed);
    }

    public void pick_a_target() {
        // TODO implement preference/fear lists
        for (Entity entity : SandboxView.getEntities()) {
            if (this == entity) continue;
            if (!entity.isValid()) continue;
            if ((aggression <= 90) && (this.type.equals(entity.type))) continue;

            double distance = this.get_distance_to(entity);
            if (sight_distance >= distance) {
                if (distance == 0) {
                    target = entity;
                }
                else if (this.sees_entity(entity)) {
                    target = entity;
                }
            }
        }
    }

    public boolean sees_entity(Entity entity) {
        double angle_to_entity = get_angle_to(entity);
        return (direction_angle - field_of_view / 2 < angle_to_entity && angle_to_entity < direction_angle + field_of_view / 2);
    }

    public boolean attack_target() {
        if (this.sees_entity(target)) {target.get_hit(attack_power, this); return true;}
        return false;
    }

    public void get_hit(int damage, Entity attacker) {
        health -= Math.max(damage + defence, 1);
        if (health <= 0) die();
    }

    public void die() {
        SandboxView.getEntities().remove(this);
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public String getState() {
        if (!(last_seen_target == null)) return "searching";
        else if (!(target == null)) {
            if (!this.sees_entity(target)) {
                last_seen_target = new SimpleTarget(target.rect.centerX(), target.rect.centerY(), SandboxView.get_time(),
                        target.direction_angle, target.current_speed, target.type);
                return "searching";
            }

            if (this.collidesSprite(target)) return "attacking";
            if (attacking_range != 0) if (get_distance_to(target) <= attacking_range) return "attacking";
            return "chasing";
        }
        else return "idle";
    }

    public void move_and_handle_collisions() {
        // x
        rect.offset((int) (current_speed * SandboxView.getTime_coefficient() * Math.cos(direction_angle)), 0);

        // effectors
        for (Rect effector_rect : SandboxView.getEffectors()) {
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
        rect.offset(0, (int) (-current_speed * SandboxView.getTime_coefficient() * Math.sin(direction_angle)));

        // effectors
        for (Rect effector_rect : SandboxView.getEffectors()) {
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

    protected class SimpleTarget {
        int x, y, last_time;
        double directional_angle, speed;
        String type;

        public SimpleTarget(int x, int y, int last_time, double directional_angle, double speed, String type) {
            this.x = x;
            this.y = y;
            this.last_time = last_time;
            this.directional_angle = directional_angle;
            this.speed = speed;
            this.type = type;
        }

        public Pair<Integer, Integer> get_possible_location() {
            return new Pair<>((int) (x + Math.cos(direction_angle) * speed * (SandboxView.get_time() - last_time) / SandboxView.getTimerUpdateInterval()),
                    (int) (y - Math.sin(direction_angle) * speed * (SandboxView.get_time() - last_time) / SandboxView.getTimerUpdateInterval()));
        }
    }

}
