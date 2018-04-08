package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.ClubInfoActivity;
import com.example.studentclubsmanagement.activity.MessagePushActivity;
import com.example.studentclubsmanagement.gson.ClubConferenceOrganizing;
import com.example.studentclubsmanagement.gson.ClubInternalTransaction;
import com.example.studentclubsmanagement.gson.ClubMessagePush;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.gc.materialdesign.views.Card;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 李子韬 on 2018/3/24.
 */

public class MessageWallRecyclerViewAdapter extends RecyclerView.Adapter<MessageWallRecyclerViewAdapter.ViewHolder> {

    public static int CLUB_MESSAGE_PUSH = 1;
    public static int CLUB_CONFERENCE_ORGANIZING = 2;
    private Context mContext;
    private List<ClubInternalTransaction> mClubInternalTransactions;

    public MessageWallRecyclerViewAdapter(Context context, List<ClubInternalTransaction> clubInternalTransactions) {
        mContext = context;
        mClubInternalTransactions = clubInternalTransactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_wall_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClubInternalTransaction clubInternalTransaction = mClubInternalTransactions.get(position);
        String body = clubInternalTransaction.getBody();
        int typeCode = clubInternalTransaction.getTransacitonType();
        Timestamp createdTime = clubInternalTransaction.getCreatedTime();
        // TODO 解析body
        String title = "";
        String time = "";
        String userName = "";
        String type = "";
        String content = "";
        if (typeCode == CLUB_MESSAGE_PUSH) {
            ClubMessagePush clubMessagePush = GsonSingleton.getInstance().fromJson(body, ClubMessagePush.class);
            title = clubMessagePush.getTitle();
            time = createdTime.toString();
            userName = clubMessagePush.getUserName();
            type = "消息";
            content = clubMessagePush.getContent();
        } else if (typeCode == CLUB_CONFERENCE_ORGANIZING) {
            ClubConferenceOrganizing clubConferenceOrganizing = GsonSingleton.getInstance().fromJson(body, ClubConferenceOrganizing.class);
            title = clubConferenceOrganizing.getTitle();
            time = createdTime.toString();
            userName = clubConferenceOrganizing.getUserName();
            type = "会议";
            content = clubConferenceOrganizing.getContent();
        }

        holder.title.setText(title);
        holder.publisher.setText(userName);
        holder.releasedTime.setText(time);
        holder.content.setText(content);
        holder.type.setText(type);
        holder.cardView.setOnClickListener(new OnClickListenerImp());
    }

    @Override
    public int getItemCount() {
        return mClubInternalTransactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView publisher;
        TextView releasedTime;
        TextView content;
        TextView type;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            title = (TextView) itemView.findViewById(R.id.message_wall_title);
            publisher = (TextView) itemView.findViewById(R.id.message_wall_publisher);
            releasedTime = (TextView) itemView.findViewById(R.id.message_wall_released_time);
            content = (TextView) itemView.findViewById(R.id.message_wall_content);
            type = (TextView) itemView.findViewById(R.id.message_wall_type);
        }
    }

    class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            Toast.makeText(mContext, "click cardview", Toast.LENGTH_SHORT).show();
        }
    }
}
