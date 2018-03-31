package com.example.studentclubsmanagement.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentclubsmanagement.util.LogUtil;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d(TAG, getClass().getSimpleName() + " onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, getClass().getSimpleName() + " onCreateView" );
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(TAG, getClass().getSimpleName() + " onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG, getClass().getSimpleName() + " onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(TAG, getClass().getSimpleName() + " onDetach");
    }
}

















