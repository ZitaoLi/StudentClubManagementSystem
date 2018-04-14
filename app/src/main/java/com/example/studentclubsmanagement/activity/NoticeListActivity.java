package com.example.studentclubsmanagement.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.studentclubsmanagement.Adapter.NoticeListRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.GsonSingleton;
import com.example.studentclubsmanagement.gson.Notice;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoticeListActivity extends BaseActivity {

    private static final String TAG = "NoticeListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("我的消息");
        }

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);
        if (userId != 0) {
            RequestBody body = new FormBody.Builder().add("user_id", String.valueOf(userId)).build();
            String url = HttpUtil.getUrlPrefix(this) + "/controller/NoticePullerServlet";
            HttpUtil.sendOkHttpRequestWithPost(url, body, new CallBackImpl());
        }
    }

    class CallBackImpl implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(final Call call, Response response) throws IOException {
            final String responseData = response.body().string();
            if (!"".equals(responseData)) {
                // TODO 更新消息列表
                if ("0".equals(responseData)) return;
                if ("-1".equals(responseData)) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Notice> notices = GsonSingleton.getInstance().fromJson(responseData, new TypeToken<List<Notice>>(){}.getType());
                        RecyclerView noticeList = (RecyclerView) findViewById(R.id.notice_list_recycler_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(NoticeListActivity.this);
                        NoticeListRecyclerViewAdapter adapter = new NoticeListRecyclerViewAdapter(NoticeListActivity.this, notices);
                        noticeList.setLayoutManager(layoutManager);
                        noticeList.setAdapter(adapter);
                        // TODO 设置左滑菜单
                        SimpleCallbackImpl simpleCallback = new SimpleCallbackImpl(0,ItemTouchHelper.LEFT);
                        simpleCallback.setAdapter(adapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(noticeList);
                    }
                });
            }
        }
    }

    class SimpleCallbackImpl extends ItemTouchHelper.SimpleCallback {

        private NoticeListRecyclerViewAdapter mAdapter;

        public SimpleCallbackImpl(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        public void setAdapter(NoticeListRecyclerViewAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT){
                mAdapter.removeItem(position);
            } else {

            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                if(dX > 0){
//                    p.setColor(Color.parseColor("#388E3C"));
//                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
//                    c.drawRect(background,p);
//                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_black_24dp);
//                    RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
//                    c.drawBitmap(icon,null,icon_dest,p);
                } else {
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background,p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_black_24dp);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon,null,icon_dest,p);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
