package com.project.fortuna.surveypdam.review;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.TSurvey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LihatDataSudahActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;

    private String[] menuItems = {""};
    private List<TSurvey> listSurvey;
    private List<TSurvey> tempListSurvey;
    private TextView txtTotal;
    private ListView reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_sudah);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Sudah Terbaca");
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initComponent();
    }

    private void initComponent() {

        txtTotal = (TextView) findViewById(R.id.txtViewTotal);
        reviewList = (ListView) findViewById(R.id.id_review_list_view);
        reviewList.setOnItemClickListener(new ListSurveySudahClickHandler());

        initListData();
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        initListData();

    }


    private void initListData() {
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        String userid = sharedPreferences.getString("userid", "");
        String sTglSurvey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println(sTglSurvey);

        listSurvey = new TSurvey(LihatDataSudahActivity.this).retrieveForReview(userid, sTglSurvey, false, extras.getBoolean("isSukses"));
        tempListSurvey = listSurvey;

		/* CheckBox Sukses */
        if (extras.getBoolean("isSukses"))
            txtTotal.setText("Total Data = " + listSurvey.size());
        {
            if (listSurvey.size() > 0) {
                ReviewSurveyAdapter adapter = new ReviewSurveyAdapter(this, listSurvey);
                reviewList.setAdapter(adapter);
            } else {
                reviewList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                AlertDialog.Builder builder = new AlertDialog.Builder(LihatDataSudahActivity.this);
                builder.setTitle("Peringatan :")
                        .setMessage("Status " + getString(R.string.msg_no_data))
                        .setCancelable(false)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
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

    /* Menu Action Bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuviewsudah, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(LihatDataSudahActivity.this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(newText)) {

        } else {
            Bundle extras = getIntent().getExtras();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SurveyApp", 0);
            String userid = sharedPreferences.getString("userid", "");
            String sTglSurvey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tempListSurvey = listSurvey;
            List<TSurvey> ls = new TSurvey(LihatDataSudahActivity.this).retrieveForReview(userid, sTglSurvey, false, extras.getBoolean("isSukses"));
            ls.clear();
            for (TSurvey tb : listSurvey) {
                if (tb.getALMT_PASANG().toLowerCase().contains(newText.toLowerCase())) {
                    ls.add(tb);
                }
            }
            ReviewSurveyAdapter adapter = new ReviewSurveyAdapter(this, ls);
            tempListSurvey = ls;
            reviewList.setAdapter(adapter);
        }

        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("") || newText.isEmpty()) {
            initListData();
        }
        return false;

    }


    private class ListSurveySudahClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                final TSurvey lvm = tempListSurvey.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(LihatDataSudahActivity.this);
                builder.setTitle("Review Data : ");
                builder.setMessage("Lihat Detail Data Calon Pelanggan : "+ lvm.getNAMA() + " ?").setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Util.showmsg(LihatDataSudahActivity.this, getString(R.string.action_view), "No Registrasi = "
                                + lvm.getNO_REGISTER().replace("'", "") + "\n" + "Nama = " + lvm.getNAMA()
                                + "\n" + "Alamat = " + lvm.getALMT_PASANG()
                                + "\n" + "No HP = " + lvm.getNO_HP().replace("'", "")
                                + "\n" + "Jumlah Penghuni = " + lvm.getJ_PGHN_RMH()
                                + "\n" + "Status Pipa = " + lvm.getS_PIPA()
                                + "\n" + "Panjang Pipa = " + lvm.getPJG_PIPA()
                                + "\n" + "Diameter Pipa = " + lvm.getDIA_PIPA()
                                + "\n" + "Luas Bangunan = " + lvm.getLB()
                                + "\n" + "Status Bangunan = " + lvm .getGOL_BANGUNAN()
                                + "\n" + "Status = " + lvm.getS_SURVEY()
                                + "\n" + "Tgl Daftar = " + lvm.getTGL_DAFTAR());
                    }
                });

                builder.setNegativeButton("Tidak", null).show();

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(LihatDataSudahActivity.this, getString(R.string.action_view), e.toString());

            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
