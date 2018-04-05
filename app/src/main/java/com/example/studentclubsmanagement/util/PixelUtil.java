package com.example.studentclubsmanagement.util;

import android.content.Context;

/**
 * Created by 李子韬 on 2018/3/23.
 */

public class PixelUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  。
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 。
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}