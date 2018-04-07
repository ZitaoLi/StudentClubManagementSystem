package com.example.studentclubsmanagement.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.fragment.StaffPowerDialogFragment;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.gson.ClubMember;
import com.example.studentclubsmanagement.util.HttpUtil;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by 李子韬 on 2018/3/23.
 */

public class ClubMemberRecyclerViewAdapter extends RecyclerView.Adapter<ClubMemberRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private boolean mHaveCheckBox;
    private List<ClubMember> mClubMemberList;
    private String mUrlPrefix;

    public ClubMemberRecyclerViewAdapter(Context context, boolean haveCheckBox, List<ClubMember> clubMemberList) {
        mContext = context;
        mHaveCheckBox = haveCheckBox;
        mClubMemberList = clubMemberList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_member_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mUrlPrefix = HttpUtil.getUrlPrefix(mContext);
        ClubMember clubMember = mClubMemberList.get(position);
        holder.clubMemberName.setText(clubMember.getUserName());
        holder.clubMemberName.setTag(position);
        String userHeaderImagePath = clubMember.getUserHeaderImage();
        if (!"".equals(userHeaderImagePath)) {
            Glide.with(mContext).load(mUrlPrefix + userHeaderImagePath).into(holder.clubMemberHeaderImage);
        }
        holder.checkBox.setChecked(true);
        if (!mHaveCheckBox) {
            holder.clubMemberName.setOnClickListener(new OnClickListenerImp());
        }
    }

    @Override
    public int getItemCount() {
        return mClubMemberList.size();
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
                    int position = (int) view.getTag();
                    StaffPowerDialogFragment dialogFragment =  new StaffPowerDialogFragment();
                    dialogFragment.setPower(mClubMemberList.get(position).getPower());
                    FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(manager, "staff_power");
//                    dialogFragment.getDialog().getWindow().setLayout(500, 500);
                    break;
                default:
                    break;
            }
        }
    }
}
