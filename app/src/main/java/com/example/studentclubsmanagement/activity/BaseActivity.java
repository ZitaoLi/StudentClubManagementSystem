package com.example.studentclubsmanagement.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.studentclubsmanagement.util.ActivityCollector;
import com.example.studentclubsmanagement.util.LogUtil;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        ImmersionBar.with(this).init(); //初始化，默认透明状态栏和黑色导航栏
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        LogUtil.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(TAG, "onRestart");
        ImmersionBar.with(this).destroy();
    }
}
