package com.example.studentclubsmanagement.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.example.studentclubsmanagement.R;

public class ClubOrganizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_organization);

        initToolbar();
        initMinister();
        initViceMinister();
        initStaff();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.club_organization_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("组织结构");
        }
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

    private void initMinister() {
        LinearLayout minister = (LinearLayout) findViewById(R.id.minister);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.club_organization_item, minister, false);
        minister.addView(item);

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
    }

    private void initViceMinister() {
    }

    private void initStaff() {
    }
}
