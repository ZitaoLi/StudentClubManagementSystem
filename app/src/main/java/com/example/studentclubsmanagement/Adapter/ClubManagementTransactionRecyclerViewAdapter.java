package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.UserRequestActivity;
import com.example.studentclubsmanagement.gson.ClubActivityRequest;
import com.example.studentclubsmanagement.gson.ClubCreationRequest;
import com.example.studentclubsmanagement.gson.ClubManagementTransaction;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.TimeUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 李子韬 on 2018/4/8.
 */

public class ClubManagementTransactionRecyclerViewAdapter extends RecyclerView.Adapter<ClubManagementTransactionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ClubManagementTransactionRecyclerViewAdapter";
    private List<ClubManagementTransaction> mClubManagementTransactions;
    private Context mContext;

    public ClubManagementTransactionRecyclerViewAdapter(Context context, List<ClubManagementTransaction> clubManagementTransactions) {
        mContext = context;
        mClubManagementTransactions = clubManagementTransactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClubManagementTransaction clubManagementTransaction = mClubManagementTransactions.get(position);
        holder.cardView.setTag(position);
        int typeCode = clubManagementTransaction.getTransactionType();
        String body = clubManagementTransaction.getBody();
        String userName = "请求者：";
        String title = "";
        String content = "";
        String type = "";
        String requestTime = "请求时间：" + clubManagementTransaction.getCreatedTime().toString();
        if (typeCode == 1) {
            // 社团创建请求
            ClubCreationRequest clubCreationRequest = GsonSingleton.getInstance().fromJson(body, ClubCreationRequest.class);
            userName = userName + clubCreationRequest.getUserName();
            title = clubCreationRequest.getTitle();
            content = clubCreationRequest.getContent();
            type = "社团创建";
        } else if (typeCode == 2) {
            // 社团解散请求
        } else if (typeCode == 3) {
            // 社团活动申请
            ClubActivityRequest clubActivityRequest = GsonSingleton.getInstance().fromJson(body, ClubActivityRequest.class);
            userName = userName + clubActivityRequest.getUserName();
            title = clubActivityRequest.getTitle();
            content = clubActivityRequest.getContent();
            type = "活动申请";
        }
        holder.title.setText(title);
        holder.userName.setText(userName);
        holder.content.setText(content);
        holder.requestTime.setText(requestTime);
        holder.type.setText(type);
    }

    @Override
    public int getItemCount() {
        return mClubManagementTransactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView userName;
        TextView requestTime;
        TextView type;
        CardView cardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.admin_transaction_card_view);
            title = (TextView) itemView.findViewById(R.id.admin_transaction_title);
            content = (TextView) itemView.findViewById(R.id.admin_transaction_content_brief);
            userName = (TextView) itemView.findViewById(R.id.admin_transaction_request_user_name);
            type = (TextView) itemView.findViewById(R.id.admin_transaction_type);
            requestTime = (TextView) itemView.findViewById(R.id.admin_transaction_request_time);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) cardView.getTag();
                    ClubManagementTransaction clubManagementTransaction = mClubManagementTransactions.get(position);
                    String body = clubManagementTransaction.getBody();
                    Intent intent = new Intent(mContext, UserRequestActivity.class);
                    intent.putExtra("body", body);
                    intent.putExtra("request_time", mClubManagementTransactions.get(position).getCreatedTime().toString());
                    intent.putExtra("type", mClubManagementTransactions.get(position).getTransactionType());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}





