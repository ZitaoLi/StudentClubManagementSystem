package com.example.studentclubsmanagement.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.studentclubsmanagement.fragment.SignInFragment;
import com.example.studentclubsmanagement.fragment.SignUpFragment;

/**
 * Created by 李子韬 on 2018/3/31.
 */

public class LauncherPageFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"登录", "注册"};

    public LauncherPageFragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new SignInFragment();
        } else if (position == 2) {
            return new SignUpFragment();
        }
        return new SignInFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
