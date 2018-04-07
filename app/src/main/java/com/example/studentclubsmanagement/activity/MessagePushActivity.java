package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagePushActivity extends BaseActivity {

    private static final String TAG = "MessagePushActivity";
    private int mClubId;
    private int mUserId;
    private String mUrlPrefix;
    private Activity mActivity;
    private ViewHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_push);

        mActivity = this;
        mHolder = new ViewHolder();
        mUrlPrefix = HttpUtil.getUrlPrefix(this);
        Intent intent = getIntent();
        int clubId = intent.getIntExtra("club_id", 0);
        mClubId = clubId;
        LogUtil.d(TAG, "club_id: " + clubId);
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        mUserId = sp.getInt("user_id", 0);
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

    private void submitData() {
        String url = mUrlPrefix + "/controller/ClubTransactionServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "message_push")
                .add("club_id", String.valueOf(mClubId))
                .add("user_id", String.valueOf(mUserId))
                .add("title", mHolder.title.getText().toString())
                .add("content", mHolder.content.getText().toString())
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mHolder.dialog.cancel();
                    }
                });
                LogUtil.d(TAG, "post success");
                String responseData = response.body().string();
                LogUtil.d(TAG, "responseData: " + responseData);
                if(!"".equals(responseData)) {
                    finish();
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
    }

    class ViewHolder {
        Toolbar toolbar = (Toolbar) findViewById(R.id.message_push_toolbar);
        EditText title = ((MyTextInputLayout) findViewById(R.id.mes_push_title)).getEditText();
        EditText content = ((MyTextInputLayout) findViewById(R.id.mes_push_content)).getEditText();
        ZLoadingDialog dialog = new ZLoadingDialog(mActivity);

        FloatingActionButton submitBtn = (FloatingActionButton) findViewById(R.id.msg_push_submit_btn);
        public ViewHolder() {
            title.setText("关于周二例会通知");
            content.setText("请部员们记得本周二在老地方例行开一次会。");

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("消息推送");
            }

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHolder.dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
                            .setLoadingColor(Color.BLACK)//颜色
                            .setHintText("Loading...")
                            .show();
                    LogUtil.d(TAG, "send data");
                    submitData();
                }
            });
        }

    }
}