package com.example.studentclubsmanagement.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studentclubsmanagement.Adapter.SquareRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubIdList;
import com.example.studentclubsmanagement.gson.News;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.example.studentclubsmanagement.util.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class SquareFragment extends BaseFragment {

    private static final String TAG = "SquareFragment";
    private SquareRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private List<News> mSquareContentList;
    private Activity mActivity;
    private View mView;
    private String mUrlPrefix;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_square, container, false);
        mView = view;
        mActivity = this.getActivity();
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);

        loadPage();

        // TODO 下拉刷新
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

    private void loadPage() {
        String url = mUrlPrefix + "/controller/NewsPullerServlet";
        HttpUtil.sendOkHttpRequestWithGet(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "get success");
                String responseData = response.body().string();
                LogUtil.d(TAG, responseData);
                initSquareContentList(responseData);
            }
        });
    }

    private void initSquareContentList(final String responseData) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSquareContentList = new Gson().fromJson(responseData, new TypeToken<List<News>>(){}.getType());
                RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
                recyclerView.setLayoutManager(layoutManager);
                SquareRecyclerViewAdapter adapter = new SquareRecyclerViewAdapter(mActivity, mSquareContentList);
                mAdapter = adapter;
                recyclerView.setAdapter(adapter);
            }
        });
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
                        loadPage();
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
