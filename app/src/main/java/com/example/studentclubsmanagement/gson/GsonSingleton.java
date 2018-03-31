package com.example.studentclubsmanagement.gson;

import com.google.gson.Gson;

/**
 * Created by 李子韬 on 2018/3/30.
 */

public class GsonSingleton {

    private static class SingletonHolder {
        private static final Gson INSTANCE = new Gson();
    }
    private GsonSingleton (){}
    public static final Gson getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
