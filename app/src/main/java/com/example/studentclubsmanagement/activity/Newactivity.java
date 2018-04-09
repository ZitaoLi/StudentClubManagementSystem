package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.ImageViewer;
import com.example.studentclubsmanagement.gson.ClubMember;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.News;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.ImmersionUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Newactivity extends BaseActivity {

    private static final String TAG = "Newactivity";
    private int mNewsId;
    private ViewHolder mHolder;
    private Activity mActivity;
    private String mUrlPrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newactivity);

        ImmersionUtil.setImmersion(this);

        Intent intent = getIntent();
        mNewsId = intent.getIntExtra("news_id", 0);
        LogUtil.d(TAG, "news_id: " + mNewsId);

        mActivity = this;
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);
        mHolder = new ViewHolder();

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.new_collapsing_toolbar);
//        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.new_floating_action_button);
//        TextView textView = (TextView) findViewById(R.id.new_text_view);
//        ImageView imageView = (ImageView) findViewById(R.id.new_image_view);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.new_toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        collapsingToolbarLayout.setTitle("Hello");
//        textView.setText("nullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnull");
//        Glide.with(this).load(R.drawable.test_img).into(imageView);
//
//        floatingActionButton.setOnClickListener(this);
        loadPage();
    }

    private void loadPage() {
        String url = mUrlPrefix + "/controller/NewsPullerServlet";
        RequestBody body = new FormBody.Builder().add("news_id", String.valueOf(mNewsId)).build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String responseData = response.body().string();
                LogUtil.d(TAG, responseData);
                initNews(responseData);
            }
        });
    }

    private void initNews(final String responseData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                News news = GsonSingleton.getInstance().fromJson(responseData, News.class);
                mHolder.collapsingToolbarLayout.setTitle(news.getClubName());
                mHolder.content.setText(news.getContent());
                mHolder.title.setText(news.getTitle());
                mHolder.userName.setText(news.getUserName());
                mHolder.createdTime.setText(TimeUtil.timestamp2string(news.getCreatedTime()));
                String  additionalImagePath = news.getImagePath();
                if (!"".equals(additionalImagePath)) {
                    Glide.with(mActivity).load(mUrlPrefix + news.getImagePath()).into(mHolder.additionalImage);
                    mHolder.additionalImage.setTag(news.getImagePath());
                }
                String userHeaderImagePath = news.getUserHeaderImagePath();
                if (!"".equals(userHeaderImagePath)) {
                    Glide.with(mActivity).load(mUrlPrefix + userHeaderImagePath).into(mHolder.userHeaderImage);
                }
                loadClubBgImage(news.getClubId());
            }
        });
    }

    private void loadClubBgImage(int clubId) {
        // TODO 下载社团背景图片
        String url = mUrlPrefix + "/controller/ImageLoaderServlet";
        RequestBody body = new FormBody.Builder().add("id", String.valueOf(clubId)).add("img_type", "club_bg_img").build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                final String responseData = response.body().string();
                LogUtil.d(TAG, responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 将社团背景图片加载到UI
                        if (!"".equals(responseData)) {
                            Glide.with(mActivity).load(mUrlPrefix + responseData).into(mHolder.clubBgImage);
                        }
                        mHolder.clubBgImage.setTag(responseData);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ViewHolder {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.new_collapsing_toolbar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.new_floating_action_button);
        TextView title = (TextView) findViewById(R.id.news_title);
        TextView content = (TextView) findViewById(R.id.news_content);
        TextView userName = (TextView) findViewById(R.id.news_user_name);
        TextView createdTime = (TextView) findViewById(R.id.news_created_time);
        ImageView additionalImage = (ImageView) findViewById(R.id.news_additional_image);
        ImageView userHeaderImage = (ImageView) findViewById(R.id.news_user_head_image);
        ImageView clubBgImage = (ImageView) findViewById(R.id.news_club_bg_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);

        private ViewHolder() {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            floatingActionButton.setOnClickListener(new OnClickListenerImpl());
            clubBgImage.setOnClickListener(new OnClickListenerImpl());
            additionalImage.setOnClickListener(new OnClickListenerImpl());
        }
    }

    class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_floating_action_button:
                    Toast.makeText(mActivity, "news_id:" + mNewsId, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.news_club_bg_image:
                    // TODO 点击加载大图
                    String url = (String) view.getTag();
                    LogUtil.d(TAG, "url: " + url);
                    ImageViewer.Builder builder = new ImageViewer.Builder(getBaseContext());
                    ImageViewer imageViewer = builder.setUrl(url).build();
                    imageViewer.show(getSupportFragmentManager(), "image_viewer");
                    break;
                case R.id.news_additional_image:
                    // TODO 点击加载大图
                    url = (String) view.getTag();
                    LogUtil.d(TAG, "url: " + url);
                    builder = new ImageViewer.Builder(getBaseContext());
                    imageViewer = builder.setUrl(url).build();
                    imageViewer.show(getSupportFragmentManager(), "image_viewer");
                    break;
                default:
                    break;
            }
        }
    }
}
