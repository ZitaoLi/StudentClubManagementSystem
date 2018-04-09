package com.example.studentclubsmanagement.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;

/**
 * Created by 李子韬 on 2018/4/10.
 */

public class ImageViewer extends DialogFragment {

    private String url;
    private static final String TAG = "ImageViewer";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.image_viewer, null);
        ImageView bigImage = view.findViewById(R.id.big_image);
        if (url != null && !"".equals(url)) {
            LogUtil.d(TAG, "url: " + HttpUtil.getUrlPrefix(getActivity()) + url);
            Glide.with(getActivity()).load(HttpUtil.getUrlPrefix(getActivity()) + url).into(bigImage);
        }
        builder.setView(view);

        return builder.create();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class Builder {

        private String url;
        private ImageViewer imageViewer;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            imageViewer = new ImageViewer();
        }

        public ImageViewer build() {
            return imageViewer;
        }

        public Builder setUrl(String url) {
            imageViewer.setUrl(url);
            return this;
        }
    }
}
