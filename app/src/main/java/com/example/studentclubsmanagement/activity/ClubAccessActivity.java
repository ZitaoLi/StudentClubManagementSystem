package com.example.studentclubsmanagement.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubAccessActivity extends BaseActivity {

    private static final String TAG = "ClubAccessActivity";
    private Uri mUri;
    private ImgSelectorPopupWindow mPopupWindow;
    private String mUrlPrefix;
    private String mBase64Image;
    private int mTransactionId;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_access);

        mUrlPrefix = HttpUtil.getUrlPrefix(this);
        mBase64Image = "";
        mActivity = this;
        initToolbar();

        ButtonRectangle uploadImgBtn = (ButtonRectangle) findViewById(R.id.club_access_upload_img_btn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgSelectorPopupWindow popupWindow = new ImgSelectorPopupWindow(ClubAccessActivity.this, getWindow().getDecorView());
                mPopupWindow = popupWindow;
                mUri = popupWindow.getImageUri();
            }
        });
        FloatingActionButton commitBtn = (FloatingActionButton) findViewById(R.id.club_access_commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitForm();
            }
        });
    }

    private void commitForm() {
        ((MyTextInputLayout) findViewById(R.id.club_access_club_name)).getEditText().setText("雨无声");
        ((MyTextInputLayout) findViewById(R.id.club_access_title)).getEditText().setText("我希望加入雨无声");
        ((MyTextInputLayout) findViewById(R.id.club_access_content)).getEditText().setText("我会编程技术，我想加入雨无声校园网。");

        final String clubName = String.valueOf(((MyTextInputLayout) findViewById(R.id.club_access_club_name)).getEditText().getText());
        String title = String.valueOf(((MyTextInputLayout) findViewById(R.id.club_access_title)).getEditText().getText());
        String content = String.valueOf(((MyTextInputLayout) findViewById(R.id.club_access_content)).getEditText().getText());

        SharedPreferences sp = this.getSharedPreferences("data", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);

        String url = mUrlPrefix + "/controller/UserRequestServlet";
        RequestBody body = new FormBody.Builder()
                .add("type", "club_access")
                .add("user_id", String.valueOf(userId))
                .add("club_name", clubName)
                .add("title", title)
                .add("content", content)
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String code = response.body().string();
                int res = -1;
                if (!"".equals(code)) {
                    res = Integer.parseInt(code);
                }
                if (res == 1) {
                    // 成功
                    HttpUtil.showToastOnUI(mActivity, "添加成功");
                } else if (res == 0) {
                    // 社团不存在
                    HttpUtil.showToastOnUI(mActivity, "该社团不存在");
                } else if (res == -1) {
                    // 申请失败
                    HttpUtil.showToastOnUI(mActivity, "申请失败，请重试");
                }
                finish();
//                mTransactionId = clubInternalTransactionId;
                // TODO 上传图片
//                uploadImage(mBase64Image, clubInternalTransactionId);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.club_access_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("入团申请");
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
                        // TODO
                        imagePath = ImageHandleUtil.handleImageOnKatKit(data, this);
                    } else {
                        // TODO
                        imagePath = ImageHandleUtil.handleImageBeforeKatKit(data, this);
                    }
                    LogUtil.d(TAG, "imagePath: " + imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    LogUtil.d(TAG, "bitmap: " + bitmap);
                    String base64 = ImageHandleUtil.bitmapToBase64(bitmap);
                    LogUtil.d(TAG, "base64: " + base64);
                    mBase64Image = base64;
//                    uploadImage(base64);
                }
                break;
            default:
                break;
        }
    }

    private void uploadImage(String base64, int transactionId) {
        String url = mUrlPrefix + "/controller/ImageUploaderServlet";;
        RequestBody body = new FormBody.Builder()
                .add("base64_img", base64)
                .add("type", "club_access")
                .add("club_internal_transaction_id", String.valueOf(mTransactionId))
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO
                    mPopupWindow.openAlbum();
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}






