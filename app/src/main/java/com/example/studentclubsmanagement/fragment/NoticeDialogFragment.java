package com.example.studentclubsmanagement.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.Adapter.NoticeListRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.BaseActivity;
import com.example.studentclubsmanagement.gson.ClubAccessRequest;
import com.example.studentclubsmanagement.gson.ClubConferenceOrganizing;
import com.example.studentclubsmanagement.gson.ClubMessagePush;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.Notice;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 李子韬 on 2018/4/15.
 */

public class NoticeDialogFragment extends DialogFragment {

    private int mNoticeType;
    private String mBody;
    private AlertDialog mDialog;
    private int mPosition;
    private NoticeListRecyclerViewAdapter mAdapter;

    public void setNoticeType(int noticeType) {
        mNoticeType = noticeType;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public void setAdapter(NoticeListRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "";
        if (mNoticeType == 1) {
            title = "系统通知";
        } else if(mNoticeType == 2) {
            title = "社团内部通知";
        } else if(mNoticeType == 3) {
            title = "社团会议通知";
        } else if(mNoticeType == 4) {
            title = "入团申请通知";
        }
        View view = initInnerView(mNoticeType, mBody);
        builder.setTitle(title).setView(view);
        AlertDialog dialog = builder.create();
        mDialog = dialog;
        return dialog;
    }

    private View initInnerView(int noticeType, String body) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout.LayoutParams params;
        View view = inflater.from(getActivity()).inflate(R.layout.notice_dialog_inner_layout, null);
        ImageView userHeader = (ImageView) view.findViewById(R.id.notice_dialog_user_header);
        TextView userName = (TextView) view.findViewById(R.id.notice_dialog_user_name);
        TextView title = (TextView) view.findViewById(R.id.notice_dialog_title);
        TextView content = (TextView) view.findViewById(R.id.notice_dialog_content);
        ButtonFlat cancelBtn = (ButtonFlat) view.findViewById(R.id.notice_dialog_btn1);
        ButtonRectangle readBtn = (ButtonRectangle) view.findViewById(R.id.notice_dialog_btn2);
        ButtonRectangle rejectionBtn = (ButtonRectangle) view.findViewById(R.id.notice_dialog_btn3);
        ButtonRectangle agreementBtn = (ButtonRectangle) view.findViewById(R.id.notice_dialog_btn4);

        cancelBtn.setOnClickListener(new ViewOnClickListenerImpl());
        readBtn.setOnClickListener(new ViewOnClickListenerImpl());
        rejectionBtn.setOnClickListener(new ViewOnClickListenerImpl());
        agreementBtn.setOnClickListener(new ViewOnClickListenerImpl());

        if (noticeType == 1) {

        } else if(noticeType == 2) {
            ClubMessagePush request = GsonSingleton.getInstance().fromJson(body, ClubMessagePush.class);
            String path =  request.getUserHeaderImagePath();
            if (!"".equals(path) && path != null) {
                Glide.with(getActivity()).load(HttpUtil.getUrlPrefix(getActivity()) + path).into(userHeader);
            }
            userName.setText(request.getUserName());
            title.setText(request.getTitle());
            content.setText(request.getContent());
            readBtn.setVisibility(View.VISIBLE);
            rejectionBtn.setVisibility(View.GONE);
            agreementBtn.setVisibility(View.GONE);
        } else if(noticeType == 3) {
            ClubConferenceOrganizing request = GsonSingleton.getInstance().fromJson(body, ClubConferenceOrganizing.class);
            String path =  request.getUserHeaderImagePath();
            if (!"".equals(path) && path != null) {
                Glide.with(getActivity()).load(HttpUtil.getUrlPrefix(getActivity()) + path).into(userHeader);
            }
            userName.setText(request.getUserName());
            title.setText(request.getTitle());
            content.setText(request.getContent());
            readBtn.setVisibility(View.VISIBLE);
            rejectionBtn.setVisibility(View.GONE);
            agreementBtn.setVisibility(View.GONE);
        } else if(noticeType == 4) {
            ClubAccessRequest request = GsonSingleton.getInstance().fromJson(body, ClubAccessRequest.class);
            String path =  request.getUserImageHeader();
            if (!"".equals(path) && path != null) {
                Glide.with(getActivity()).load(HttpUtil.getUrlPrefix(getActivity()) + path).into(userHeader);
            }
            userName.setText(request.getUserName());
            title.setText(request.getTitle());
            content.setText(request.getContent());
            readBtn.setVisibility(View.GONE);
            rejectionBtn.setVisibility(View.VISIBLE);
            agreementBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class ViewOnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.notice_dialog_btn1:
                    mDialog.dismiss();
                    break;
                case R.id.notice_dialog_btn2:
                    mAdapter.getNoticeList().remove(mPosition);
                    mAdapter.notifyDataSetChanged();
                    mDialog.dismiss();
                    break;
                case R.id.notice_dialog_btn3:
                    mAdapter.getNoticeList().remove(mPosition);
                    mAdapter.notifyDataSetChanged();
                    mDialog.dismiss();
                    break;
                case R.id.notice_dialog_btn4:
                    mAdapter.getNoticeList().remove(mPosition);
                    mAdapter.notifyDataSetChanged();
                    mDialog.dismiss();
                    break;
            }
        }
    }
}








