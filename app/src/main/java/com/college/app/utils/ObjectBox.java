package com.college.app.utils;

import android.content.Context;

import com.college.app.MyObjectBox;

import io.objectbox.BoxStore;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
