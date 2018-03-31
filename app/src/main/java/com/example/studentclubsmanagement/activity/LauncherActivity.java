package com.example.studentclubsmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentclubsmanagement.Adapter.LauncherPageFragmentPagerAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LauncherActivity extends BaseActivity {

//    private TabLayout mTabLayout;
//    private ViewPager mViewPager;
//    private LauncherPageFragmentPagerAdapter launcherPageFragmentPagerAdapter;
//
//    private TabLayout.Tab signIn;
//    private TabLayout.Tab singUp;
    private static final String TAG = "LauncherActivity";
    private AppBarLayout mAppBarLayout;
    private ViewHolder mHolder;
    private String mUrlPrefix;
    private Context mContext;

    public LauncherActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mHolder = new ViewHolder();

//        mViewPager= (ViewPager) findViewById(R.id.launcher_page_view_pager);
//        launcherPageFragmentPagerAdapter = new LauncherPageFragmentPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(launcherPageFragmentPagerAdapter);
//
//        mTabLayout = (TabLayout) findViewById(R.id.launcher_page_tab_layout);
//        mTabLayout.setupWithViewPager(mViewPager);
//
//        signIn = mTabLayout.getTabAt(0);
//        singUp = mTabLayout.getTabAt(1);
//
//        signIn.setIcon(R.drawable.ic_mood_black_24dp);
//        singUp.setIcon(R.drawable.ic_mood_black_24dp);
    }

    class ViewHolder {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.launcher_page_app_bar);
        ImageView bgImage = (ImageView) findViewById(R.id.launcher_page_bg_image);
        CardView cardView = (CardView) findViewById(R.id.launcher_page_card_view);
        MyTextInputLayout textInputLayout1 = (MyTextInputLayout) findViewById(R.id.launcher_page_student_code);
        MyTextInputLayout textInputLayout2 = (MyTextInputLayout) findViewById(R.id.launcher_page_password);
        ButtonRectangle signInButton = (ButtonRectangle) findViewById(R.id.launcher_page_sign_in_btn);
        ButtonFlat signUpButton = (ButtonFlat) findViewById(R.id.launcher_page_sign_up_btn);

        private ViewHolder() {
            textInputLayout1.getEditText().setText("1407300306");
            textInputLayout2.getEditText().setText("lztao");

            cardView.setOnClickListener(new OnClickListenerImpl());
            signInButton.setOnClickListener(new OnClickListenerImpl());
            signUpButton.setOnClickListener(new OnClickListenerImpl());
            signUpButton.setOnClickListener(new OnClickListenerImpl());
        }
//        private void bindOnClickListener() {
//            cardView.setOnClickListener(new OnClickListenerImpl());
//            signInButton.setOnClickListener(new OnClickListenerImpl());
//            signUpButton.setOnClickListener(new OnClickListenerImpl());
//        }
    }

    class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.launcher_page_card_view:
                    mHolder.appBarLayout.setExpanded(false);
                    break;
                case R.id.launcher_page_sign_in_btn:
                    mUrlPrefix = HttpUtil.getUrlPrefix(mContext);
                    String url = mUrlPrefix + "/controller/UserSignInServlet";
                    String studentCode = mHolder.textInputLayout1.getEditText().getText().toString();
                    String password = mHolder.textInputLayout2.getEditText().getText().toString();
                    RequestBody body = new FormBody.Builder()
                            .add("student_code", studentCode)
                            .add("password", password)
                            .build();
                    HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int userId = Integer.parseInt(response.body().string());
                            if (userId == 0) LogUtil.d(TAG, "password no match");
                            if (userId == -1) LogUtil.d(TAG, "account no exist");
                            LogUtil.d(TAG, "post success");
                            // TODO 持久化user_id
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putInt("user_id", userId);
                            editor.apply();
                            // TODO 启动主页
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    break;
                case R.id.launcher_page_sign_up_btn:
                    Intent intent = new Intent(mContext, RegistertActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }
}
