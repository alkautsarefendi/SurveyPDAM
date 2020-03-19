package com.project.fortuna.surveypdam.export;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.project.fortuna.surveypdam.R;

public class ExportFileSurveyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnKomputer, btnJaringan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_file_survey);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export File");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnJaringan = (Button)findViewById(R.id.btnDariJaringan);
        btnKomputer = (Button)findViewById(R.id.btnDariKomputer);

        btnJaringan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExportFileSurveyActivity.this, ExportDariJaringanActivity.class));
            }
        });

        btnKomputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExportFileSurveyActivity.this, ExportDariKomputerActivity.class));
            }
        });
    }
}
