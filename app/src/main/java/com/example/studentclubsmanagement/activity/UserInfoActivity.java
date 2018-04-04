package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.User;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;
import com.google.gson.Gson;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";
    private int mUserId;
    private String mUrlPrefix;
    private Activity mActivity;
    private ViewHolder mHolder;

    public UserInfoActivity() {
        mActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mHolder = new ViewHolder();
        initUserId();
        loadPage();
    }

    private void initUserId() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        mUserId = sp.getInt("user_id", 1);
    }

    private void loadPage() {
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);
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
                }
            }
        });
    }

    private void showResponseData(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Gson gson = GsonSingleton.getInstance();
                LogUtil.d(TAG, json);
                User user = gson.fromJson(json, User.class);
                LogUtil.d(TAG, user.toString());

                setSupportActionBar(mHolder.toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle("个人信息");
                }
                String headerImagePath = user.getHeaderImagePath();
                if (!"".equals(headerImagePath)){
                    LogUtil.d(TAG, "load:" + mUrlPrefix + headerImagePath);
                    Glide.with(mActivity).load(mUrlPrefix + headerImagePath).into(mHolder.userHeader);
                }
                mHolder.userName.setText(user.getName());
                mHolder.studentCode.setText(user.getStudentCode());
                mHolder.userSelfIntroduction.setText(user.getSelfIntroduction());
                mHolder.userPhone.setText(user.getPhoneNumber());
                mHolder.studentCode.setText(user.getStudentCode());
                mHolder.userEmail.setText(user.getEmail());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ViewHolder {
        CircleImageView userHeader = (CircleImageView) findViewById(R.id.user_info_user_header);
        TextView userName = (TextView) findViewById(R.id.user_info_user_name);
        TextView studentCode = (TextView) findViewById(R.id.user_info_student_code);
        TextView userSelfIntroduction = (TextView) findViewById(R.id.user_info_self_introduction);
        TextView userPhone = (TextView) findViewById(R.id.user_info_phone);
        TextView userEmail = (TextView) findViewById(R.id.user_info_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_toolbar);
    }
}
