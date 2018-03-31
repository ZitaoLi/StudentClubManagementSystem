package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.fragment.StaffPowerDialogFragment;

/**
 * Created by 李子韬 on 2018/3/23.
 */

public class ClubMemberRecyclerViewAdapter extends RecyclerView.Adapter<ClubMemberRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private boolean mHaveCheckBox;

    public ClubMemberRecyclerViewAdapter(Context context, boolean haveCheckBox) {
        mContext = context;
        mHaveCheckBox = haveCheckBox;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_member_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clubMemberHeaderImage.setImageResource(R.drawable.test_user_header);
        holder.checkBox.setChecked(true);
        if (!mHaveCheckBox) {
            holder.clubMemberName.setOnClickListener(new OnClickListenerImp());
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView clubMemberHeaderImage;
        com.gc.materialdesign.views.CheckBox checkBox;
        TextView clubMemberName;

        public ViewHolder(View itemView) {
            super(itemView);
            clubMemberHeaderImage = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.club_member_list_image_view);
            checkBox = (com.gc.materialdesign.views.CheckBox) itemView.findViewById(R.id.club_member_list_checkbox);
            if (!mHaveCheckBox) {
                checkBox.setVisibility(View.GONE);
            }
            clubMemberName = (TextView) itemView.findViewById(R.id.club_member_list_text_view);
        }
    }

    class OnClickListenerImp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.club_member_list_text_view:
                    StaffPowerDialogFragment dialogFragment =  new StaffPowerDialogFragment();
                    FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(manager, "staff_power");
                    break;
                default:
                    break;
            }
        }
    }
}
