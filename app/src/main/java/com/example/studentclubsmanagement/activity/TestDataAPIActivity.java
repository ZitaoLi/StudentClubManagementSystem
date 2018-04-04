package com.example.studentclubsmanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.gson.ClubIdList;
import com.example.studentclubsmanagement.gson.Power;
import com.example.studentclubsmanagement.gson.PowerItem;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestDataAPIActivity extends BaseActivity {

    private final static String TAG = "TestDataAPIActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data_api);

        textView = (TextView) findViewById(R.id.test_data_api_test_view);

        Resources res = this.getResources();
        String server = res.getString(R.string.server);
        String rootPath = res.getString(R.string.root_path);

        String url = server + rootPath + "/controller/TestServlet";
//    HttpUtil.sendOkHttpRequestWithGet(url, new okhttp3.Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            LogUtil.d(TAG, e.toString());
//        }
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String json = response.body().string();
//            LogUtil.d(TAG, "get success");
////                showResponseData(json);
//        }
//    });
        url = server + rootPath + "/controller/ClubInfoServlet";
//        String json = "{\"club_id\":13}";
//        LogUtil.d(TAG, "json: " + json);
//        HttpUtil.sendOkHttpRequestWithPost(url, json, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d(TAG, e.toString());
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                LogUtil.d(TAG, "post success");
//                showResponseData(json);
//            }
//        });
        RequestBody body = new FormBody.Builder().add("club_id", "13").build();
//        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d(TAG, e.toString());
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                LogUtil.d(TAG, "post success");
//                showResponseData(json);
//            }
//        });

        Gson gson = new Gson();
        final String json = "{\"power\":[{\"power_item\":[1,2,3]},{\"power_item\":[1,2]},{\"power_item\":[1,2,3,4]},]}";
        LogUtil.d(TAG, "json: " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("power");
            String powerArray = jsonArray.getJSONObject(0).toString();
            LogUtil.d(TAG, "powerArray: " + powerArray);
            Power power = new Gson().fromJson(json, Power.class);
            LogUtil.d(TAG, "power: " + power);
            List<PowerItem> item1 =  power.getPower();
            int items[] = item1.get(1).getItems();
            int i = items[0];
            LogUtil.d(TAG, "i: " + String.valueOf(i));
            PowerItem powerItem = new Gson().fromJson(powerArray, PowerItem.class);
            LogUtil.d(TAG, "power_item: " + powerItem.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url = server + rootPath + "/controller/UserClubRelationServlet";
        body = new FormBody.Builder()
                .add("user_id", "1")
                .build();
        HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d(TAG, "post success");
                String data = response.body().string();
                LogUtil.d(TAG, data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
//                    JSONArray jsonArray = jsonObject.getJSONArray("club_id_list");
                    ClubIdList clubIdList = new Gson().fromJson(data, ClubIdList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showResponseData(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(json);
            }
        });
    }
}













