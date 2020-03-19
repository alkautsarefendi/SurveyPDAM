package com.project.fortuna.surveypdam.config;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.fortuna.surveypdam.Cryptograph;
import com.project.fortuna.surveypdam.FontCache;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.activity.HalamanUtamaActivity;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.dao.MUser;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnKonfigurasi;
    protected EditText txtUser, txtPass;
    private TextView txtTitle;
    protected TextView tvBuild;

    protected static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/CSV/";
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/Foto/";
    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/Survey/Icon/";

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnKonfigurasi = (Button) findViewById(R.id.btnKonfigurasi);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtUser = (EditText) findViewById(R.id.txtUserLogin);
        txtPass = (EditText) findViewById(R.id.txtUserPassword);

        txtTitle.setTypeface(FontCache.getTypeface("roboto.light.ttf", LoginActivity.this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));
                /*login();*/
            }
        });

        btnKonfigurasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, KonfigurasiActivity.class));
            }
        });

        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");
            }
        }


        // init2
        File dir2 = new File(PHOTO_PATH);
        if (!dir2.exists()) {
            if (!dir2.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + PHOTO_PATH + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + PHOTO_PATH + " on sdcard");
            }
        }


        // init3
        File dir3 = new File(ICON_DEFAULT);
        if (!dir3.exists()) {
            if (!dir3.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + ICON_DEFAULT + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + ICON_DEFAULT + " on sdcard");
            }
        }

        isInternetOn();
    }

    private void login() {
        MUser user = new MUser(getApplicationContext());
        user = user.getUser(txtUser.getText().toString());
        System.out.println(user);
        if (user != null) {
            String decrypt = Cryptograph.getInstance().decrypt(user.getPassword());
            if (decrypt.equals(txtPass.getText().toString())) {

                //TODO Save data user ke session
                SharedPreferences pref = getApplicationContext().getSharedPreferences("SurveyApp", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userid", user.userid);
                editor.putString("username", user.username);
                editor.putString("nmPegawai", user.nmPegawai);
                editor.putString("nmJabatan", user.nmJabatan);
                editor.putString("kdWilayah", user.kdWilayah);
                editor.putString("nmWilayah", user.nmWilayah);
                editor.apply();

                Intent menuUtama = new Intent(LoginActivity.this, HalamanUtamaActivity.class);
                startActivity(menuUtama);
            } else {
                Util.showmsg(LoginActivity.this, "Peringatan :", "Password salah");
            }
        } else {
            Util.showmsg(LoginActivity.this, "Peringatan :", "User tidak ditemukan");
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

        alertDialog.setMessage("Keluar dari Aplikasi?");
        alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoginActivity.super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });

        alertDialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED){
            Log.e(TAG,"Connected");
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

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
                            LoginActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(intent);
                            finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }
}
