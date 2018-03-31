package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;

/**
 * Created by 李子韬 on 2018/3/23.
 */

public class SpinnerArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mData;

    public SpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull String[] data) {
        super(context, resource, data);
        mContext = context;
        mData = data;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(mData[position]);
        textView.setTextSize(22f);
        textView.setTextColor(Color.RED);

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(mData[position]);
        textView.setTextSize(18f);
        textView.setTextColor(Color.GRAY);

        return convertView;
    }
}













