package com.example.a50beees;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class SpriteGroup<T extends Sprite> implements Iterable<T> {
    protected HashSet<T> spriteGroup;
    SpriteGroup() {
        spriteGroup = new HashSet<>();
    }

    public void add(T obj) {spriteGroup.add(obj);}

    public void addAll(Collection<T> collection) {spriteGroup.addAll(collection);}

    public void updateAll() {
        for (T obj : spriteGroup) {
            obj.update();
        }
    }

    public void drawAll(Canvas canvas) {
        for (T obj : spriteGroup) {
            obj.draw(canvas);
        }
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return spriteGroup.iterator();
    }
}
