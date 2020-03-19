package com.project.fortuna.surveypdam.spko;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.TSurvey;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ImportSPKOLocalActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    protected static final int REQUEST_CODE_GET_CONTENT = 2;
    protected static final int DIALOG_LOAD_FILE = 1000;
    protected static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/CSV/";
    protected static final String TAG = "F_PATH";

    // Stores names of traversed directories
    private ArrayList<String> str = new ArrayList<String>();

    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;

    private Item[] fileList;
    private File path = new File(Environment.getExternalStorageDirectory().toString() + "/Survey/CSV/");
    private String chosenFile;
    private List<TSurvey> tSurvey = new ArrayList<TSurvey>();

    private ListAdapter adapter;

    private SharedPreferences pref;
    protected EditText mFileName;

    private Button btnImport, btnImportCancel, btnBrowse;
    ProgressDialog barProgressDialog;
    @SuppressWarnings("unused")
    private int progressBarStatus = 0;
    Handler updateBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_spkolocal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Import File Local");
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

        initComponent();
    }

    private void initComponent() {

        btnImportCancel = (Button) findViewById(R.id.btnImportCancel);
        btnImportCancel.setOnClickListener(new ButtonImportCancelClickHandler());
        btnImport = (Button) findViewById(R.id.btnImport);
        btnImport.setOnClickListener(new ButtonImportClickHandler());
        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new ButtonBrowseClickHandler());

        mFileName = (EditText) findViewById(R.id.txtfileLocal);

        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of Directory " + DATA_PATH
                        + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created Directory " + DATA_PATH + " on sdcard");
            }
        }
    }

    /* Button Cancel */
    public class ButtonImportCancelClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            finish();
        }
    }


    /* Button Browse */
    public class ButtonBrowseClickHandler implements View.OnClickListener {

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View view) {
            loadFilesOnPath();

            showDialog(DIALOG_LOAD_FILE);
            Log.d(TAG, path.getAbsolutePath());

        }

    }

    private void loadFilesOnPath() {
        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

        // Checks whether path exists
        if (path.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return (sel.isFile() || sel.isDirectory()) && !sel.isHidden();

                }
            };

            String[] fList = path.list(filter);
            fileList = new Item[fList.length];
            for (int i = 0; i < fList.length; i++) {
                fileList[i] = new Item(fList[i], R.drawable.file_icon);

                // Convert into file path
                File sel = new File(path, fList[i]);

                // Set drawables
                if (sel.isDirectory()) {
                    fileList[i].icon = R.drawable.directory_icon;
                    Log.d("DIRECTORY", fileList[i].file);
                } else {
                    Log.d("FILE", fileList[i].file);
                }
            }

            if (!firstLvl) {
                Item temp[] = new Item[fileList.length + 1];
                for (int i = 0; i < fileList.length; i++) {
                    temp[i + 1] = fileList[i];
                }
                temp[0] = new Item("Up", R.drawable.directory_up);
                fileList = temp;
            }
        } else {
            new AlertDialog.Builder(this).setTitle("Warning")
                    .setMessage("Path [SDCARD]/Survey/CSV not found.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

        adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, fileList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // creates view
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                // put the image on the text view
                textView.setCompoundDrawablesWithIntrinsicBounds(fileList[position].icon, 0, 0, 0);

                // add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                textView.setCompoundDrawablePadding(dp5);

                return view;
            }
        };

    }

    private class Item {
        public String file;
        public int icon;

        public Item(String file, Integer icon) {
            this.file = file;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return file;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (fileList == null) {
            Log.e(TAG, "No files loaded");
            dialog = builder.create();
            return dialog;
        }

        switch (id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenFile = fileList[which].file;
                        File sel = new File(path + "/" + chosenFile);
                        if (sel.isDirectory()) {
                            firstLvl = false;

                            // Adds chosen directory to list
                            str.add(chosenFile);
                            fileList = null;
                            path = new File(sel + "");

                            loadFilesOnPath();

                            removeDialog(DIALOG_LOAD_FILE);
                            showDialog(DIALOG_LOAD_FILE);
                            Log.d(TAG, path.getAbsolutePath());

                        }

                        // Checks if 'up' was clicked
                        else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

                            // present directory removed from list
                            String s = str.remove(str.size() - 1);

                            // path modified to exclude present directory
                            path = new File(path.toString().substring(0, path.toString().lastIndexOf(s)));
                            fileList = null;

                            // if there are no more directories in the list, then its the first level
                            if (str.isEmpty()) {
                                firstLvl = true;
                            }
                            loadFilesOnPath();

                            removeDialog(DIALOG_LOAD_FILE);
                            showDialog(DIALOG_LOAD_FILE);
                            Log.d(TAG, path.getAbsolutePath());

                        }
                        // File picked
                        else {
						/*
						Perform action with file picked
						mFilePath.setText(path.getAbsolutePath() + "/" + chosenFile);
						mFileName.setText(chosenFile);
						 */
                            mFileName.setText(chosenFile);
                            try{
                                importFileCSV();
                            }catch(Exception e){
                                Util.showmsg(ImportSPKOLocalActivity.this, "Peringatan :", "Error Import");
                            }
                        }

                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }


    /* Import CSV */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void importFileCSV() throws Exception {
        String filePath = path.getAbsolutePath() + "/" + chosenFile;
        Reader reader = new FileReader(filePath);

        CSVStrategy strategy = new CSVStrategy(',', '"', '#', false, true);
        CSVReader csvParser = new CSVReaderBuilder(reader).strategy(strategy).entryParser(new DefaultCSVEntryParser()).build();
        List<String[]> data = csvParser.readAll();
        tSurvey.clear();
        int i = 0;
        boolean flag = false;

        for (String[] sData : data) {
            if(sData.length == 8)
            {
                System.out.println("["+sData[3]+"]["+pref.getString("userid", "")+"");
                if(sData[3].equals(pref.getString("userid", "")))
                {
                    TSurvey ts = new TSurvey(getApplicationContext());
                    ts.NO_REGISTER = sData[0];
                    ts.NAMA = sData[1];
                    ts.ALMT_PASANG = sData[2];
                    ts.USER_ID = sData[3];
                    ts.NO_HP = "'"+sData[4];
                    ts.LB = sData[5];
                    ts.J_PGHN_RMH = sData[6];
                    ts.TGL_DAFTAR = sData[7];
                    ts.S_SURVEY = "-1";
                    ts.S_KIRIM = "0";

                    tSurvey.add(ts);
                } else {
                    //	Util.showmsg(ImportDSMLFromLocal.this, "Peringatan :", "User ID Tidak Sesuai, Data Tidak Dapat di Import");
                }
            }else{
                flag = true;
            }
            i++;
        }
        if(flag){
            Util.showmsg(this, "Peringatan :", "Data kolom tidak sesuai Sebanyak " + String.valueOf(i + 1)+" Baris");
        }

        ListView l = (ListView) findViewById(R.id.id_import_list_view);
        l.setAdapter(new ImportSPKOAdapter(ImportSPKOLocalActivity.this, tSurvey));

        Util.showmsg((Context) this, "Peringatan :", "Data yang di import : " + String.valueOf(tSurvey.size()));
    }


    /* Button Import */
    public class ButtonImportClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            boolean flag =false;

            if(!tSurvey.isEmpty()){
                try {
                    for (TSurvey ts : tSurvey) {
                        ts.NO_HP = "'"+ts.NO_HP;
                        ts.NO_REGISTER = "'"+ts.NO_REGISTER;
                        if(ts.retrieveByID() == null ){
                            ts.save();
                        }else{
                            ts.update();
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    Util.showmsg(getApplicationContext(), "Peringatan :", "Data Gagal di Import");
                }
                if(!flag){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ImportSPKOLocalActivity.this);
                    builder.setTitle("Import From Local")
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ImportSPKOLocalActivity.this);
                    builder.setTitle("Import From Local")
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
    }
}
