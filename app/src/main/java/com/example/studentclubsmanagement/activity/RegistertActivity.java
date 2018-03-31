package com.example.studentclubsmanagement.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.studentclubsmanagement.R;
import com.example.studentclubsmanagement.customview.MyTextInputLayout;
import com.example.studentclubsmanagement.util.HttpUtil;
import com.example.studentclubsmanagement.util.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistertActivity extends BaseActivity {

    private static final String TAG = "RegistertActivity";
    private ViewHolder mHolder;
    private String mUrlPrefix;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mHolder = new ViewHolder();
    }

    class ViewHolder {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_register_toolbar);
        MyTextInputLayout studentCode = (MyTextInputLayout) findViewById(R.id.register_student_code);
        MyTextInputLayout name = (MyTextInputLayout) findViewById(R.id.register_name);
        MyTextInputLayout password = (MyTextInputLayout) findViewById(R.id.register_password);
        MyTextInputLayout passwordConfirmed = (MyTextInputLayout) findViewById(R.id.register_confirm_password);
        MyTextInputLayout email = (MyTextInputLayout) findViewById(R.id.register_email);
        MyTextInputLayout phone = (MyTextInputLayout) findViewById(R.id.register_phone);
        FloatingActionButton handUpBtn = (FloatingActionButton) findViewById(R.id.register_hand_up_btn);

        private ViewHolder() {
            studentCode.getEditText().setText("1407300306");
            name.getEditText().setText("跑得快");
            password.getEditText().setText("paodekuai");
            passwordConfirmed.getEditText().setText("paodekuai");
            email.getEditText().setText("");
            phone.getEditText().setText("14789632541");

            initToolbar(toolbar);
            handUpBtn.setOnClickListener(new OnClickListenerImpl());
        }

        private void initToolbar(Toolbar toolbar) {
            this.toolbar = (Toolbar) findViewById(R.id.activity_register_toolbar);
            setSupportActionBar(this.toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("注册账号");
            }
        }
    }

    class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.register_hand_up_btn:
                    mUrlPrefix = HttpUtil.getUrlPrefix(mContext);
                    String url = mUrlPrefix + "/controller/UserSignUpServlet";
                    String studentCode = mHolder.studentCode.getEditText().getText().toString();
                    String name = mHolder.name.getEditText().getText().toString();
                    String password = mHolder.password.getEditText().getText().toString();
                    String passwordConfirmed = mHolder.passwordConfirmed.getEditText().getText().toString();
                    String email = mHolder.email.getEditText().getText().toString();
                    String phone = mHolder.phone.getEditText().getText().toString();
                    if (!password.equals(passwordConfirmed)) {
                        showToastOnUI("两次密码不一致");
                        break;
                    }
                    RequestBody body = new FormBody.Builder()
                            .add("student_code", studentCode)
                            .add("name", name)
                            .add("password", password)
                            .add("email", email)
                            .add("phone", phone)
                            .build();
                    HttpUtil.sendOkHttpRequestWithPost(url, body, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            LogUtil.d(TAG, "post success");
                            int userId = Integer.parseInt(response.body().string());
                            // TODO 注册失败逻辑
                            if (userId == 0) {
                                showToastOnUI("注册失败");
                                return;
                            } else if (userId == -1) {
                                showToastOnUI("账号已经存在");
                                return;
                            }
                            // TODO 持久化user_id
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putInt("user_id", userId);
                            editor.apply();
                            // TODO 启动主页
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    break;
            }
        }

        private void showToastOnUI(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
