package com.example.studentclubsmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.Club;
import com.example.studentclubsmanagement.gson.ClubIdList;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubListActivity extends BaseActivity {

    private List<Club> mClubList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.club_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("社团列表");
        }

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);
        if (userId != 0) {
            RequestBody body = new FormBody.Builder().add("user_id", String.valueOf(userId)).build();
            String url = HttpUtil.getUrlPrefix(this) + "/controller/UserClubRelationServlet";
            HttpUtil.sendOkHttpRequestWithPost(url, body, new HttpCallBackImpl());
        }
    }

    class HttpCallBackImpl implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();
            if (!"".equals(responseData) && responseData != null) {
                if ("0".equals(responseData)) return;
                if ("-1".equals(responseData)) return;
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    ClubIdList clubIdContainer = GsonSingleton.getInstance().fromJson(responseData, ClubIdList.class);
                    int[] clubIdArray = clubIdContainer.getClubIdArray();
                    List<String> clubIdList = new ArrayList<String>();
                    for (int i = 0; i < clubIdArray.length; i++) {
                        clubIdList.add(String.valueOf(clubIdArray[i]));
                    }
                    String url = HttpUtil.getUrlPrefix(ClubListActivity.this) + "/controller/ClubInfoServlet";
                    FormBody.Builder builder = new FormBody.Builder();
                    for (int i = 0; i < clubIdList.size(); i++) {
                        builder.add("club_id_list", clubIdList.get(i));
                    }
                    RequestBody body = builder.build();
                    HttpUtil.sendOkHttpRequestWithPost(url, body, new HttpCallBackImpl2());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                HttpUtil.showToastOnUI(ClubListActivity.this, "出错");
            }
        }
    }

    class HttpCallBackImpl2 implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();
            if (!"".equals(responseData) && responseData != null) {
                if ("0".equals(responseData)) return;
                if ("-1".equals(responseData)) return;
                mClubList = GsonSingleton.getInstance().fromJson(responseData, new TypeToken<List<Club>>(){}.getType());
                if (mClubList != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.club_list_recycler_view);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ClubListActivity.this);
                            ClubListAdapter adapter = new ClubListAdapter();
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            } else {
                HttpUtil.showToastOnUI(ClubListActivity.this, "出错");
            }
        }
    }

    class ClubListAdapter extends RecyclerView.Adapter<ClubListActivity.ClubListViewHolder> {

        @Override
        public ClubListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ClubListActivity.this).inflate(R.layout.club_breif_info_item, parent, false);
            return new ClubListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ClubListViewHolder holder, int position) {
            Club club = mClubList.get(position);
            String clubHeaderImagePath = club.getClubBgImagePath();
            if (!"".equals(clubHeaderImagePath) && clubHeaderImagePath != null) {
                Glide.with(ClubListActivity.this).load(HttpUtil.getUrlPrefix(ClubListActivity.this) + clubHeaderImagePath).into(holder.clubHeader);
            }
            holder.clubName.setText(club.getClubName());
            holder.view.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mClubList.size();
        }
    }

    class ClubListViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView clubHeader;
        TextView clubName;

        public ClubListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            clubHeader = (ImageView) itemView.findViewById(R.id.club_brief_info_club_header);
            clubName = (TextView) itemView.findViewById(R.id.club_brief_info_club_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    int clubId = mClubList.get(position).getId();
                    Intent intent = new Intent(ClubListActivity.this, ClubInfoActivity.class);
                    intent.putExtra("club_id", clubId);
                    startActivity(intent);
                }
            });
        }
    }
}
