package com.example.studentclubsmanagement.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestDataAPIActivity extends AppCompatActivity {

    private final static String TAG = "TestDataAPIActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data_api);

        textView = (TextView) findViewById(R.id.test_data_api_test_view);

        Resources res = this.getResources();
        String server = res.getString(R.string.server);
        String rootPath = res.getString(R.string.root_path);

        String url = server + rootPath + "/controller/TestServlet";
        HttpUtil.sendOkHttpRequestWithGet(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtil.d(TAG, "get success");
//                showResponseData(json);
            }
        });
        url = server + rootPath + "/controller/ClubInfoServlet";
//        String json = "{\"club_id\":13}";
//        LogUtil.d(TAG, "json: " + json);
//        HttpUtil.sendOkHttpRequestWithPost(url, json, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d(TAG, e.toString());
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                LogUtil.d(TAG, "post success");
//                showResponseData(json);
//            }
//        });
        RequestBody body = new FormBody.Builder().add("club_id", "13").build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtil.d(TAG, "post success");
                showResponseData(json);
            }
        });
    }

    private void showResponseData(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(json);
            }
        });
    }
}













