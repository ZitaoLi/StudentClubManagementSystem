package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.studentclubsmanagement.Adapter.MessageWallRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubInternalTransaction;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageWallActivity extends BaseActivity {

    private static final String TAG = "MessageWallActivity";
    private ViewHolder mHolder;
    private Activity mActivity;
    private String mUrlPrefix;
    private int mClubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_wall);

        mActivity = this;
        mHolder = new ViewHolder();
        mUrlPrefix = HttpUtil.getUrlPrefix(this);
        Intent intent = getIntent();
        int clubId = intent.getIntExtra("club_id", 0);
        mClubId = clubId;
        LogUtil.d(TAG, "club_id: " + clubId);

        loadPage();
    }

    private void loadPage() {
        String url = mUrlPrefix + "/controller/MessageWallServlet";
        RequestBody body = new FormBody.Builder()
                .add("club_id", String.valueOf(mClubId))
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String responseData = response.body().string();
                LogUtil.d(TAG, "responseData: " + responseData);
                if(!"".equals(responseData) && responseData != null) {
                    final List<ClubInternalTransaction> clubInternalTransactions = GsonSingleton.getInstance().fromJson(responseData, new TypeToken<List<ClubInternalTransaction>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                            mHolder.recyclerView.setLayoutManager(layoutManager);
                            MessageWallRecyclerViewAdapter adapter = new MessageWallRecyclerViewAdapter(mActivity, clubInternalTransactions);
                            mHolder.recyclerView.setAdapter(adapter);
                        }
                    });
                } else {
                    HttpUtil.showToastOnUI(mActivity, "请求数据出错");
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

    class ViewHolder {
        Toolbar toolbar = (Toolbar) findViewById(R.id.message_wall_toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.message_wall_recycler_view);

        private ViewHolder() {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("消息墙");
            }
        }
    }
}
