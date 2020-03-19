package com.project.fortuna.surveypdam.spko;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.project.fortuna.surveypdam.ConnectivityWS;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.DefaultDTO;
import com.project.fortuna.surveypdam.dao.MKonfigurasi;
import com.project.fortuna.surveypdam.dao.TSurvey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportSPKONetworkActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/CSV/";

    private ListView listView;
    private ListAdapter adapter;
    private List<TSurvey> listSurvey = new ArrayList<TSurvey>();

    private Button btnImportFile, btnCancelDownload;
    Handler updateBarHandler;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_spkonetwork);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Import File Network");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateBarHandler = new Handler();

        initComponent();
    }

    private void initComponent() {

        btnImportFile = (Button) findViewById(R.id.btnImportFile);
        btnImportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listSurvey.isEmpty()){
                    boolean flag=false;
                    try{
                        for (TSurvey ts : listSurvey) {
                            ts.NO_HP = "'"+ts.NO_HP;
                            ts.NO_REGISTER = "'"+ts.NO_REGISTER;
                            if(ts.retrieveByID() != null ){
                                ts.update();
                                flag=true;
                            }else{
                                ts.save();
                            }
                        }
                    }catch(Exception e){
                        Util.showmsg(getApplicationContext(), "Peringatan :", "Data Gagal di Import");
                    }
                    if(!flag){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImportSPKONetworkActivity.this);
                        builder.setTitle("Import From Network")
                                .setMessage("Import Data Berhasil")
                                .setCancelable(false)
                                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImportSPKONetworkActivity.this);
                        builder.setTitle("Import From Network")
                                .setMessage("Data Berhasil diperbaharui")
                                .setCancelable(false)
                                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.show();
                    }
                }
            }
        });

        btnCancelDownload = (Button) findViewById(R.id.btnCancelDownload);
        btnCancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {

                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH
                        + " on sdcard failed");
                return;
            } else {

                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");

            }
        }

        MKonfigurasi km = new MKonfigurasi(getApplicationContext());
        km.kdKonfigurasi = "1";
        km = km.retrieveByID();
        /*String karambia = km.
        System.out.println("kanciang = " +km);*/

        if(km != null && !km.getDownloadURL().trim().equals("")){
            listView = (ListView) findViewById(R.id.id_import_list_view_server);
            adapter = new ImportSPKOAdapter(this, listSurvey) ;
            listView.setAdapter(adapter);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            SharedPreferences pref = getApplicationContext().getSharedPreferences("SurveyApp", 0); // 0 - for private mode

            DefaultDTO dto = new DefaultDTO(getApplicationContext());
            dto.USERID = pref.getString("userid", "");
            JSONObject response = ConnectivityWS.postToServer( dto, km.getDownloadURL());
            try {
                if(response != null && response.has("CODE") && response.get("CODE").equals("00")){
                    JSONArray jsonArray = response.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        System.out.println(jsonArray.getJSONObject(i).getString("LUAS_PERSIL"));
                        TSurvey tSurvey = new TSurvey(getApplicationContext());
                        tSurvey.ALMT_PASANG = jsonArray.getJSONObject(i).getString("ALMT_PASANG");
                        tSurvey.LB = ""+jsonArray.getJSONObject(i).getString("LUAS_PERSIL");
                        tSurvey.USER_ID = jsonArray.getJSONObject(i).getString("SPKO_USERID");
                        tSurvey.NO_HP = jsonArray.getJSONObject(i).getString("NO_TELP");
                        tSurvey.NO_REGISTER = jsonArray.getJSONObject(i).getString("KD_DAFTAR");
                        tSurvey.NAMA = jsonArray.getJSONObject(i).getString("PEMILIK");
                        tSurvey.J_PGHN_RMH = ""+jsonArray.getJSONObject(i).getString("JML_PENGHUNI");
                        tSurvey.TGL_DAFTAR = jsonArray.getJSONObject(i).getString("TGL_DAFTAR");
                        tSurvey.S_SURVEY = "-1";
                        tSurvey.S_KIRIM = "0";
                        listSurvey.add(tSurvey);
                    }
                    hidePDialog();
                }else{
                    hidePDialog();
                    if(response == null)
                        Util.showmsg(ImportSPKONetworkActivity.this, "Peringatan !", "Permintaan gagal");
                    else
                        Util.showmsg(ImportSPKONetworkActivity.this, "Peringatan !", "Permintaan gagal, response is "+response.getString("CODE"));
                }
            } catch (Exception e) {
                hidePDialog();
                e.printStackTrace();
                Util.showmsg(ImportSPKONetworkActivity.this, "Peringatan !", "Permintaan gagal, error is "+e.getMessage());
            }
        }else{
            Util.showmsg(ImportSPKONetworkActivity.this, "Peringatan !", "Silahkan konfigurasi url download terlebih dahulu");
        }
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
