package com.example.studentclubsmanagement.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.PixelUtil;

/**
 * Created by 李子韬 on 2018/3/23.
 */

public class MyTextInputLayout extends LinearLayout {

    private Context mContext;
    private AttributeSet mAttrs;
    private EditText mEditText;

    public MyTextInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    public EditText getEditText() {
        return mEditText;
    }

    private void init() {
        TypedArray array = mContext.obtainStyledAttributes(mAttrs, R.styleable.MyTextInputLayout);
        String hintValue = array.getString(R.styleable.MyTextInputLayout_hintValue);
        int imageResourceValue = array.getResourceId(R.styleable.MyTextInputLayout_imageResourceValue, R.drawable.ic_mood_black_24dp);
        int linesNumValue = array.getInt(R.styleable.MyTextInputLayout_linesNumValue, 1);
        boolean focusable = array.getBoolean(R.styleable.MyTextInputLayout_focusable, true);
        String text = array.getString(R.styleable.MyTextInputLayout_text);
        LinearLayout.LayoutParams params;

        android.support.design.widget.TextInputLayout textInputLayout = new android.support.design.widget.TextInputLayout(mContext);
        ImageView imageView = new ImageView(mContext);
        mEditText = new EditText(mContext);

        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(params);

        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, PixelUtil.dip2px(mContext,20), 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setImageResource(imageResourceValue);
        imageView.setLayoutParams(params);

        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mEditText.setHint(hintValue);
        // if (text != null || text != "") editText.setText(text);
        mEditText.setLines(linesNumValue);
        mEditText.setFocusable(focusable);
        mEditText.setFocusableInTouchMode(focusable);
        mEditText.setLayoutParams(params);

        textInputLayout.addView(mEditText);
        this.addView(imageView);
        this.addView(textInputLayout);

        array.recycle();
    }
}













