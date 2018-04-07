package com.example.studentclubsmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.Adapter.ClubMemberRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubMember;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.UserIdList;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubOrganizationActivity extends BaseActivity {

    private static final String TAG = "ClubOrganizationActivity";
    private Activity mActivity;
    private String mUrlPrefix;
    private ViewHolder mHolder;
    private int mClubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_organization);

        mActivity = this;
        mHolder = new ViewHolder();
        mUrlPrefix = HttpUtil.getUrlPrefix(this);

        Intent intent = getIntent();
        int clubId = intent.getIntExtra("club_id", 0);
        mClubId = clubId;
        LogUtil.d(TAG, "club_id: " + clubId);

        String url = mUrlPrefix + "/controller/UserClubRelationServlet";
        RequestBody body = new FormBody.Builder()
                .add("club_id", String.valueOf(clubId))
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String responseData = response.body().string();
                LogUtil.d(TAG, "responseData: " + responseData);
                if(!"".equals(responseData)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        UserIdList userIdList = GsonSingleton.getInstance().fromJson(responseData, UserIdList.class);
                        LogUtil.d(TAG, "userIdList: " + userIdList);
                        int[] userIdArray = userIdList.getUserIdArray();
                        LogUtil.d(TAG, "clubIdArray: " + userIdArray);
                        List<String> data = new ArrayList<String>();
                        for (int i = 0; i < userIdArray.length; i++) {
                            data.add(String.valueOf(userIdArray[i]));
                        }
                        requestClubMemberInfo(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
    }

    private void requestClubMemberInfo(List<String> userIdList) {
        String url = mUrlPrefix + "/controller/MultiClubMemberInfoServlet";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (int i = 0; i < userIdList.size(); i++) {
            bodyBuilder.add("user_id_list", userIdList.get(i));
        }
        bodyBuilder.add("club_id", String.valueOf(mClubId));
        RequestBody requestBody = bodyBuilder.build();
        HttpUtil.sendOkHttpRequestWithPost(url, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String responseData = response.body().string();
                LogUtil.d(TAG, "responseData: " + responseData);
                if(!"".equals(responseData)) {
                    Gson gson = GsonSingleton.getInstance();
                    final List<ClubMember> clubMemberList = gson.fromJson(responseData, new TypeToken<List<ClubMember>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO 生成页面
                            List<ClubMember> ministers = new ArrayList<ClubMember>();
                            List<ClubMember> viceMinisters = new ArrayList<ClubMember>();
                            List<ClubMember> staffs = new ArrayList<ClubMember>();
                            for (int i = 0; i < clubMemberList.size(); i++) {
                                ClubMember clubMember = clubMemberList.get(i);
                                if (clubMember.getLevel() == 0) {
                                    staffs.add(clubMember);
                                } else if (clubMember.getLevel() == 1) {
                                    viceMinisters.add(clubMember);
                                } else if (clubMember.getLevel() == 2) {
                                    ministers.add(clubMember);
                                }
                            }
                            initMinister(ministers);
                            initViceMinister(viceMinisters);
                            initStaff(staffs);
                        }
                    });
                } else {
                    HttpUtil.showToastOnUI(mActivity, "出错");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMinister(List<ClubMember> clubMembers) {
        if (clubMembers.size() == 0) {
            return;
        }
        LinearLayout ministerLayout = (LinearLayout) findViewById(R.id.minister);
        ministerLayout.setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < clubMembers.size(); i++) {
            ClubMember clubMember = clubMembers.get(i);
            View item = inflater.inflate(R.layout.club_organization_item, ministerLayout, false);
            ImageView userHeaderImage = item.findViewById(R.id.organization_member_head_image);
            String userHeaderImagePath = clubMember.getUserHeaderImage();
            if (!"".equals(userHeaderImagePath)) {
                Glide.with(mActivity).load(mUrlPrefix + userHeaderImagePath).into(userHeaderImage);
            }
            TextView userName = item.findViewById(R.id.organization_member_name);
            userName.setText(clubMember.getUserName());
            ImageView popupMenu = (ImageView) item.findViewById(R.id.organization_popup_menu);
            popupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_club_organization_minister, popupMenu.getMenu());
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
                }
            });
            ministerLayout.addView(item);
        }
    }

    private void initViceMinister(List<ClubMember> clubMembers) {
        if (clubMembers.size() == 0) {
            return;
        }
        LinearLayout viceMinisterLayout = (LinearLayout) findViewById(R.id.vice_minister);
        viceMinisterLayout .setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < clubMembers.size(); i++) {
            ClubMember clubMember = clubMembers.get(i);
            View item = inflater.inflate(R.layout.club_organization_item, viceMinisterLayout , false);
            ImageView userHeaderImage = item.findViewById(R.id.organization_member_head_image);
            String userHeaderImagePath = clubMember.getUserHeaderImage();
            if (!"".equals(userHeaderImagePath)) {
                Glide.with(mActivity).load(mUrlPrefix + userHeaderImagePath).into(userHeaderImage);
            }
            TextView userName = item.findViewById(R.id.organization_member_name);
            userName.setText(clubMember.getUserName());
            ImageView popupMenu = (ImageView) item.findViewById(R.id.organization_popup_menu);
            popupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_club_organization_vice_minister, popupMenu.getMenu());
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
                }
            });
            viceMinisterLayout .addView(item);
        }
    }

    private void initStaff(List<ClubMember> clubMembers) {
        if (clubMembers.size() == 0) {
            return;
        }
        LinearLayout staffLayout = (LinearLayout) findViewById(R.id.staff);
        staffLayout .setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < clubMembers.size(); i++) {
            ClubMember clubMember = clubMembers.get(i);
            View item = inflater.inflate(R.layout.club_organization_item, staffLayout , false);
            ImageView userHeaderImage = item.findViewById(R.id.organization_member_head_image);
            String userHeaderImagePath = clubMember.getUserHeaderImage();
            if (!"".equals(userHeaderImagePath)) {
                Glide.with(mActivity).load(mUrlPrefix + userHeaderImagePath).into(userHeaderImage);
            }
            TextView userName = item.findViewById(R.id.organization_member_name);
            userName.setText(clubMember.getUserName());
            ImageView popupMenu = (ImageView) item.findViewById(R.id.organization_popup_menu);
            popupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_club_organization_staff, popupMenu.getMenu());
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
                }
            });
            staffLayout .addView(item);
        }
    }

    class ViewHolder {
        Toolbar toolbar = (Toolbar) findViewById(R.id.club_organization_toolbar);

        private ViewHolder() {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("组织结构");
            }
        }
    }
}
