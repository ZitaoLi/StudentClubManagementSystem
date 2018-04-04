package com.example.studentclubsmanagement.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentclubsmanagement.Adapter.DashboardRecyclerViewAdapter;
import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.activity.ActivityRequestActivity;
import com.example.studentclubsmanagement.activity.ClubAccessActivity;
import com.example.studentclubsmanagement.activity.ClubCreationActivity;
import com.example.studentclubsmanagement.activity.ClubOrganizationActivity;
import com.example.studentclubsmanagement.activity.ClubQueryActivity;
import com.example.studentclubsmanagement.activity.ConferenceOrganizingActivity;
import com.example.studentclubsmanagement.activity.MemberAdditionActivity;
import com.example.studentclubsmanagement.activity.MemberDeletionActivity;
import com.example.studentclubsmanagement.activity.MessagePushActivity;
import com.example.studentclubsmanagement.activity.MessageWallActivity;
import com.example.studentclubsmanagement.activity.StaffPowerActivity;
import com.example.studentclubsmanagement.gson.ClubIdList;
import com.example.studentclubsmanagement.gson.ClubMember;
import com.example.studentclubsmanagement.gson.Power;
import com.example.studentclubsmanagement.gson.PowerItem;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 李子韬 on 2018/3/12.
 */

public class DashboardFragment extends BaseFragment implements  DashboardRecyclerViewAdapter.ItemClickListener, View.OnClickListener {

    @android.support.annotation.IdRes
    public static final int CLUB_ACCESS = 1001;
    public static final int CLUB_CREATION = 1002;
    public static final int CLUB_QUERY = 1003;
    public static final int MEMBER_ADDITION = 1004;
    public static final int MEMBER_DELETION = 1005;
    public static final int STAFF_POWER = 1006;
    public static final int ORGANIZATION = 1007;
    public static final int CLUB_DISSOLUTION = 1008;
    public static final int NEWS_PUSH = 1009;
    public static final int ACTIVITY_REQUEST = 1010;
    public static final int CONFERENCE_ORGANIZING = 1011;
    public static final int MESSAGE_WALL = 1012;
    public static final int[] cell_id = {
            CLUB_ACCESS,
            CLUB_CREATION,
            CLUB_QUERY,
            MEMBER_ADDITION,
            MEMBER_DELETION,
            STAFF_POWER,
            ORGANIZATION,
            CLUB_DISSOLUTION,
            NEWS_PUSH,
            ACTIVITY_REQUEST,
            CONFERENCE_ORGANIZING,
            MESSAGE_WALL
    };
    private Map<Integer, List> mCellIdTextMap;
    private static final String TAG = "DashboardFragment";
    private static final int NUM_OF_COLUMN = 4;
    private int[] mLayer = new int[] { R.id.layer_1, R.id.layer_1, R.id.layer_1  };
    private DashboardRecyclerViewAdapter adapter;
    private List<List<Integer>> data;
    private boolean isSpinnerFirst = true;
    private String mUrlPrefix;
    private int mUserId;
    private int mCurrentClubId;
    private Activity mActivity;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mView = view;
        initCellIdTextMap();
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_recycler_view);
//        int numberOfColumns = 4;
//        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
//        adapter = new DashboardRecyclerViewAdapter(view.getContext(), new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "48" });
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);
        SharedPreferences sp = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        mUserId = sp.getInt("user_id", 0);
        mActivity = this.getActivity();
        mUrlPrefix = HttpUtil.getUrlPrefix(mActivity);
        String url = mUrlPrefix + "/controller/UserClubRelationServlet";
        RequestBody body = new FormBody.Builder()
                .add("user_id", String.valueOf(mUserId))
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
                LogUtil.d(TAG, responseData);
                if (responseData != null) {
                    // TODO 加载社团成员仪表盘
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        ClubIdList clubIdList = new Gson().fromJson(responseData, ClubIdList.class);
                        LogUtil.d(TAG, "clubIdList: " + clubIdList);
                        int[] clubIdArray = clubIdList.getClubIdArray();
                        LogUtil.d(TAG, "clubIdArray: " + clubIdArray);
                        List<String> data = new ArrayList<String>();
                        for (int i = 0; i < clubIdArray.length; i++) {
                            data.add(String.valueOf(clubIdArray[i]));
                        }
                        requestClubMemberInfo(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO 显示普通用户仪表盘
                }
            }
        });

