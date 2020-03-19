package com.project.fortuna.surveypdam.export;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.project.fortuna.surveypdam.ConnectivityWS;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.MKonfigurasi;
import com.project.fortuna.surveypdam.dao.TSurvey;

import org.json.JSONObject;

import java.util.List;

public class ExportDariJaringanActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private List<TSurvey> listSurvey;
    private TSurvey selectedSurvey;
    private Button btnKirimSemua;
    private ListView listExport;

    private int flag;

    protected String userid;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_dari_jaringan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export File Network");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pref = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        userid = pref.getString("userid", "");

        initComponent();

    }

    private void initComponent() {

        listExport = (ListView) findViewById(R.id.id_export_list_view);

        listSurvey = new TSurvey(ExportDariJaringanActivity.this).retrieveForExportServer(userid);
        if(listSurvey.size() > 0){
            ExportFileSurveyAdapter adapter = new ExportFileSurveyAdapter(this, listSurvey);
            listExport.setAdapter(adapter);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ExportDariJaringanActivity.this);
            builder.setTitle("Peringatan :")
                    .setMessage("Status " + getString(R.string.msg_no_data))
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


		/* Button Kirim */
        btnKirimSemua = (Button)findViewById(R.id.btnSendChecked);
        btnKirimSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String allnoreg = "";
                    for(int i = 0;i<listExport.getCount();i++){
                        View vg = listExport.getChildAt(i);
                        CheckBox cb = (CheckBox) vg.findViewById(R.id.cb);
                        if(cb.isChecked()){
                            TSurvey m = listSurvey.get(i);
                            allnoreg += m.getNO_REGISTER();
                        }
                    }
                    if(allnoreg!=""){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ExportDariJaringanActivity.this);
                        builder.setTitle("Peringatan : ");
                        builder.setMessage("Apakah Anda Ingin Mengirim Data Dengan No. Reg : "+ allnoreg.replace("'", "") + " ke Server ?").setCancelable(false);
                        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                MKonfigurasi km = new MKonfigurasi(getApplicationContext());
                                km.kdKonfigurasi = "1";
                                km = km.retrieveByID();
                                flag = 0;
                                if(km != null && km.uploadURL != null && !km.uploadURL.trim().equals("")){
                                    for(int i = 0;i<listExport.getCount();i++){
                                        View vg = listExport.getChildAt(i);
                                        CheckBox cb = (CheckBox) vg.findViewById(R.id.cb);
                                        if(cb.isChecked()){
                                            selectedSurvey = listSurvey.get(i);
                                            selectedSurvey.NO_REGISTER = selectedSurvey.NO_REGISTER.replace("'", "");
                                            try{
                                                JSONObject response = ConnectivityWS.postSurvey(selectedSurvey, km.uploadURL, Integer.valueOf(pref.getString("kdWilayah", "")));
                                                if(response.getString("CODE").equals("00")){
                                                    selectedSurvey.S_KIRIM = "1";
                                                    selectedSurvey.NO_REGISTER = "'"+selectedSurvey.NO_REGISTER;
                                                    selectedSurvey.update();
                                                    flag++;
                                                }
                                                else throw new Exception("Gagal terkirim");
                                            }catch(Exception e){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ExportDariJaringanActivity.this);
                                                builder.setTitle("Export To Network")
                                                        .setMessage("Gagal terkirim")
                                                        .setCancelable(false)
                                                        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.setCancelable(false);
                                                alert.show();
                                            }
                                        }
                                    }
                                }else{
                                    Util.showmsg(ExportDariJaringanActivity.this, "Export ke server", "Konfigurasi url belum tersedia.");
                                }
                                if(flag>0){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ExportDariJaringanActivity.this);
                                    builder.setTitle("Export To Network")
                                            .setMessage(flag+" Sukses Dikirim")
                                            .setCancelable(false)
                                            .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setCancelable(false);
                                    alert.show();
                                }
                            }
                        });

                        builder.setNegativeButton("Batal", null).show();
                    }else{
                        throw new Exception("Tidak ada data yang dipilih");
                    }
                }catch(Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExportDariJaringanActivity.this);
                    builder.setTitle("Peringatan : ");
                    builder.setMessage(e.getMessage()).setCancelable(false);
                    builder.setNegativeButton("Batal", null).show();
                }
            }
        });
    }
}
