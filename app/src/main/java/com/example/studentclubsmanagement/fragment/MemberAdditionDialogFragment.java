package com.example.studentclubsmanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.studentclubsmanagement.Adapter.SpinnerArrayAdapter;
import com.example.studentclubsmanagement.R;

/**
 * Created by 李子韬 on 2018/4/15.
 */

public class MemberAdditionDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("成员添加");
        View view = inflater.inflate(R.layout.member_addition_dialog_inner_layout, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.member_addition_sex_spinner);
        initSpinner(spinner);
        builder.setView(view);
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        return dialog;
    }

    private void initSpinner(Spinner spinner) {
        final String[] sex = getResources().getStringArray(R.array.sex);
        ArrayAdapter<String> adapter = new SpinnerArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sex);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view.getContext(), sex[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
