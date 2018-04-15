package com.example.studentclubsmanagement.Adapter;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.NoticeActivity;
import com.example.studentclubsmanagement.activity.NoticeListActivity;
import com.example.studentclubsmanagement.fragment.NoticeDialogFragment;
import com.example.studentclubsmanagement.gson.ClubAccessRequest;
import com.example.studentclubsmanagement.gson.ClubConferenceOrganizing;
import com.example.studentclubsmanagement.gson.ClubMessagePush;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.Notice;

import java.util.List;

/**
 * Created by 李子韬 on 2018/4/14.
 */

public class NoticeListRecyclerViewAdapter extends RecyclerView.Adapter<NoticeListRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Notice> mNoticeList;

    public NoticeListRecyclerViewAdapter(Context context, List<Notice> noticeLit) {
        mContext = context;
        mNoticeList = noticeLit;
    }

    public List<Notice> getNoticeList() {
        return mNoticeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice notice = mNoticeList.get(position);
        String body = notice.getBody();
        if ("".equals(body) || body == null) {
            return;
        }
        int noticeType = notice.getNoticeType();
        String title = "";
        String sender = "";
        String sendTime = "";
        String content = "";
        String type = "";
        if (noticeType == 1) {
            // 系统通知
        } else if (noticeType == 2) {
            // 社团内部通知
            ClubMessagePush request = GsonSingleton.getInstance().fromJson(body, ClubMessagePush.class);
            title = request.getTitle();
            sender = request.getUserName();
            sendTime = notice.getCreatedTime().toString();
            content = request.getContent();
            type = "社团内部通知";
        } else if (noticeType == 3) {
            // 社团会议消息
            ClubConferenceOrganizing request = GsonSingleton.getInstance().fromJson(body, ClubConferenceOrganizing.class);
            title = request.getTitle();
            sender = request.getUserName();
            sendTime = notice.getCreatedTime().toString();
            content = request.getContent();
            type = "社团会议通知";
        } else if (noticeType == 4) {
            // 入团申请通知
            ClubAccessRequest request = GsonSingleton.getInstance().fromJson(body, ClubAccessRequest.class);
            title = request.getTitle();
            sender = request.getUserName();
            sendTime = notice.getCreatedTime().toString();
            content = request.getContent();
            type = "入团申请";
        }
        holder.title.setText(title);
        holder.sender.setText(sender);
        holder.sendTime.setText(sendTime);
        holder.content.setText(content);
        holder.type.setText(type);
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mNoticeList.size();
    }

    public void removeItem(int position) {
        mNoticeList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNoticeList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView sender;
        TextView sendTime;
        TextView content;
        TextView type;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.notice_list_item_title);
            sender = (TextView) itemView.findViewById(R.id.notice_list_item_sender);
            sendTime = (TextView) itemView.findViewById(R.id.notice_list_item_send_time);
            content = (TextView) itemView.findViewById(R.id.notice_list_item_content);
            type = (TextView) itemView.findViewById(R.id.notice_list_item_type);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    Notice notice = mNoticeList.get(position);
//                    Intent intent = new Intent(mContext, NoticeActivity.class);
//                    intent.putExtra("type", notice.getNoticeType());
//                    intent.putExtra("body", notice.getBody());
//                    mContext.startActivity(intent);
                    NoticeDialogFragment dialogFragment = new NoticeDialogFragment();
                    FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    dialogFragment.setNoticeType(notice.getNoticeType());
                    dialogFragment.setBody(notice.getBody());
                    dialogFragment.setPosition(position);
                    dialogFragment.setAdapter(NoticeListRecyclerViewAdapter.this);
                    dialogFragment.show(manager, "notice_dialog");
                }
            });
        }
    }
}
