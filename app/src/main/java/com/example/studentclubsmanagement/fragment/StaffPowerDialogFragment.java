package com.example.studentclubsmanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;

/**
 * Created by 李子韬 on 2018/3/24.
 */

public class StaffPowerDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View innerView = initInnerView();
        builder.setTitle("更改干事权限")
                .setView(innerView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    private View initInnerView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout.LayoutParams params;

        LinearLayout itemGroup = new LinearLayout(getContext());
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemGroup.setLayoutParams(params);
        itemGroup.setOrientation(LinearLayout.VERTICAL);

        View item1 = inflater.inflate(R.layout.textview_switch_item, null);
        item1.setLayoutParams(params);
        View item2 = inflater.inflate(R.layout.textview_switch_item, null);
        item2.setLayoutParams(params);

        itemGroup.addView(item1);
        itemGroup.addView(item2);

        return itemGroup;
    }
}
