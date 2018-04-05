package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.Newactivity;
import com.example.studentclubsmanagement.gson.News;
import com.example.studentclubsmanagement.util.HttpUtil;

import java.util.List;

/**
 * Created by 李子韬 on 2018/3/13.
 */

public class SquareRecyclerViewAdapter extends RecyclerView.Adapter<SquareRecyclerViewAdapter.ViewHolder> {

    private List<News> mSquareContentList;
    private Context mContext;
    private String mUrlPrefix;
    private ViewHolder mHolder;

    public SquareRecyclerViewAdapter(Context context, List<News> squareContentList) {
        mContext = context;
        mSquareContentList = squareContentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_content_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mHolder = holder;

        mUrlPrefix = HttpUtil.getUrlPrefix(mContext);
//        holder.moreImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//                MenuInflater inflater = popupMenu.getMenuInflater();
//                inflater.inflate(R.menu.menu_square_content, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.exit:
//                                break;
//                            case R.id.set:
//                                break;
//                            default:
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mSquareContentList.get(position);
        String userHeaderImagePath = news.getUserHeaderImagePath();
        if (!"".equals(userHeaderImagePath)) {
            Glide.with(mContext).load(mUrlPrefix + userHeaderImagePath).into(holder.userHeadImage);
        }
        String additionImagePath = news.getImagePath();
        if (!"".equals(additionImagePath)) {
            Glide.with(mContext).load(mUrlPrefix + additionImagePath).override(1000, 500).centerCrop().into(holder.additionalImage);
        }
        holder.userName.setText(news.getUserName());
        holder.clubName.setText(news.getClubName());
        holder.titleText.setText(news.getTitle());
        holder.contentText.setText(news.getContent());
    }

    @Override
    public int getItemCount() {
        return mSquareContentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userHeadImage;
        ImageView additionalImage;
        TextView userName;
        TextView clubName;
        TextView titleText;
        TextView contentText;
        ImageView moreImageView;
        Button startButton;
        Button commentButton;
        Button shareButton;
        LinearLayout clickZone;

        public ViewHolder(View itemView) {
            super(itemView);
            userHeadImage = (ImageView) itemView.findViewById(R.id.user_head_image);
            additionalImage = (ImageView) itemView.findViewById(R.id.additional_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            clubName = (TextView) itemView.findViewById(R.id.club_name);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            contentText = (TextView) itemView.findViewById(R.id.content_text);
//            moreButton = (ImageButton) itemView.findViewById(R.id.more);
            moreImageView = (ImageView) itemView.findViewById(R.id.more);
            startButton = (Button) itemView.findViewById(R.id.start);
            commentButton = (Button) itemView.findViewById(R.id.comment);
            shareButton = (Button) itemView.findViewById(R.id.share);
            clickZone = (LinearLayout) itemView.findViewById(R.id.click_zone);

            startButton.setOnClickListener(new OnClickListenerImpl());
            commentButton.setOnClickListener(new OnClickListenerImpl());
            shareButton.setOnClickListener(new OnClickListenerImpl());
            clickZone.setOnClickListener(new OnClickListenerImpl());
            moreImageView.setOnClickListener(new OnClickListenerImpl());
        }
    }

    class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start:
                    break;
                case R.id.comment:
                    break;
                case R.id.share:
                    break;
                case R.id.click_zone:
                    int position = mHolder.getAdapterPosition();
                    Intent intent = new Intent(mContext, Newactivity.class);
                    intent.putExtra("news_id", mSquareContentList.get(position).getId());
                    mContext.startActivity(intent);
                    break;
                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_square_content, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.exit:
                                    break;
                                case R.id.set:
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    break;
                default:
                    break;
            }
        }
    }
}
