package com.example.a50beees.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.a50beees.ui.Activities.SandboxActivity;

public class SandboxBackground {
    Bitmap single_image = SandboxActivity.getBitmaps().get("grass");
    int w, h;
    int[][] array;

    public SandboxBackground(int w, int h) {
        this.w = w;
        this.h = h;
        init_array();
    }

    private void init_array() {
        int nw = (int) Math.ceil((double) w / single_image.getWidth());
        int nh = (int) Math.ceil((double) h / single_image.getHeight());

        int i = 0;
        array = new int[nw * nh][2];
        for (int y = 0; y < nh; y++) {
            for (int x = 0; x < nw; x++, i++) {
                array[i][0] = x * single_image.getWidth();
                array[i][1] = y * single_image.getHeight();
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int[] mas : array) {
            canvas.drawBitmap(single_image, mas[0], mas[1], paint);
        }
    }
}
