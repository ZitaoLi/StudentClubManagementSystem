package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.ImgSelectorPopupWindow;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.UserIdList;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.ImageHandleUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.gc.materialdesign.views.ButtonRectangle;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityRequestActivity extends BaseActivity {

    private static final String TAG = "ActivityRequestActivity";
    private int mYear, mMonth, mDay;
    private StringBuilder mDate;
    private Activity mActivity;
    private ViewHolder mHolder;
    private int mClubId;
    private int mUserId;
    private String mUrlPrefix;
    private String mBase64Image;
    private Uri mUri;
    private ImgSelectorPopupWindow mPopupWindow;

    public ActivityRequestActivity() {
        mActivity = this;
        mDate = new StringBuilder();
        mBase64Image = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_request);

        initDateTime();
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

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void submitData() {
        String url = mUrlPrefix + "/controller/UserRequestServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "club_activity")
                .add("club_id", String.valueOf(mClubId))
                .add("user_id", String.valueOf(mUserId))
                .add("title", mHolder.title.getText().toString())
                .add("time", mDate.toString())
                .add("place", mHolder.place.getText().toString())
                .add("content", mHolder.content.getText().toString())
                .add("base64_img", mBase64Image)
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_request_toolbar);
        MyTextInputLayout timeSelector = (MyTextInputLayout) findViewById(R.id.activity_request_when);
        EditText title = ((MyTextInputLayout) findViewById(R.id.activity_request_what)).getEditText();
        EditText place = ((MyTextInputLayout) findViewById(R.id.activity_request_where)).getEditText();
        EditText content = ((MyTextInputLayout) findViewById(R.id.activity_request_content)).getEditText();
        ButtonRectangle uploadImgBtn = (ButtonRectangle) findViewById(R.id.activity_request_upload_img_btn);
        FloatingActionButton submitBtn = (FloatingActionButton) findViewById(R.id.activity_request_commit_btn);
        ZLoadingDialog dialog = new ZLoadingDialog(mActivity);

        private ViewHolder() {
            title.setText("关于雨无声社团迎新活动申请一事");
            place.setText("6B小广场前");
            content.setText("雨无声社团20xx年xx月xx日将于西校园6B小广场前举办迎新活动。");

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("活动申请");
            }
            timeSelector.setOnTouchListener(new OnTouchListenerImpl());
            uploadImgBtn.setOnClickListener(new OnClickListenerImpl());
            submitBtn.setOnClickListener(new OnClickListenerImpl());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    LogUtil.d(TAG, "take photo success");
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                        mBase64Image = ImageHandleUtil.bitmapToBase64(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    if (Build.VERSION.SDK_INT >= 19) {
                        imagePath = ImageHandleUtil.handleImageOnKatKit(data, this);
                    } else {
                        imagePath = ImageHandleUtil.handleImageBeforeKatKit(data, this);
                    }
                    LogUtil.d(TAG, "imagePath: " + imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    LogUtil.d(TAG, "bitmap: " + bitmap);
                    String base64 = ImageHandleUtil.bitmapToBase64(bitmap);
                    LogUtil.d(TAG, "base64: " + base64);
                    mBase64Image = base64;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPopupWindow.openAlbum();
                } else {
                    Toast.makeText(mActivity, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_request_upload_img_btn:
                    // TODO 读取图片
                    ImgSelectorPopupWindow popupWindow = new ImgSelectorPopupWindow(mActivity, getWindow().getDecorView());
                    mPopupWindow = popupWindow;
                    mUri = popupWindow.getImageUri();
                    break;
                case R.id.activity_request_commit_btn:
                    // TODO 上传数据
                    mHolder.dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
                            .setLoadingColor(Color.BLACK)//颜色
                            .setHintText("Loading...")
                            .show();
                    LogUtil.d(TAG, "send data");
                    submitData();
                    break;
                default:
                    break;
            }
        }
    }

    class OnTouchListenerImpl implements View.OnTouchListener, DatePicker.OnDateChangedListener {

        @Override
        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.activity_request_when:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mDate.length() > 0) { //清除上次记录的日期
                                mDate.delete(0, mDate.length());
                            }
                            mHolder.timeSelector.getEditText().setText(mDate.append(String.valueOf(mYear)).append("年").append(String.valueOf(mMonth)).append("月").append(mDay).append("日"));
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
                    View dialogView = View.inflate(mActivity, R.layout.dialog_date, null);
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














