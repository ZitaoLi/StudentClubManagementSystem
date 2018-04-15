package com.example.studentclubsmanagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.ClubListActivity;
import com.example.studentclubsmanagement.activity.NoticeListActivity;
import com.example.studentclubsmanagement.activity.UserInfoActivity;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.User;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";
    private String mUrlPrefix;
    private int mUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        initMineNavigationView(view);
        return view;
    }

    private void initMineNavigationView(View view) {
        initBriefUserInfo(view);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.mine_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.mine_nav_my_info:
                        intent = new Intent(getActivity(), UserInfoActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.mine_nav_my_club:
                        intent = new Intent(getActivity(), ClubListActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.mine_nav_my_msg:
                        intent = new Intent(getActivity(), NoticeListActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.mine_nav_my_collection:
                        return true;
                    case R.id.mine_nav_help_and_feedback:
                        return true;
                }
                return false;
            }
        });
    }

    private void initBriefUserInfo(final View view) {
        SharedPreferences sp = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        mUserId = sp.getInt("user_id", 1);
        LogUtil.d(TAG, "user_id: " + mUserId);
        mUrlPrefix = HttpUtil.getUrlPrefix(this.getActivity());
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
                    showResponseData(view, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showResponseData(final View view, final String json) {
        if ("".equals(json) || json == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView userName = (TextView) view.findViewById(R.id.mine_fragment_user_name);
                ImageView userHeader = (ImageView) view.findViewById(R.id.mine_fragment_user_header);

                Gson gson = GsonSingleton.getInstance();
                User user = gson.fromJson(json, User.class);

                userName.setText(user.getName());
                String headerImagePath = user.getHeaderImagePath();
                if (!"".equals(headerImagePath)){
                    LogUtil.d(TAG, "load:" + mUrlPrefix + headerImagePath);
                    Glide.with(getActivity()).load(mUrlPrefix + headerImagePath).into(userHeader);
                }
            }
        });
    }
}
