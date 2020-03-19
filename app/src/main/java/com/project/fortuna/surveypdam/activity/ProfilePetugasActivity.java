package com.project.fortuna.surveypdam.activity;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.dao.MUser;

import java.io.File;
import java.util.List;

public class ProfilePetugasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<MUser> listUser;
    private SharedPreferences pref;

    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/Survey/Icon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_petugas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Petugas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("SurveyApp", 0);

        // init
        File dir = new File(ICON_DEFAULT);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + ICON_DEFAULT + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + ICON_DEFAULT + " on sdcard");
            }
        }

        initComponent();
    }

    private void initComponent() {

        String userid = pref.getString("userid", "");
        MUser mu = new MUser(getApplicationContext());
        mu = mu.retrieveForUserHome(userid);

        initListData();

    }


    private void initListData() {

        //		Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        String userid = sharedPreferences.getString("userid", "");

        listUser = new MUser(ProfilePetugasActivity.this).retrieveForUser(userid);

        ProfileSurveyAdapter profileadapter = new ProfileSurveyAdapter(this, listUser);
        ListView androidListView = (ListView) findViewById(R.id.list_view_profile);
        androidListView.setAdapter(profileadapter);

    }
}
