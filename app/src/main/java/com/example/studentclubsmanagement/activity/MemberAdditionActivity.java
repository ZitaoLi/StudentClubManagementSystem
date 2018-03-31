package com.example.studentclubsmanagement.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentclubsmanagement.Adapter.SpinnerArrayAdapter;
import com.example.studentclubsmanagement.R;

public class MemberAdditionActivity extends BaseActivity {

    private String[] mSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_addition);

        initToolbar();
        initSpinner();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.member_addition_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("成员添加");
        }
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.member_addition_sex_spinner);
        String[] sex = getResources().getStringArray(R.array.sex);
        mSex = sex;
        ArrayAdapter<String> adapter = new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_item, sex);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view.getContext(), mSex[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
}
