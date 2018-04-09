package com.example.studentclubsmanagement.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentclubsmanagement.Adapter.ClubManagementTransactionRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubManagementTransaction;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class AdminTransactionFragment extends BaseFragment {

    private static final String TAG = "AdminTransactionFragment";
    private List<ClubManagementTransaction> mClubManagementTransactionList;
    private String mUrlPrefix;
    private Activity mActivity;
    private View mView;
    private SwipeRefreshLayout mRefreshLayout;
    private ClubManagementTransactionRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_admin_transaction, container, false);

        mView = view;
        mActivity = getActivity();
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);

        // TODO 下拉刷新
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.square_swipe_refresh_layout);
        mRefreshLayout = refreshLayout;
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshClubManagementTransaction();
            }
        });

        loadPage();
        return view;
    }

    private void loadPage() {
        String url = mUrlPrefix + "/controller/ClubTransactionServlet";
        HttpUtil.sendOkHttpRequestWithGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO 刷新页面
                LogUtil.d(TAG, "get success");
                String responseData = response.body().string();
                LogUtil.d(TAG, "responseData" + responseData);
                initClubManagementTransactionList(responseData);
            }
        });
    }

    private void initClubManagementTransactionList(final String responseData) {
        if ("".equals(responseData) || responseData == null) {
            HttpUtil.showToastOnUI(mActivity, "当前没有新数据");
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mClubManagementTransactionList = new Gson().fromJson(responseData, new TypeToken<List<ClubManagementTransaction>>(){}.getType());
                LogUtil.d(TAG, mClubManagementTransactionList.toString());
                RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.admin_transaction_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
                recyclerView.setLayoutManager(layoutManager);
                ClubManagementTransactionRecyclerViewAdapter adapter = new ClubManagementTransactionRecyclerViewAdapter(mActivity, mClubManagementTransactionList);
                mAdapter = adapter;
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void refreshClubManagementTransaction() {
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
                        loadPage();
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
