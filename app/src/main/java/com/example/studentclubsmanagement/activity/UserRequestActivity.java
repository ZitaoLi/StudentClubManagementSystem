package com.example.studentclubsmanagement.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.ImageViewer;
import com.example.studentclubsmanagement.gson.ClubActivityRequest;
import com.example.studentclubsmanagement.gson.ClubCreationRequest;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UserRequestActivity extends BaseActivity {

    private static int CLUB_CREATION_REQUEST = 1;
    private static int CLUB_DISSOLUTION = 2;
    private static int CLUB_ACTIVITY_REQUEST = 3;
    private static final String TAG = "UserRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        final ViewHolder holder = new ViewHolder();

        Intent intent = getIntent();
        String body = intent.getStringExtra("body");
        String requestTime = intent.getStringExtra("request_time");
        int typeCode = intent.getIntExtra("type", 0);
        Map map = new  HashMap();
        if (body != null && typeCode != 0) {
            if (typeCode == CLUB_CREATION_REQUEST) {
                ClubCreationRequest request = GsonSingleton.getInstance().fromJson(body, ClubCreationRequest.class);
                map.put("user_header_image_path", request.getUserHeaderImagePath());
                map.put("user_name", request.getUserName());
                map.put("request_time", requestTime);
                map.put("title", request.getTitle());
                map.put("content", request.getContent());
                map.put("type", "社团创建");
                map.put("additional_image_path", request.getImagePath());
            } else if (typeCode == CLUB_DISSOLUTION) {

            } else if (typeCode == CLUB_ACTIVITY_REQUEST) {
                ClubActivityRequest request = GsonSingleton.getInstance().fromJson(body, ClubActivityRequest.class);
                map.put("user_header_image_path", request.getUserHeaderImagePath());
                map.put("user_name", request.getUserName());
                map.put("request_time", requestTime);
                map.put("title", request.getTitle());
                map.put("content", request.getContent());
                map.put("type", "活动申请");
            }
        }
        String userHeaderImagePath = (String) map.get("user_header_image_path");
        if (!"".equals(userHeaderImagePath) && userHeaderImagePath != null) {
            Glide.with(this).load(HttpUtil.getUrlPrefix(this) + userHeaderImagePath).into(holder.userHeader);
        }
        String additionalImagePath = (String) map.get("additional_image_path");
        if (!"".equals(userHeaderImagePath) && userHeaderImagePath != null) {
            Glide.with(this)
                    .load(HttpUtil.getUrlPrefix(this) + additionalImagePath)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            if (holder.additionalImage == null) {
//                                return false;
//                            }
//                            if (holder.additionalImage.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                                holder.additionalImage.setScaleType(ImageView.ScaleType.FIT_XY);
//                            }
//                            ViewGroup.LayoutParams params = holder.additionalImage.getLayoutParams();
//                            int vw = holder.additionalImage.getWidth() - holder.additionalImage.getPaddingLeft() - holder.additionalImage.getPaddingRight();
//                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                            params.width = vw;
//                            params.height = vh + holder.additionalImage.getPaddingTop() + holder.additionalImage.getPaddingBottom();
//                            LogUtil.d(TAG,"width: " + params.width + " height: " + params.height);
//                            holder.additionalImage.setLayoutParams(params);
//                            return false;
//                        }
//                    })
                    .into(holder.additionalImage);
        }
        holder.additionalImage.setTag(additionalImagePath);
        holder.userName.setText((String) map.get("user_name"));
        holder.requestTime.setText((String) map.get("request_time"));
        holder.title.setText((String) map.get("title"));
        holder.content.setText((String) map.get("content"));
        holder.type.setText((String) map.get("type"));
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

    class ViewHolder {

        ImageView userHeader = (ImageView) findViewById(R.id.user_request_header);
        TextView userName = (TextView) findViewById(R.id.user_request_user_name);
        TextView requestTime = (TextView) findViewById(R.id.user_request_time);
        TextView title = (TextView) findViewById(R.id.user_request_title);
        TextView content = (TextView) findViewById(R.id.user_request_content);
        TextView type = (TextView) findViewById(R.id.user_request_type);
        ImageView additionalImage = (ImageView) findViewById(R.id.user_request_additional_iamge);
        FloatingActionButton agreementBtn = (FloatingActionButton) findViewById(R.id.club_transaction_agreement);
        FloatingActionButton skipBtn = (FloatingActionButton) findViewById(R.id.club_transaction_skip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_request_toolbar);

        private ViewHolder() {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("入团申请");
            }
            agreementBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), "同意申请", Toast.LENGTH_SHORT).show();
                }
            });
            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), "忽略申请", Toast.LENGTH_SHORT).show();
                }
            });
            additionalImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 点击加载大图
                    String url = (String) view.getTag();
                    ImageViewer.Builder builder = new ImageViewer.Builder(getBaseContext());
                    ImageViewer imageViewer = builder.setUrl(url).build();
                    imageViewer.show(getSupportFragmentManager(), "image_viewer");
                }
            });
        }
    }
}
