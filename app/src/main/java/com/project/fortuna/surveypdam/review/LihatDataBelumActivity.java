package com.project.fortuna.surveypdam.review;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.fortuna.surveypdam.activity.InputSurveyActivity;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.TSurvey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LihatDataBelumActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private String[] menuItems = { "" };
    private List<TSurvey> listSurvey;
    private List<TSurvey> tempListSurvey;
    private TextView txtTotal;
    private ListView reviewList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_belum);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Belum Terbaca");
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

        initComponent();
    }

    private void initComponent() {

        txtTotal = (TextView) findViewById(R.id.txtViewTotal);
        reviewList = (ListView) findViewById(R.id.id_review_list_view);
        reviewList.setOnItemClickListener(new ListSurveyBelumClickHandler());

        initListData();
    }


    @Override
    public void onResume(){  // After a pause OR at startup
        super.onResume();
        initListData();

    }

    @SuppressLint("SetTextI18n")
    private void initListData() {
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        String userid = sharedPreferences.getString("userid", "");
        String sTglSurvey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println(sTglSurvey);

        listSurvey = new TSurvey(LihatDataBelumActivity.this).retrieveForReview(userid, sTglSurvey, extras.getBoolean("isBelum"), false);
        tempListSurvey = listSurvey;

		/* CheckBox Belum */
        if (extras.getBoolean("isBelum"))
            txtTotal.setText("Total Data = " + listSurvey.size());
        {
            if (listSurvey.size() > 0) {
                ReviewSurveyAdapter adapter = new ReviewSurveyAdapter(this, listSurvey);
                reviewList.setAdapter(adapter);
            } else {
                reviewList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                AlertDialog.Builder builder = new AlertDialog.Builder(LihatDataBelumActivity.this);
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



    /* Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuviewbelum, menu);
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(LihatDataBelumActivity.this);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onQueryTextSubmit(String query)
    {
        // this is your adapter that will be filtered
        if (query.isEmpty() || query.equals(""))
        {

        }
        else
        {
            Bundle extras = getIntent().getExtras();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SurveyApp", 0);
            String userid = sharedPreferences.getString("userid", "");
            String sTglSurvey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tempListSurvey = listSurvey;
            List<TSurvey> ls = new TSurvey(LihatDataBelumActivity.this).retrieveForReview(userid, sTglSurvey, extras.getBoolean("isBelum"), false);
            ls.clear();
            for(TSurvey tb:tempListSurvey){
                if(tb.getALMT_PASANG().toLowerCase().contains(query.toLowerCase())){
                    ls.add(tb);
                }
            }
            ReviewSurveyAdapter adapter = new ReviewSurveyAdapter(this, ls);
            tempListSurvey = ls;
            reviewList.setAdapter(adapter);
        }

        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("") || newText.isEmpty()) {
            initListData();
        }
        return false;

    }

    private class ListSurveyBelumClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                final TSurvey lvm = tempListSurvey.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(LihatDataBelumActivity.this);
                builder.setTitle("Input Data : ");
                builder.setMessage("Mulai Melakukan Survey Pelanggan : "+ lvm.getNAMA() + " ?").setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(LihatDataBelumActivity.this, InputSurveyActivity.class);
                        intent.putExtra("key", lvm.getNO_REGISTER().replace("'", ""));
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Tidak", null).show();

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(LihatDataBelumActivity.this, getString(R.string.action_view), e.toString());

            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
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
