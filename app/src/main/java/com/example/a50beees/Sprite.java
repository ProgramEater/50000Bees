package com.example.a50beees;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite {
    SpriteGroup<Sprite> parent_group;

    // frames are listed in rows from left to right, then continue on next row
    protected Bitmap frames_image;
    protected int frame_count_columns, frame_count_rows, frame_count;
    protected Paint paint = new Paint();

    // -------- animation vars -------------
    // contains number of Rects for cropping *frames_image on frames
    protected Rect[] animation_list;
    protected double current_frame = 0;
    protected double animation_speed = 1;

    protected Rect rect;


    public Sprite(SpriteGroup<Sprite> parent_group, Bitmap frames_image, int frame_count_columns, int frame_count_rows, int frame_count,
           Rect rect) {
        this.parent_group = parent_group;

        this.rect = rect;

        this.frames_image = Bitmap.createScaledBitmap(frames_image,
                rect.width() * frame_count_columns,
                rect.height() * frame_count_rows,
                false);

        this.frame_count_columns = frame_count_columns;
        this.frame_count_rows = frame_count_rows;
        this.frame_count = frame_count;

        fill_animation_list(rect, frame_count_columns, frame_count_rows, frame_count);
    }

    private void fill_animation_list(Rect rect, int frame_count_columns, int frame_count_rows,
                                     int frame_count) {
        animation_list = new Rect[frame_count];

        int frame_w = rect.width(), frame_h = rect.height();

        // i - row
        // j - column
        for (int i = 0; i < frame_count_rows; i++) {
            for (int j = 0; j < frame_count_columns; j++) {
                int frame_number = frame_count_columns * i + j + 1;

                // if frames have ended: break the cycle
                if (frame_number > frame_count) break;

                animation_list[frame_number - 1] = new Rect(
                        frame_w * j, frame_h * i,
                        frame_w * (j + 1), frame_h * (i + 1));
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(frames_image, animation_list[(int) current_frame], rect, paint);
        current_frame++;
        current_frame %= animation_list.length;
    }

    public void update() {}

    public void change_size(int dst_Width, int dst_Height) {
        int[] new_rect_params = new int[] {
                rect.centerX() - dst_Width / 2,
                rect.centerY() - dst_Height / 2};

        rect = new Rect(
                new_rect_params[0],
                new_rect_params[0] + dst_Width,
                new_rect_params[1],
                new_rect_params[1] + dst_Height);

        frames_image = Bitmap.createScaledBitmap(frames_image, dst_Width, dst_Height, false);

        fill_animation_list(rect, frame_count_columns, frame_count_rows, frame_count);
    }

    public boolean collidesSprite(Sprite sprite) {
        return Rect.intersects(this.getRect(), sprite.getRect());
    }

    public boolean collidesRect(Rect rect) {
        return Rect.intersects(this.getRect(), rect);
    }

    public Bitmap getFrames_image() {
        return frames_image;
    }

    public Rect getRect() {
        return rect;
    }

    public int get_distance_to(Sprite sprite) {
        return (int) Math.sqrt(Math.pow(this.rect.centerX(), 2) +
                               Math.pow(sprite.rect.centerX(), 2));
    }

    protected void setAnimation_speed(double animation_speed) {
        this.animation_speed = animation_speed;
    }
}
