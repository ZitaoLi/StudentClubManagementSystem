package com.example.studentclubsmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity {

    private static final String TAG = "UserInfoActivity";
    private Context mContext;
    private int mUserId;
    private String mUrlPrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    private void initUserId() {
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", 1);
    }

    private void loadPage() {
        mUrlPrefix = HttpUtil.getUrlPrefix(mContext);
        String url = mUrlPrefix + "/controller/UserInfoServlet";
        RequestBody body = new FormBody.Builder().add("user_id", String.valueOf(mUserId)).build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtil.d(TAG, "post success");
                try {
                    showResponseData(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyApplication.getContext(), "加载页面失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showResponseData(final String json) {
    }
}
