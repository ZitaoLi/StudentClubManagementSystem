package com.example.studentclubsmanagement.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.Power;
import com.example.studentclubsmanagement.gson.PowerItem;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.PixelUtil;
import com.gc.materialdesign.views.Switch;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李子韬 on 2018/3/24.
 */

public class StaffPowerDialogFragment extends DialogFragment {

    private static final String TAG = "StaffPowerDialogFragment";
    private String mPower;

    public void setPower(String mPower) {
        this.mPower = mPower;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View innerView = initInnerView();
        builder.setTitle("更改干事权限")
                .setView(innerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

    private View initInnerView() {
        LogUtil.d(TAG, "power: " + mPower);
        // TODO 解析power json
        Power power = GsonSingleton.getInstance().fromJson(mPower, Power.class);
        List<PowerItem> powerItems = power.getPower();
        List<Integer> powers = new ArrayList<Integer>();
        for (int i = 0; i < powerItems.size() - 1; i++) {
            int[] arr = powerItems.get(i).getItems();
            for (int j = 0; j < arr.length; j++) {
                powers.add(arr[j]);
            }
        }
        LogUtil.d(TAG, "powers: " + powers);

        // TODO 生成布局
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout.LayoutParams params;

        LinearLayout itemGroup = new LinearLayout(getContext());
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PixelUtil.dip2px(itemGroup.getContext(), 250));
//        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemGroup.setOrientation(LinearLayout.VERTICAL);
        itemGroup.setLayoutParams(params);

        FrameLayout scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(params);
        itemGroup.addView(scrollView);

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(params);
        scrollView.addView(container);

        for (int i = 0; i < powers.size(); i++) {
            View item = inflater.inflate(R.layout.textview_switch_item, null);
            TextView powerName = item.findViewById(R.id.staff_power_name);
            Switch checkBox = item.findViewById(R.id.staff_power_switches);
            powerName.setText(String.valueOf(powers.get(i)));
            checkBox.setChecked(true);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            item.setLayoutParams(params);
            container.addView(item);

        }

        return itemGroup;
    }
}
