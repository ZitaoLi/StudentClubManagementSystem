package com.example.studentclubsmanagement.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studentclubsmanagement.R;

public class Newactivity extends BaseActivity implements View.OnClickListener {

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newactivity);

//        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.new_collapsing_toolbar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.new_floating_action_button);
        TextView textView = (TextView) findViewById(R.id.new_text_view);
        ImageView imageView = (ImageView) findViewById(R.id.new_image_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("Hello");
        textView.setText("nullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnull");
        Glide.with(this).load(R.drawable.test_img).into(imageView);

        floatingActionButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_floating_action_button:
                Toast.makeText(this, "position:" + mPosition, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
