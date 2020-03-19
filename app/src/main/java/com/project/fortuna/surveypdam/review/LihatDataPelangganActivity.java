package com.project.fortuna.surveypdam.review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.project.fortuna.surveypdam.R;

public class LihatDataPelangganActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnBelum, btnSudah;
    private SharedPreferences pref;
    private Boolean viewData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_pelanggan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Review");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        btnBelum = (Button)findViewById(R.id.btnBelum);
        btnSudah = (Button)findViewById(R.id.btnSudah);
        btnBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewBelum = new Intent(LihatDataPelangganActivity.this, LihatDataBelumActivity.class);
                reviewBelum.putExtra("isBelum", viewData);
                startActivity(reviewBelum);
            }
        });

        btnSudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewSudah = new Intent(LihatDataPelangganActivity.this, LihatDataSudahActivity.class);
                reviewSudah.putExtra("isSukses", viewData);
                startActivity(reviewSudah);
            }
        });
    }
}
