package com.example.studentclubsmanagement.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.LogUtil;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class TestUploadImgActivity extends AppCompatActivity {

    private static final String TAG = "TestUploadImgActivity";
    public static final int TAKE_PHPOTEO = 1;
    private ImageView mPicture;
    private Uri mImageUri;
    private ButtonRectangle mUploadImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upload_img);

        mUploadImgBtn = (ButtonRectangle) findViewById(R.id.test_upload_img_btn);
        mPicture = (ImageView) findViewById(R.id.test_upload_img_image_view);

        mUploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outPutImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outPutImage.exists()) {
                        outPutImage.delete();
                    }
                    outPutImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    mImageUri = FileProvider.getUriForFile(TestUploadImgActivity.this, "com.example.studentclubsmanagement.fileprovider", outPutImage);
                } else {
                    mImageUri = Uri.fromFile(outPutImage);
                }
                // TODO launch camera
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, TAKE_PHPOTEO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHPOTEO:
                if (resultCode == RESULT_OK) {
                    try {
                        LogUtil.d(TAG, "take photo success");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        Glide.with(this).load(bytes).into(mPicture);
//                        mPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}










