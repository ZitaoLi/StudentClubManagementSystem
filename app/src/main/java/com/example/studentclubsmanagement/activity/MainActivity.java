package com.example.studentclubsmanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.fragment.DashboardFragment;
import com.example.studentclubsmanagement.fragment.MineFragment;
import com.example.studentclubsmanagement.fragment.SquareFragment;
import com.example.studentclubsmanagement.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private PageNavigationView tab;
    private View square, dashboard, mine;
    private ViewPager viewPager;
    private List<View> viewList;
    private List<Fragment> fragmentList;
    private PagerAdapter mPagerAdapter;
    private NavigationController navigationController;
    private Toolbar toolbar;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        isAdmin = sp.getBoolean("is_admin", false);
        if (!isAdmin) {
            int userId = sp.getInt("user_id", 0);
            LogUtil.d(TAG, "user_id: " + userId);
            initUserScreenSlide();
        } else {
            int adminId = sp.getInt("admin_id", 0);
            LogUtil.d(TAG, "user_id: " + adminId);
            initAdimnScreenSlide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.android_drawable_activity:
                intent = new Intent(this, AndroidDrawableActivity.class);
                startActivity(intent);
                break;
            case R.id.club_info_activity:
                intent = new Intent(this, ClubInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.test_data_api_activity:
                intent = new Intent(this, TestDataAPIActivity.class);
                startActivity(intent);
                break;
            case R.id.test_image_backward:
                intent = new Intent(this, LauncherActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initUserScreenSlide() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new SquareFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new MineFragment());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tab = (PageNavigationView) findViewById(R.id.tab);
        navigationController = tab.material()
                .addItem(android.R.drawable.ic_menu_camera, "广场")
                .addItem(android.R.drawable.ic_dialog_dialer, "仪表盘")
                .addItem(android.R.drawable.ic_menu_search, "我的")
                .build();
        navigationController.setupWithViewPager(viewPager);
    }

    private void initAdimnScreenSlide() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new SquareFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new MineFragment());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tab = (PageNavigationView) findViewById(R.id.tab);
        navigationController = tab.material()
                .addItem(android.R.drawable.ic_menu_camera, "广场")
                .addItem(android.R.drawable.ic_dialog_dialer, "仪表盘")
                .addItem(android.R.drawable.ic_menu_search, "我的")
                .build();
        navigationController.setupWithViewPager(viewPager);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void initView() {
        LayoutInflater lf = getLayoutInflater().from(MainActivity.this);
        square = lf.inflate(R.layout.fragment_square, null);
        dashboard = lf.inflate(R.layout.fragment_dashboard, null);
        mine = lf.inflate(R.layout.fragment_mine, null);

        viewList = new ArrayList<View>();
        viewList.add(square);
        viewList.add(dashboard);
        viewList.add(mine);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
}











