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
import com.gc.materialdesign.views.Card;

/**
 * Created by 李子韬 on 2018/3/24.
 */

public class MessageWallRecyclerViewAdapter extends RecyclerView.Adapter<MessageWallRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

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
        holder.title.setText("There is title");
        holder.publisher.setText("Lztao");
        holder.releasedTime.setText("2018-03-24");
        holder.content.setText("There is content");
        holder.type.setText("inner message");
        holder.cardView.setOnClickListener(new OnClickListenerImp());
    }

    @Override
    public int getItemCount() {
        return 4;
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
            Toast.makeText(mContext, "click cardview", Toast.LENGTH_SHORT).show();
        }
    }
}
