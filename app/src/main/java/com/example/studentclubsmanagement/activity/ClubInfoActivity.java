package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.ImmersionUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubInfoActivity extends BaseActivity {

    public static final String TAG = "ClubInfoActivity";
    private int mClubId;
    private Activity mActivity;
    private String mUrlPrefix;

    public ClubInfoActivity() {
        mActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info);

        ImmersionUtil.setImmersion(this);

//        initToolbar();
        initClubId();
        loadPage();
    }

    private void initClubId() {
        Intent intent = getIntent();
        mClubId = intent.getIntExtra("club_id", 13);
    }

    private void loadPage() {
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);
        String url = mUrlPrefix + "/controller/ClubInfoServlet";
        RequestBody body = new FormBody.Builder().add("club_id", String.valueOf(mClubId)).build();
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
                    HttpUtil.showToastOnUI(mActivity, "加载页面失败");
                    e.printStackTrace();
                }
            }
        });
    }

    private void showResponseData(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toolbar toolbar = (Toolbar) findViewById(R.id.club_info_toolbar);
                ImageView clubInfoBgImage = (ImageView) findViewById(R.id.club_info_bg_image);
                TextView clubName = (TextView) findViewById(R.id.club_info_club_name);
                TextView clubInfo = (TextView) findViewById(R.id.club_info_club_info);
                TextView clubMemberNum = (TextView) findViewById(R.id.club_info_club_member_num);
                TextView clubAge = (TextView) findViewById(R.id.club_info_club_age);

                Gson gson = GsonSingleton.getInstance();
                LogUtil.d(TAG, json);
                Club club = gson.fromJson(json, Club.class);
                LogUtil.d(TAG, club.toString());

                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle(club.getClubName());
                }
                String imagePath = club.getClubBgImagePath();
                if (!"".equals(imagePath)){
                    LogUtil.d(TAG, "load:" + mUrlPrefix + imagePath);
                    Glide.with(mActivity).load(mUrlPrefix + imagePath).into(clubInfoBgImage);
                } else {
                    // TODO 设置默认社团背景
//                    clubInfoBgImage.setImageResource(R.drawable.default_user);
                }
                clubName.setText(club.getClubName());
                clubInfo.setText(club.getClubInfo());
                clubMemberNum.setText(String.valueOf(club.getMemberNum()));
                clubAge.setText(String.valueOf(club.getLifeTime()));
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
}
