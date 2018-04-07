package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConferenceOrganizingActivity extends BaseActivity {

    private static final String TAG = "ConferenceOrganizingActivity";
    private int mYear, mMonth, mDay;
    private StringBuilder mDate;
    private Context mContext;
    private Activity mActivity;
    private ViewHolder mHolder;
    private String mUrlPrefix;
    private int mClubId;
    private int mUserId;

    public ConferenceOrganizingActivity() {
        mContext = this;
        mDate = new StringBuilder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_organizing);

        mActivity = this;
        mHolder = new ViewHolder();
        mUrlPrefix = HttpUtil.getUrlPrefix(this);
        Intent intent = getIntent();
        int clubId = intent.getIntExtra("club_id", 0);
        mClubId = clubId;
        LogUtil.d(TAG, "club_id: " + clubId);
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        mUserId = sp.getInt("user_id", 0);
        initDateTime();
    }

    private void submitData() {
        String url = mUrlPrefix + "/controller/ClubTransactionServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "conference_organizing")
                .add("club_id", String.valueOf(mClubId))
                .add("user_id", String.valueOf(mUserId))
                .add("time", mDate.toString())
                .add("place", mHolder.place.getText().toString())
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

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    class ViewHolder {
        Toolbar toolbar = (Toolbar) findViewById(R.id.conference_organizing_toolbar);
        MyTextInputLayout time = (MyTextInputLayout) findViewById(R.id.conference_organizing_when);
        EditText title = ((MyTextInputLayout) findViewById(R.id.conference_organizing_what)).getEditText();
        EditText place = ((MyTextInputLayout) findViewById(R.id.conference_organizing_where)).getEditText();
        EditText content = ((MyTextInputLayout) findViewById(R.id.conference_organizing_content)).getEditText();
        FloatingActionButton submitBtn = (FloatingActionButton) findViewById(R.id.conference_organizing_submit_btn);
        ZLoadingDialog dialog = new ZLoadingDialog(mActivity);

        private ViewHolder() {
            title.setText("关于下次会议通知");
            place.setText("4教603");
            content.setText("会议主要围绕招新一事展开，请部员们务必准时到场，有事缺席的必须请假！");

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("会议组织");
            }
            time.setOnTouchListener(new OnClickListenerImp());
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

    class OnClickListenerImp implements View.OnTouchListener, DatePicker.OnDateChangedListener {

        @Override
        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.conference_organizing_when:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mDate.length() > 0) { //清除上次记录的日期
                                mDate.delete(0, mDate.length());
                            }
                            mHolder.time.getEditText().setText(mDate.append(String.valueOf(mYear)).append("年").append(String.valueOf(mMonth)).append("月").append(mDay).append("日"));
//                            mTimeSelector.getEditText().setText("hello");
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    View dialogView = View.inflate(mContext, R.layout.dialog_date, null);
                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    dialog.setTitle("设置日期");
                    dialog.setView(dialogView);
                    dialog.show();
                    //初始化日期监听事件
                    datePicker.init(mYear, mMonth - 1, mDay, this);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
