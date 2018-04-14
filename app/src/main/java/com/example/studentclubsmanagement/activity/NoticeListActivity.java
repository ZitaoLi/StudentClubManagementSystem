package com.example.studentclubsmanagement.activity;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.studentclubsmanagement.Adapter.NoticeListRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.Notice;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoticeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("我的消息");
        }

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);
        if (userId != 0) {
            RequestBody body = new FormBody.Builder().add("user_id", String.valueOf(userId)).build();
            String url = HttpUtil.getUrlPrefix(this) + "/controller/NoticePullerServlet";
            HttpUtil.sendOkHttpRequestWithPost(url, body, new CallBackImpl());
        }
    }

    class CallBackImpl implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String responseData = response.body().string();
            if (!"".equals(responseData)) {
                // TODO 更新消息列表
                if ("0".equals(responseData)) return;
                if ("-1".equals(responseData)) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Notice> notices = GsonSingleton.getInstance().fromJson(responseData, new TypeToken<List<Notice>>(){}.getType());
                        RecyclerView noticeList = (RecyclerView) findViewById(R.id.notice_list_recycler_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(NoticeListActivity.this);
                        RecyclerView.Adapter adapter = new NoticeListRecyclerViewAdapter(NoticeListActivity.this, notices);
                        noticeList.setLayoutManager(layoutManager);
                        noticeList.setAdapter(adapter);
                    }
                });
            }
        }
    }
}
