package com.project.fortuna.surveypdam.export;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.TSurvey;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportDariKomputerActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CustomCalendarView calendarView;
    protected EditText mEditText;
    protected Button btnExport, btnExportCancel;
    protected String userid;
    protected String username;
    private String tgl, bulan, tahun;
    protected SharedPreferences pref;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/CSV/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_dari_komputer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export File Local");
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
        username = pref.getString("username", "");

        initComponent();
    }

    private void initComponent() {

        btnExportCancel = (Button) findViewById(R.id.btnExportCanceltoLocal);
        btnExportCancel.setOnClickListener(new ButtonExportCancelClickHandler());
        btnExport = (Button) findViewById(R.id.btnExporttoLocal);
        btnExport.setOnClickListener(new ButtonExportClickHandler());

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        mEditText = (EditText) findViewById(R.id.txtExportFile);


        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH
                        + " on sdcard failed");
            } else {
                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");
            }
        }

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat tf = new SimpleDateFormat("dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat mf = new SimpleDateFormat("MM");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat yf = new SimpleDateFormat("yyyy");
                tgl = tf.format(date);
                bulan = mf.format(date);
                tahun = yf.format(date);
                Toast.makeText(ExportDariKomputerActivity.this, String.valueOf(tgl + "-" + bulan + "-" + tahun), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
                Toast.makeText(ExportDariKomputerActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

    }


    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
            }
        }
    }


    /* Button Cancel */
    public class ButtonExportCancelClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            finish();
        }
    }


    /* Button Export */
    public class ButtonExportClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            exportData();
        }
    }


    /*
     * AGS 02072013
	 */
    private void exportData() {

        String sTglSurvey = tahun + "-" + bulan + "-" + tgl;
        System.out.println(sTglSurvey);
        String tglExport = sTglSurvey.replaceAll("-", "");
        String sFileName = DATA_PATH+ "S" + "_" +username+ "_" + tglExport + ".CSV";
        String editTextField = "S" + "_" + username + "_" + tglExport + ".csv";
        String sLine = "";

        TSurvey ds2 = new TSurvey(this);
        List<TSurvey> retrieveForExport = ds2.retrieveForExport(userid, sTglSurvey);

        if(retrieveForExport.size() == 0){
            Util.showmsg(this, getString(R.string.action_export), getString(R.string.msg_export_notok) + ", Belum ada.");
        }else{
            for (TSurvey bm : retrieveForExport) {
                sLine = sLine + bm.getNO_REGISTER().replace("'", "") + ",";
                sLine = sLine + bm.getUSER_ID() + ",";

                if (bm.getNAMA().contains(",") && !bm.getNAMA().contains("\""))
                    sLine = sLine + "\"" + bm.getNAMA() + "\",";
                else
                    sLine = sLine + bm.getNAMA() + ",";

                if (bm.getALMT_PASANG().contains(","))
                    sLine = sLine + "\"" + bm.getALMT_PASANG() + "\",";
                else
                    sLine = sLine + bm.getALMT_PASANG() + ",";

                sLine = sLine + bm.getNO_HP().replace("'", "") + ",";
                sLine = sLine + bm.getS_PIPA() + ",";
                sLine = sLine + bm.getPJG_PIPA() + ",";
                sLine = sLine + bm.getDIA_PIPA() + ",";
                sLine = sLine + bm.getJ_PGHN_RMH() + ",";
                sLine = sLine + bm.getLB() + ",";
                sLine = sLine + pref.getString("kdWilayah", "") + ",";
                sLine = sLine + bm.getGOL_RUMAH() + ",";
                sLine = sLine + (bm.getGOL_BANGUNAN() != null ? bm.getGOL_BANGUNAN().trim() :"") + ",";
                sLine = sLine + bm.getS_SURVEY() + ",";
                sLine = sLine + bm.getFOTO() + ",";
                sLine = sLine + bm.getFOTO_LAT() + ",";
                sLine = sLine + bm.getFOTO_LONG() + ",";
                sLine = sLine + bm.getTGL_SURVEY() + "\n";
            }

            System.out.println(sLine);

            boolean writeFile = Util.writeFile(sFileName, sLine);
            if(writeFile){
                Util.showmsg(this, "Export File", "Sukses export "+retrieveForExport.size()+" Calon Pelanggan.");
                mEditText.setText(editTextField);
                for (TSurvey bm : retrieveForExport) {
                    bm.S_KIRIM = "1";
                    bm.update();
                }
            }else{
                Util.showmsg(this, "Import File", "Gagal import file to " +sFileName);
            }
        }
    }


    /* Clear */
    public void clearExport() {
        mEditText.setText("");
    }



}
