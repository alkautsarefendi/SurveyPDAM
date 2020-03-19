package com.project.fortuna.surveypdam.config;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.fortuna.surveypdam.ConnectivityDB;
import com.project.fortuna.surveypdam.ConnectivityWS;
import com.project.fortuna.surveypdam.FortunaUtils;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.MBangunan;
import com.project.fortuna.surveypdam.dao.MJalan;
import com.project.fortuna.surveypdam.dao.MKonfigurasi;
import com.project.fortuna.surveypdam.dao.MUser;
import com.project.fortuna.surveypdam.dao.MWilayah;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class KonfigurasiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    protected EditText txtUser;
    protected Button btnSyncUser, btnSyncWilayah, btnSyncBlok, btnSyncBangunan, btnSimpanConfig, btnCancelConfig;
    private TextView inputIP;
    Snackbar snackbar;
    private static final String TAG = KonfigurasiActivity.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfigurasi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Konfigurasi");
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

        isInternetOn();
        initComponent();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponent() {
        txtUser = (EditText) findViewById(R.id.txtInputIP);
        inputIP = (TextView) findViewById(R.id.text_dummy_hint_inputIP);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutKonfig);

        txtUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            inputIP.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (txtUser.getText().length() > 0)
                        inputIP.setVisibility(View.VISIBLE);
                    else
                        inputIP.setVisibility(View.INVISIBLE);
                }
            }
        });

        /* Button Sync User */
        btnSyncUser = (Button) findViewById(R.id.btnSyncUser);
        btnSyncUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    SyncUser syncUser = new SyncUser();
                    syncUser.execute();
                }

            }
        });

        /* Button Sync Wilayah */
        btnSyncWilayah = (Button) findViewById(R.id.btnSyncWilayah);
        btnSyncWilayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    SyncWilayah syncWilayah = new SyncWilayah();
                    syncWilayah.execute();
                }
            }
        });


		/* Button Sync Blok */
        btnSyncBlok = (Button) findViewById(R.id.btnSyncBlok);
        btnSyncBlok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    SyncBlok syncBlok = new SyncBlok();
                    syncBlok.execute();
                }
            }
        });


		/* Button Sync Bangunan */
        btnSyncBangunan = (Button) findViewById(R.id.btnSyncBangunan);
        btnSyncBangunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    SyncBangunan syncBangunan = new SyncBangunan();
                    syncBangunan.execute();
                }
            }
        });


		/* Button Simpan */
        btnSimpanConfig = (Button) findViewById(R.id.btnSimpanConfig);
        btnSimpanConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Mohon melakukan konfigurasi dulu", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    inputData();
                    clearText();
                }
            }
        });


		/* Button Cancel */
        btnCancelConfig = (Button) findViewById(R.id.btnBackConfig);
        btnCancelConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();

    }

    private void initData(){

        MKonfigurasi mk = new MKonfigurasi(getApplicationContext());
        //mk.kdKonfigurasi = "1";
        mk = mk.retrieveByID();
        if(mk != null){
            txtUser.setText(mk.userURL);
        }else{
            txtUser.setText("");
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncUser extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String userURL = String.valueOf(txtUser.getText());
            String url = "http://" + userURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncUser&PDAMID=PDAMCoreV2";
            MUser domain = new MUser(getApplicationContext());
            domain.setImei(FortunaUtils.getDeviceID(KonfigurasiActivity.this));
            JSONObject object = ConnectivityWS.postToServer(domain, url);
            System.out.println("object = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                domain.delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MUser muser = domain;
                        JSONObject juser = jsonArray.getJSONObject(i);
                        Iterator keys = juser.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("USER_ID")) {
                                    muser.setUserid(String.valueOf(juser.get(key)));
                                } else {
                                    muser.getClass().getDeclaredField(FortunaUtils.formatField(key)).set(muser, String.valueOf(juser.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        muser.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncWilayah extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String wilayahURL = String.valueOf(txtUser.getText());
            String url = "http://" + wilayahURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayah&PDAMID=PDAMCoreV2";
            MWilayah domain = new MWilayah(getApplicationContext());
            JSONObject object = ConnectivityWS.postToServer(domain, url);
            System.out.println("object = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new MWilayah(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MWilayah mwilayah = domain;
                        JSONObject jwilayah = jsonArray.getJSONObject(i);
                        Iterator keys = jwilayah.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("KD_WILAYAH")) {
                                    mwilayah.setKdWilayah(String.valueOf(jwilayah.get(key)));
                                } else {
                                    mwilayah.getClass().getDeclaredField(FortunaUtils.formatField(key)).set(mwilayah, String.valueOf(jwilayah.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mwilayah.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncBangunan extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String bangunanURL = String.valueOf(txtUser.getText());
            String url = "http://"+bangunanURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncBangunan&PDAMID=PDAMCoreV2";
            MBangunan domain = new MBangunan(getApplicationContext());
            JSONObject object = ConnectivityWS.postToServer(domain, url);
            System.out.println("object = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new MBangunan(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MBangunan mbangunan = domain;
                        JSONObject jwilayah = jsonArray.getJSONObject(i);
                        Iterator keys = jwilayah.keys();
                        while (keys.hasNext()) {
                            try{
                                String key = ((String) keys.next()).trim();
                                mbangunan.getClass().getDeclaredField(key).set(mbangunan, String.valueOf(jwilayah.get(key)));
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        mbangunan.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncBlok extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String blokURL = String.valueOf(txtUser.getText());
            String url = "http://"+blokURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncJalan&PDAMID=PDAMCoreV2";
            MJalan domain = new MJalan(getApplicationContext());
            JSONObject object = ConnectivityWS.postToServer(domain, url);
            System.out.println("object = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new MBangunan(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MJalan mjalan = domain;
                        JSONObject jjalan = jsonArray.getJSONObject(i);
                        Iterator keys = jjalan.keys();
                        while (keys.hasNext()) {
                            try{
                                String key = ((String) keys.next()).trim();
                                if(key.equals("KD_JALAN")){
                                    mjalan.setKdJalan(String.valueOf(jjalan.get(key)));
                                }else{
                                    mjalan.getClass().getDeclaredField(FortunaUtils.formatField(key)).set(mjalan, String.valueOf(jjalan.get(key)));
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        mjalan.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }



    /* Input Data */
    private void inputData() {

        try{
            MKonfigurasi mk = new MKonfigurasi(getApplicationContext());

            String userURL = String.valueOf(txtUser.getText());
            String urlUser = "http://"+userURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncJalan&PDAMID=PDAMCoreV2";
            String wilayahURL = String.valueOf(txtUser.getText());
            String urlWIlayah = "http://"+wilayahURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayah&PDAMID=PDAMCoreV2";
            String blockURL = String.valueOf(txtUser.getText());
            String urlBlock = "http://"+blockURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncJalan&PDAMID=PDAMCoreV2";
            String bangunanURL = String.valueOf(txtUser.getText());
            String urlBangunan = "http://"+bangunanURL+"/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncBangunan&PDAMID=PDAMCoreV2";
            String downloadURL = String.valueOf(txtUser.getText());
            String urlDownload = "http://"+downloadURL+"/pdamws/pdam/corev2?WEBCMD=DownloadSPKO&PDAMID=PDAMCoreV2";
            String uploadURL = String.valueOf(txtUser.getText());
            String urlUpload = "http://"+uploadURL+"/pdamws/pdam/corev2?WEBCMD=Survey&PDAMID=PDAMCoreV2";

            mk.kdKonfigurasi = "1";
            mk.userURL = urlUser;
            mk.wilayahURL = urlWIlayah;
            mk.jalanURL = urlBlock;
            mk.bangunanURL = urlBangunan;
            mk.downloadURL = urlDownload;
            mk.uploadURL = urlUpload;
            mk.realtimeURL= txtUser.getText().toString();

            mk.update();

            if (mk.retrieveByID() != null) {
                mk.update();
                System.out.println("dataupdate = ");
            } else {
                mk.save();
                System.out.println("datasave = ");
            }

            new AlertDialog.Builder(this)
                    .setTitle("Simpan Data")
                    .setMessage("Data konfigurasi telah disimpan")
                    .setNeutralButton("Ok ", null)
                    .show();
        }catch(Exception e){
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("Simpan Data")
                    .setMessage("Simpan data gagal karena :"+e.getMessage())
                    .setNeutralButton("Ok ", null)
                    .show();
        }
    }

    /* Clear All text */
    private void clearText (){

        txtUser.setText("");

    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            Log.e(TAG, "Connected");
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(KonfigurasiActivity.this);

            alertDialog.setMessage("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            KonfigurasiActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(new Intent(KonfigurasiActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }

}
