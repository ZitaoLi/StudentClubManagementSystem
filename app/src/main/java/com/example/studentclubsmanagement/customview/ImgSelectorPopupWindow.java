package com.example.studentclubsmanagement.customview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.MainActivity;
import com.example.studentclubsmanagement.activity.TestUploadImgActivity;
import com.example.studentclubsmanagement.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.Inflater;

/**
 * Created by 李子韬 on 2018/4/5.
 */

public class ImgSelectorPopupWindow {

    private PopupWindow mPopupWindow;
    private Context mContext;
    private View mParent;
    Uri mImageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    public ImgSelectorPopupWindow(Context context, View parent) {
        mContext = context;
        mParent = parent;
        initPopupWindow();
    }

    private void initPopupWindow() {
        lightoff();
        View view = View.inflate(mContext, R.layout.img_uploading_popupwindow, null);
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        TranslateAnimation  animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        view.startAnimation(animation);

        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 拍照
                File outPutImage = new File(mContext.getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outPutImage.exists()) {
                        outPutImage.delete();
                    }
                    outPutImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    mImageUri = FileProvider.getUriForFile(mContext, "com.example.studentclubsmanagement.fileprovider", outPutImage);
                } else {
                    mImageUri = Uri.fromFile(outPutImage);
                }
                // TODO launch camera
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                ((Activity)mContext).startActivityForResult(intent, TAKE_PHOTO);

                mPopupWindow.dismiss();
                lighton();
            }
        });
        view.findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 从相册中选择相片
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
                mPopupWindow.dismiss();
                lighton();
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });
        mPopupWindow.setContentView(view);
        mPopupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.update();
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.3f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void lighton() {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 1f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        ((Activity) mContext).startActivityForResult(intent, CHOOSE_PHOTO);
    }

    public Uri getImageUri() {
        return mImageUri;
    }
}
