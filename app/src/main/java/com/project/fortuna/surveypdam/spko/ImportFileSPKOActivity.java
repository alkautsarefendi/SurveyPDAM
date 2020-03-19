package com.project.fortuna.surveypdam.spko;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.project.fortuna.surveypdam.R;

public class ImportFileSPKOActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLocal, btnNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_file_spko);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Masukan File SPKO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLocal = (Button)findViewById(R.id.btnLocal);
        btnNetwork = (Button)findViewById(R.id.btnNetwork);

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImportFileSPKOActivity.this, ImportSPKOLocalActivity.class));
            }
        });

        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImportFileSPKOActivity.this, ImportSPKONetworkActivity.class));
            }
        });
    }
}
