package com.example.studentclubsmanagement.fragment;

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
import android.widget.Toast;

import com.example.studentclubsmanagement.R;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        initMineNavigationView(view);
        return view;
    }

    private void initMineNavigationView(final View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.mine_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mine_nav_my_info:
                        return true;
                    case R.id.mine_nav_my_club:
                        return true;
                    case R.id.mine_nav_my_msg:
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
}
