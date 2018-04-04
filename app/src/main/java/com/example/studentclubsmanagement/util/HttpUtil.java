package com.example.studentclubsmanagement.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 李子韬 on 2018/3/25.
 */

public class HttpUtil {

    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
//    public static final MediaType JSON  = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static void sendOkHttpRequestWithGet(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestWithPost(String address, String json, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        LogUtil.d("HttpUtil", "json: " + json);
        Request request = new Request.Builder().url(address).addHeader("content-type", "application/json;charset:utf-8").post(body).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestWithPost(String address, RequestBody body, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    public static String getUrlPrefix(Context context) {
        Resources res = context.getResources();
        String server = res.getString(R.string.server);
        String rootPath = res.getString(R.string.root_path);

        return server + rootPath;
    }

    public static void showToastOnUI(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}



















