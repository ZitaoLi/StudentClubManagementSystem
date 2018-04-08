package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.studentclubsmanagement.Adapter.ClubMemberRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubMember;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.UserIdList;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.TimeUtil;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MemberDeletionActivity extends BaseActivity {

    private static final String TAG = "MemberDeletionActivity";
    private String mUrlPrefix;
    private int mClubId;
    private Activity mActivity;
    private ViewHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_deletion);
        mActivity = this;
        mHolder = new ViewHolder();
        mUrlPrefix = HttpUtil.getUrlPrefix(this);

        Intent intent = getIntent();
        int clubId = intent.getIntExtra("club_id", 0);
        mClubId = clubId;
        LogUtil.d(TAG, "club_id: " + clubId);

        String url = mUrlPrefix + "/controller/UserClubRelationServlet";
        RequestBody body = new FormBody.Builder()
                .add("club_id", String.valueOf(clubId))
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
                if(!"".equals(responseData)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        UserIdList userIdList = GsonSingleton.getInstance().fromJson(responseData, UserIdList.class);
                        LogUtil.d(TAG, "userIdList: " + userIdList);
                        int[] userIdArray = userIdList.getUserIdArray();
                        LogUtil.d(TAG, "clubIdArray: " + userIdArray);
                        List<String> data = new ArrayList<String>();
                        for (int i = 0; i < userIdArray.length; i++) {
                            data.add(String.valueOf(userIdArray[i]));
                        }
                        loadPage(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
    }

    private void loadPage(List<String> userIdList) {
        String url = mUrlPrefix + "/controller/MultiClubMemberInfoServlet";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (int i = 0; i < userIdList.size(); i++) {
            bodyBuilder.add("user_id_list", userIdList.get(i));
        }
        bodyBuilder.add("club_id", String.valueOf(mClubId));
        RequestBody requestBody = bodyBuilder.build();
        HttpUtil.sendOkHttpRequestWithPost(url, requestBody, new okhttp3.Callback() {
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
                    final List<ClubMember> clubMembers = GsonSingleton.getInstance().fromJson(responseData, new TypeToken<List<ClubMember>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
                            mHolder.recyclerView.setLayoutManager(layoutManager);
                            ClubMemberRecyclerViewAdapter adapter = new ClubMemberRecyclerViewAdapter(mActivity, true, clubMembers);
                            mHolder.recyclerView.setAdapter(adapter);
                        }
                    });
                } else {
                    HttpUtil.showToastOnUI(mActivity, "请求数据出错");
                }
            }
        });
    }

    private void submitData() {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.member_deletion_toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.member_deletion_recycler_view);
        FloatingActionButton submitBtn = (FloatingActionButton) findViewById(R.id.member_deletion_submit_btn);

        ZLoadingDialog dialog = new ZLoadingDialog(mActivity);
        private ViewHolder() {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("成员删除");
            }
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 上传数据
                    mHolder.dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
                            .setLoadingColor(Color.BLACK)//颜色
                            .setHintText("Loading...")
                            .show();
                    LogUtil.d(TAG, "send data");
                    submitData();
                    // TODO 睡5000ms
                    TimeUtil.sleep(5000, new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            mHolder.dialog.cancel();
                            finish();
                            return null;
                        }
                    });

//                    Timer timer = new Timer();
//                    TimerTask timerTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            mHolder.dialog.cancel();
//                            finish();
//                        }
//                    };
//                    timer.schedule(timerTask, 1000);
                }
            });
        }

    }
}
