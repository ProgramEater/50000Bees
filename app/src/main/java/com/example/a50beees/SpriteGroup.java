package com.example.a50beees;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class SpriteGroup<T extends Sprite> implements Iterable<T> {
    protected HashSet<T> spriteGroup;
    protected ArrayList<T> forRemoval;

    SpriteGroup() {
        spriteGroup = new HashSet<>();
        forRemoval = new ArrayList<>();
    }

    public void add(T obj) {spriteGroup.add(obj);}

    public void addAll(Collection<T> collection) {spriteGroup.addAll(collection);}

    public void remove(T obj) {
        forRemoval.add(obj);
    }

    public void updateAll() {
        for (T obj : spriteGroup) {
            obj.update();
        }
        for (T obj : forRemoval) {
            spriteGroup.remove(obj);
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