//        initSpinner(view);
//        initCell(view);
        return view;
    }

    private void requestClubMemberInfo(List<String> clubIdList) {
        String url = mUrlPrefix + "/controller/MultiClubMemberInfoServlet";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (int i = 0; i < clubIdList.size(); i++) {
            bodyBuilder.add("club_id_list", clubIdList.get(i));
        }
        bodyBuilder.add("user_id", String.valueOf(mUserId));
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
                LogUtil.d(TAG, responseData);
                List<ClubMember> clubMembers = new Gson().fromJson(responseData, new TypeToken<List<ClubMember>>(){}.getType());
                initSpinner(clubMembers);

                LogUtil.d(TAG, "clubMembers: " + clubMembers);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

    private void initSpinner(final List<ClubMember> clubMembers) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                List<String> clubNames = new ArrayList<String>();
                final List<Power> powers = new ArrayList<Power>();
                for (int i = 0; i < clubMembers.size(); i++) {
                    clubNames.add(clubMembers.get(i).getClubName());
                    powers.add(gson.fromJson(clubMembers.get(i).getPower(), Power.class));
                }
                String[] clubNameStringArrary = (String[]) clubNames.toArray(new String[clubNames.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_list_item_1, clubNameStringArrary);
                Spinner spinner = (Spinner) mView.findViewById(R.id.dashboard_spinner);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        LogUtil.d(TAG, "click item: " + i);
                        LogUtil.d(TAG, "powers.get(i): " + powers.get(i));
                        mCurrentClubId = clubMembers.get(i).getClubId();
                        initCell(powers.get(i));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    // 测试用方法
    private void initCell(Power power) {
        Gson gson = new Gson();
        List<PowerItem> powerItems = power.getPower();
        LogUtil.d(TAG, "powerItems: " + powerItems);

        data = new ArrayList<List<Integer>>();
        for ( int i = 0; i < powerItems.size() - 1; i++) {
            List<Integer> item = new ArrayList<Integer>();
            for (int j = 0; j < powerItems.get(i).getItems().length; j++) {
                item.add(powerItems.get(i).getItems()[j]);
            }
            data.add(item);
        }
//        data.add(Arrays.asList(1,2,3));
//        data.add(Arrays.asList(4, 5, 6, 7, 8));
//        data.add(Arrays.asList(9, 10, 11, 12));

        for (int i = 0; i < powerItems.size() - 1; i++) {
            LinearLayout linearLayout = (LinearLayout) mView.findViewById(mLayer[i]);
            insertCell(data.get(i), linearLayout);
        }
    }

    private void insertCell(List<Integer> cells, LinearLayout parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 这部分逻辑写成了一坨，很恶心
        int cellSize = cells.size();
        int numberOfLayer = (int) Math.ceil((float)cellSize / (float)NUM_OF_COLUMN);
        int numPerLayer[] = new int[numberOfLayer];
        int counter = cellSize;
        for (int k = 0; k < numberOfLayer; k++) {
            if (counter - NUM_OF_COLUMN < 0) {
                numPerLayer[k] = counter;
            } else {
                counter = counter - NUM_OF_COLUMN;
                numPerLayer[k] = NUM_OF_COLUMN;
            }
        }
        counter = 0;
//        int k = 0;
//        for (; k < numberOfLayer - 1; k++) {
//            t[k] = NUM_OF_COLUMN;
//        }
//        if (cellSize % NUM_OF_COLUMN == 0) {
//            t[k] = 4;
//        } else {
//            t[k] = cellSize % NUM_OF_COLUMN;
//        }
        for (int i = 0; i < numberOfLayer; i++) {
            // 动态添加LinearLayout
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < numPerLayer[i]; j++) {
                // 动态添加子控件
                View view = inflater.inflate(R.layout.dashboard_cell_item, linearLayout, false);
                WindowManager windowManager = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
                int width = windowManager.getDefaultDisplay().getWidth() / 4;
                view.setLayoutParams(new RelativeLayout.LayoutParams(width , width));
                TextView cellText =((TextView) view.findViewById(R.id.cell_text));
                ImageView cellImage= (ImageView) view.findViewById(R.id.cell_image);
//                view.setId(cell_id[cells.get(i + j) - 1]);
//                cellText.setText((String) mCellIdTextMap.get(cell_id[cells.get(i + j) - 1]).get(0));
//                cellImage.setImageResource((Integer) mCellIdTextMap.get(cell_id[cells.get(i + j) - 1]).get(1));
                view.setId(cell_id[cells.get(counter) - 1]);
                cellText.setText((String) mCellIdTextMap.get(cell_id[cells.get(counter) - 1]).get(0));
                cellImage.setImageResource((Integer) mCellIdTextMap.get(cell_id[cells.get(counter) - 1]).get(1));
                view.setOnClickListener(this);
                linearLayout.addView(view);
                counter++;
            }
            parent.addView(linearLayout);
        }
    }

    private void testCell(View view) {
        Toast.makeText(view.getContext(), (String) mCellIdTextMap.get(view.getId()).get(0), Toast.LENGTH_SHORT).show();
    }

    private void startSubPage(View view, Class cls) {
        Intent intent = new Intent(view.getContext(), cls);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case CLUB_ACCESS:
                testCell(view);
                startSubPage(view, ClubAccessActivity.class);
                break;
            case CLUB_CREATION:
                testCell(view);
                startSubPage(view, ClubCreationActivity.class);
                break;
            case CLUB_QUERY:
                testCell(view);
                startSubPage(view, ClubQueryActivity.class);
                break;
            case MEMBER_ADDITION:
                testCell(view);
                startSubPage(view, MemberAdditionActivity.class);
                break;
            case MEMBER_DELETION:
                testCell(view);
                startSubPage(view, MemberDeletionActivity.class);
                break;
            case STAFF_POWER:
                testCell(view);
                startSubPage(view, StaffPowerActivity.class);
                break;
            case ORGANIZATION:
                testCell(view);
                startSubPage(view, ClubOrganizationActivity.class);
                break;
            case CLUB_DISSOLUTION:
                testCell(view);
                break;
            case ACTIVITY_REQUEST:
                testCell(view);
                startSubPage(view, ActivityRequestActivity.class);
                break;
            case NEWS_PUSH:
                testCell(view);
                startSubPage(view, MessagePushActivity.class);
                break;
            case CONFERENCE_ORGANIZING:
                testCell(view);
                startSubPage(view, ConferenceOrganizingActivity.class);
                break;
            case MESSAGE_WALL:
                testCell(view);
                startSubPage(view, MessageWallActivity.class);
                break;
            default:
                break;
        }
    }

    private void initCellIdTextMap() {
        mCellIdTextMap = new HashMap<Integer, List>();
        mCellIdTextMap.put(CLUB_ACCESS, Arrays.asList(new String("入团申请"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(CLUB_CREATION, Arrays.asList(new String("社团创建"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(CLUB_QUERY, Arrays.asList(new String("社团查询"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(MEMBER_ADDITION, Arrays.asList(new String("成员添加"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(MEMBER_DELETION, Arrays.asList(new String ("成员删除"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(STAFF_POWER, Arrays.asList(new String("干事权限"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(ORGANIZATION, Arrays.asList(new String("组织结构"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(CLUB_DISSOLUTION, Arrays.asList(new String("社团解除"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(ACTIVITY_REQUEST, Arrays.asList(new String ("活动申请"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(NEWS_PUSH, Arrays.asList(new String("消息推送"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(CONFERENCE_ORGANIZING, Arrays.asList(new String("会议组织"), R.drawable.ic_mood_black_24dp));
        mCellIdTextMap.put(MESSAGE_WALL, Arrays.asList(new String("消息墙"), R.drawable.ic_mood_black_24dp));

//        mCellIdTextMap.put(CLUB_CREATION, "社团创建");
//        mCellIdTextMap.put(CLUB_QUERY, "社团查询");
//        mCellIdTextMap.put(MEMBER_ADDITION, "成员添加");
//        mCellIdTextMap.put(MEMBER_DELETION, "成员删除");
//        mCellIdTextMap.put(STAFF_POWER, "干事权限");
//        mCellIdTextMap.put(ORGANIZATION, "组织结构");
//        mCellIdTextMap.put(CLUB_DISSOLUTION, "社团解除");
//        mCellIdTextMap.put(ACTIVITY_REQUEST, "活动申请");
//        mCellIdTextMap.put(NEWS_PUSH, "消息推送");
//        mCellIdTextMap.put(CONFERENCE_ORGANIZING, "会议组织");
//        mCellIdTextMap.put(MESSAGE_WALL, "消息墙");
    }
}














