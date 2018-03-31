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

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.Newactivity;

/**
 * Created by 李子韬 on 2018/3/13.
 */

public class SquareRecyclerViewAdapter extends RecyclerView.Adapter<SquareRecyclerViewAdapter.ViewHolder> {

    private String[] mData;

    public SquareRecyclerViewAdapter(Context context, String[] data) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_content_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

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
        holder.userHeadImage.setImageResource(R.mipmap.ic_launcher_round);
        holder.additionalImage.setImageResource(R.drawable.ic_launcher_background);
        holder.userName.setText("null");
        holder.clubName.setText("null");
        holder.titleText.setText("null");
        holder.contentText.setText("nullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnull");
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView userHeadImage;
        ImageView additionalImage;
        TextView userName;
        TextView clubName;
        TextView titleText;
        TextView contentText;
//        ImageButton moreButton;
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

            startButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            shareButton.setOnClickListener(this);
            clickZone.setOnClickListener(this);
            moreImageView.setOnClickListener(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start:
                    break;
                case R.id.comment:
                    break;
                case R.id.share:
                    break;
                case R.id.click_zone:
                    int position = this.getAdapterPosition();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, Newactivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
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
