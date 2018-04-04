package com.example.studentclubsmanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SquareRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_square, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);
        SquareRecyclerViewAdapter adapter = new SquareRecyclerViewAdapter(inflater.getContext(), null);
        mAdapter = adapter;
        recyclerView.setAdapter(adapter);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.square_swipe_refresh_layout);
        mRefreshLayout = refreshLayout;
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSquareContent();
            }
        });

        return view;
    }

    private void refreshSquareContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO 刷新页面逻辑
                try {
                    Thread.sleep(2000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
