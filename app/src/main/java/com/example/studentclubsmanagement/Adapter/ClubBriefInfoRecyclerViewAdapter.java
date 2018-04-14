package com.example.studentclubsmanagement.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.ClubInfoActivity;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.util.HttpUtil;

import java.util.List;
import java.util.zip.Inflater;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by 李子韬 on 2018/4/14.
 */

public class ClubBriefInfoRecyclerViewAdapter extends RecyclerView.Adapter<ClubBriefInfoRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ClubBriefInfoRecyclerViewAdapter";
    private List<Club> mClubList;
    private Context mContext;

    public ClubBriefInfoRecyclerViewAdapter(Context context, List<Club> clubList) {
        mContext = context;
        mClubList = clubList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_breif_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Club club = mClubList.get(position);
        String clubHeaderImagePath = club.getClubBgImagePath();
        if (!"".equals(clubHeaderImagePath) && clubHeaderImagePath != null) {
            Glide.with(mContext).load(HttpUtil.getUrlPrefix(mContext) + clubHeaderImagePath).into(holder.clubHeader);
        }
        holder.clubName.setText(club.getClubName());
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mClubList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView clubHeader;
        TextView clubName;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            clubHeader = (ImageView) itemView.findViewById(R.id.club_brief_info_club_header);
            clubName = (TextView) itemView.findViewById(R.id.club_brief_info_club_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    int clubId = mClubList.get(position).getId();
                    Intent intent = new Intent(mContext, ClubInfoActivity.class);
                    intent.putExtra("club_id", clubId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
