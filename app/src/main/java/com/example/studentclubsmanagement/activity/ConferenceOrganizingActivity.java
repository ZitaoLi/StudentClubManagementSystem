package com.example.studentclubsmanagement.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;

import com.example.studentclubsmanagement.R;

import java.util.Calendar;

public class ConferenceOrganizingActivity extends BaseActivity {

    private int mYear, mMonth, mDay;
    private StringBuilder mDate;
    private Context mContext;
    private com.example.studentclubsmanagement.customview.MyTextInputLayout mTimeSelector;

    public ConferenceOrganizingActivity() {
        mContext = this;
        mDate = new StringBuilder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_organizing);

        initToolbar();
        initView();
        initDateTime();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.conference_organizing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("会议组织");
        }
    }

    private void initView() {
        mTimeSelector = (com.example.studentclubsmanagement.customview.MyTextInputLayout) findViewById(R.id.conference_organizing_when);
        mTimeSelector.setOnTouchListener(new OnClickListenerImp());
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
                            mTimeSelector.getEditText().setText(mDate.append(String.valueOf(mYear)).append("年").append(String.valueOf(mMonth)).append("月").append(mDay).append("日"));
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
