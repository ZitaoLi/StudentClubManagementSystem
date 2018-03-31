package com.example.studentclubsmanagement.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
