package com.example.studentclubsmanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentclubsmanagement.Adapter.SquareRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.util.MyApplication;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class SquareFragment extends BaseFragment {

    private static final String TAG = "SquareFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_square, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);
        SquareRecyclerViewAdapter adapter = new SquareRecyclerViewAdapter(inflater.getContext(), null);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
