package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.studentclubsmanagement.Adapter.MessageWallRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.gson.ClubInternalTransaction;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubQueryActivity extends BaseActivity {

    private static final String TAG = "ClubQueryActivity";
    private String mUrlPrefix;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_query);

        mActivity = this;
        mUrlPrefix = HttpUtil.getUrlPrefix(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.club_query_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("社团查询");
        }
        final EditText searchBar = (EditText) findViewById(R.id.club_query_search_bar);
        final ImageButton searchBtn = (ImageButton) findViewById(R.id.club_query_search_btn);
        final ZLoadingDialog dialog = new ZLoadingDialog(mActivity);

        searchBar.setText("雨无声");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String clubName = searchBar.getText().toString();
                dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
                        .setLoadingColor(Color.BLACK)//颜色
                        .setHintText("Loading...")
                        .show();
                LogUtil.d(TAG, "send data");
                if (!"".equals(clubName) && clubName != null) {
                    String url = mUrlPrefix + "/controller/UserRequestServlet";
                    RequestBody body = new FormBody.Builder()
                            .add("type", "club_query")
                            .add("club_name", clubName)
                            .build();
                    HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            LogUtil.d(TAG, "post success");
                            final String responseData = response.body().string();
                            LogUtil.d(TAG, "responseData: " + responseData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.cancel();
                                    if(!"".equals(responseData) && responseData != null) {
                                        if ("0".equals(responseData)) {
                                            HttpUtil.showToastOnUI(mActivity, "查找的社团不存在");
                                        } else {
                                            Club club = GsonSingleton.getInstance().fromJson(responseData, Club.class);
                                            Intent intent = new Intent(mActivity, ClubInfoActivity.class);
                                            intent.putExtra("club_id", club.getId());
                                            startActivity(intent);
                                        }
                                    } else {
                                        HttpUtil.showToastOnUI(mActivity, "请求数据出错");
                                    }
                                }
                            });
                        }
                    });
                }
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
}
