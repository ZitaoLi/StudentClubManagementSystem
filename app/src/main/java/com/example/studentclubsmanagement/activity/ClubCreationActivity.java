package com.example.studentclubsmanagement.activity;

import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.ImgSelectorPopupWindow;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.ImageHandleUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.gc.materialdesign.views.ButtonRectangle;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubCreationActivity extends BaseActivity {

    private static final String TAG = "ClubCreationActivity";
    private String mUrlPrefix;
    private String mBase64Image;
    private Activity mActivity;
    private ImgSelectorPopupWindow mPopupWindow;
    private Uri mUri;
    private ViewHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_creation);

        mUrlPrefix = HttpUtil.getUrlPrefix(this);
        mBase64Image = "";
        mActivity = this;
        mHolder = new ViewHolder();
    }

    private void commitForm() {
        SharedPreferences sp = this.getSharedPreferences("data", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);

        String url = mUrlPrefix + "/controller/UserRequestServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "club_creation")
                .add("submit_times", "first")
                .add("user_id", String.valueOf(userId))
                .add("club_name", mHolder.clubName.getText().toString())
                .add("title", mHolder.title.getText().toString())
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
                if (!"".equals(responseData)) {
                    int code = Integer.parseInt(responseData);
                    if (code == 0) {
                        HttpUtil.showToastOnUI(mActivity, "申请失败");
                        return;
                    } else if (code == 1) {
                        HttpUtil.showToastOnUI(mActivity, "申请成功");
                        finish();
                    }
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
//                if (!"".equals(responseData)) {
//                    int tempId = Integer.parseInt(responseData);
//                    if (tempId == 0) {
//                        HttpUtil.showToastOnUI(mActivity, "上传数据失败");
//                        return;
//                    }
//                    if (!"".equals(mBase64Image)) {
//                        // TODO 上传图片
//                        uploadImage(mBase64Image, tempId);
//                    } else {
//                        confirm(tempId);
//                    }
//                } else {
//                    HttpUtil.showToastOnUI(mActivity, "出错");
//                }
            }
        });
    }

    private void uploadImage(String base64, int tempId) {
        String url = mUrlPrefix + "/controller/ImageUploaderServlet";
        RequestBody body = new FormBody.Builder()
                .add("base64_img", base64)
                .add("temp_id", String.valueOf(tempId))
                .add("type", "club_creation")
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!"".equals(responseData)) {
                    int tempId = Integer.parseInt(responseData);
                    if (tempId == 0) {
                        HttpUtil.showToastOnUI(mActivity, "申请失败");
                        return;
                    } else if (tempId == 1) {
                        confirm(tempId);
                    }
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
    }

    private void confirm(int tempId) {
        String url = mUrlPrefix + "/controller/UserRequestServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "club_creation")
                .add("submit_times", "second")
                .add("temp_id", String.valueOf(tempId))
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!"".equals(responseData)) {
                    int resCode = Integer.parseInt(responseData);
                    if (resCode == 0) {
                        HttpUtil.showToastOnUI(mActivity, "申请失败");
                        return;
                    } else if (resCode == 1) {
                        HttpUtil.showToastOnUI(mActivity, "申请成功");
                        finish();
                    }
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
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
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                        byte[] bytes = byteArrayOutputStream.toByteArray();
//                        Glide.with(this).load(bytes).into(mPicture);
//                        mPicture.setImageBitmap(bitmap);
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
        ButtonRectangle uploadImgBtn = (ButtonRectangle) findViewById(R.id.club_creation_upload_img_btn);
        FloatingActionButton commitBtn = (FloatingActionButton) findViewById(R.id.club_creation_commit_btn);
        EditText clubName = ((MyTextInputLayout) findViewById(R.id.club_creation_club_name)).getEditText();
        EditText title = ((MyTextInputLayout) findViewById(R.id.club_creation_title)).getEditText();
        EditText content = ((MyTextInputLayout) findViewById(R.id.club_creation_content)).getEditText();
        Toolbar toolbar = (Toolbar) findViewById(R.id.club_creation_toolbar);
        ZLoadingDialog dialog = new ZLoadingDialog(mActivity);

        private ViewHolder() {
            clubName.setText("漫研社");
            title.setText("关于漫研社立社申请一事");
            content.setText("我们想成立漫研社，让志同道合的同学们一块交流。");

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("社团创建");
            }

            uploadImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImgSelectorPopupWindow popupWindow = new ImgSelectorPopupWindow(mActivity, getWindow().getDecorView());
                    mPopupWindow = popupWindow;
                    mUri = popupWindow.getImageUri();
                }
            });
            commitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHolder.dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
                            .setLoadingColor(Color.BLACK)//颜色
                            .setHintText("Loading...")
                            .show();
                    LogUtil.d(TAG, "send data");
                    commitForm();
                }
            });
        }
    }
}
