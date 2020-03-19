package com.project.fortuna.surveypdam.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.fortuna.surveypdam.ConnectivityWS;
import com.project.fortuna.surveypdam.FortunaUtils;
import com.project.fortuna.surveypdam.GPSTracker;
import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.Util;
import com.project.fortuna.surveypdam.dao.MBangunan;
import com.project.fortuna.surveypdam.dao.MKonfigurasi;
import com.project.fortuna.surveypdam.dao.TSurvey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InputSurveyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static final String PACKAGE_NAME = "com.fortuna.android.survey";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/";
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/Foto/";

    /* Define component from layout */
    private EditText txtNoReg, txtUserId, txtNama, txtAlamat, txtNoHP, txtJmlHuni, txtPjgPipa, txtLB, txtGolTarif, txtTglPendaftaran;
    private Spinner spinPipa, spinDiaPipa, /*spinUkuranMeter,*/ spinGolRumah, spinStatusBangunan, spinStatus;
    private ImageView imgSurvey;
    private Button btnQRCode, btnCari, btnInput, btnReset, btnInputCancel, telButton, surveyButton;

    private String fileName;
    private SharedPreferences pref;
    private String sPathFoto;
    protected Uri outputFileUri = null;
    Uri output = null;
    private ScrollView surveyScrollView;

    String KD_DAFTAR, USERID, NAMA, ALAMAT, LUAS_BANGUNAN, PHOTO_RUMAH, LATITUDE, LONGITUDE, PANJANG_PIPA,
            DIAMETER_PIPA, JUMLAH_PENGHUNI, TANGGAL_SURVEY, STATUS_SURVEY, KD_WILAYAH, GOL_BANGUNAN,
            GOL_RUMAH;
    String periodeSurvey = new SimpleDateFormat("yyyyMMdd").format(new Date());

    InputStream is = null;
    String result = null;
    String line = null;
    public boolean _taken;

    // GPSTracker class
    GPSTracker gps;
    private double latitude = 0.0;
    private double longitude = 0.0;


    private static final int PHOTO = 1;
    private static final int SCANNER_QRCODE = 2;
    public static final int AVS_MOST_ACCURATE = 100;
    protected static final String PHOTO_TAKEN = "photo_taken";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_survey);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Survey");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("SurveyApp", 0);
        getGPSFoto();

        initComponent();
        doInitData();
        doFromReview();
    }

    private void initComponent() {

        surveyScrollView = (ScrollView) findViewById(R.id.surveyScrollView);

        btnQRCode = (Button) findViewById(R.id.btnQRCode);
        btnQRCode.setOnClickListener(new ButtonQRCodeClickHandler());

        btnInputCancel = (Button) findViewById(R.id.btnInputCancel);
        btnInputCancel.setOnClickListener(new ButtonInputCancelClickHandler());

        btnInput = (Button) findViewById(R.id.btnInput);
        btnInput.setOnClickListener(new ButtonInputClickHandler());

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new ButtonResetClickHandler());

        btnCari = (Button) findViewById(R.id.btnCari);
        btnCari.setOnClickListener(new ButtonCariClickHandler());

        txtNoReg = (EditText) findViewById(R.id.txtNoReg);
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtAlamat = (EditText) findViewById(R.id.txtAlamat);
        txtNoHP = (EditText) findViewById(R.id.txtNoHP);
        txtJmlHuni = (EditText) findViewById(R.id.txtJmlHuni);
        txtPjgPipa = (EditText) findViewById(R.id.txtPjgPipa);
        if (txtPjgPipa != null) {
            txtPjgPipa.setEnabled(false);
        }
        txtLB = (EditText) findViewById(R.id.txtLB);
        txtGolTarif = (EditText) findViewById(R.id.txtTarif);
        txtUserId = (EditText) findViewById(R.id.txtUserId);
        txtTglPendaftaran = (EditText) findViewById(R.id.txttTglPendaftaran);

        spinPipa = (Spinner) findViewById(R.id.spinPipa);
        spinPipa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    txtPjgPipa.setEnabled(false);
                    txtPjgPipa.setText("0");
                } else {
                    txtPjgPipa.setEnabled(true);
                    txtPjgPipa.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {

            }

        });

        /*spinUkuranMeter = (Spinner) findViewById(R.id.spinUkuranMeter);*/

        spinDiaPipa = (Spinner) findViewById(R.id.spinDiaPipa);
        spinGolRumah = (Spinner) findViewById(R.id.spinGolRumah);

        spinStatusBangunan = (Spinner) findViewById(R.id.spinGolBangunan);
        spinStatusBangunan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("1")) {
                    txtGolTarif.setText("IA");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("2")) {
                    txtGolTarif.setText("IIA");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("3")) {
                    txtGolTarif.setText("IIB");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("4")) {
                    txtGolTarif.setText("IIIA");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("5")) {
                    txtGolTarif.setText("IIIB");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("6")) {
                    txtGolTarif.setText("IVA");
                } else if (spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim().equals("7")) {
                    txtGolTarif.setText("IVBA");
                }
                System.out.println("pilihan si jon = "+spinStatusBangunan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {

            }

        });


        spinStatus = (Spinner) findViewById(R.id.spinStatus);

        telButton = (Button) findViewById(R.id.btnTelepon);
        telButton.setOnClickListener(new ButtonTeleponClickHandler());

        surveyButton = (Button) findViewById(R.id.btnSurvey);
        surveyButton.setOnClickListener(new ButtonSurveyClickHandler());

        imgSurvey = (ImageView) findViewById(R.id.imageViewSurvey);

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
    }


    private void doInitData() {
        List<MBangunan> w = new ArrayList<MBangunan>();
        w.addAll(new MBangunan(getApplicationContext()).retrieveAll());
        spinStatusBangunan.setAdapter(FortunaUtils.getSimpleArrayAdapter(InputSurveyActivity.this,
                new MBangunan(getApplicationContext()).retrieveAll(), "KD_TARIF", "KETERANGAN"));

        txtNoReg.setFocusable(true);
    }


    private void doFromReview() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString("key");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA " + string);
            txtNoReg.setText(string);
            getData(string);
        }
    }


    /**
     * Button QR Code Handler
     */
    public class ButtonQRCodeClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCANNER_QRCODE);

            } catch (ActivityNotFoundException anfe) {
                showDialog(InputSurveyActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    /* Button Cari */
    public class ButtonCariClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getData(txtNoReg.getText().toString());
        }
    }

    /* Get Data From QR Code and Button Cari */
    private void getData(String sNoReg) {
        try {

            String userid = pref.getString("userid", "");
            TSurvey ds = new TSurvey(this);
            ds = ds.retrieveForSurvey(userid, "'" + sNoReg);

            if (ds == null) {
                Util.showmsg(this, "Peringatan :", getString(R.string.msg_no_data));
                txtNoReg.setEnabled(true);
                txtNoReg.setSelection(txtNoReg.getText().length());
            } else {
                Log.v("siapa", "hp : " + ds.getNO_HP());
                txtNama.setText(ds.getNAMA());
                txtAlamat.setText(ds.getALMT_PASANG());
                txtUserId.setText(ds.getUSER_ID());
                txtNoHP.setText(ds.getNO_HP().replace("'", ""));
                txtPjgPipa.setText("0");
                spinPipa.setSelection(FortunaUtils.getPosition(spinPipa.getAdapter(), "" + ds.getS_PIPA()));
                spinDiaPipa.setSelection(FortunaUtils.getPosition(spinDiaPipa.getAdapter(), "" + ds.getDIA_PIPA()));
                txtJmlHuni.setText(ds.getJ_PGHN_RMH() + " Orang");
                txtLB.setText((ds.getLB() == null ? "" : "" + ds.getLB()));
                spinStatusBangunan.setSelection(FortunaUtils.getPosition(spinStatusBangunan.getAdapter(), "" + ds.getGOL_BANGUNAN()));
                spinGolRumah.setSelection(FortunaUtils.getPosition(spinGolRumah.getAdapter(), "" + ds.getGOL_RUMAH()));
                txtTglPendaftaran.setText(ds.getTGL_DAFTAR().replace(".0", ""));
                spinStatus.setSelection(FortunaUtils.getPosition(spinStatus.getAdapter(), "" + ds.getS_SURVEY()));

                Util.popup(this, "Pelanggan : " + ds.getNAMA());
                txtNoReg.setEnabled(false);
            }
            ds.close();
        } catch (Exception e) {
            Log.e("getData", "Error: " + e.toString());
        }
    }

    /* Button Telepon */
    public class ButtonTeleponClickHandler implements View.OnClickListener {
        @SuppressWarnings("MissingPermission")
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + txtNoHP.getText()));
            startActivity(intent);
        }
    }


    /* Button Reset */
    public class ButtonResetClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            clearText();
            txtNoReg.setEnabled(true);
            txtNoReg.requestFocus();
            surveyScrollView.scrollTo(0, 0);
        }
    }


    /* Button Input */
    public class ButtonInputClickHandler implements View.OnClickListener {
        public void onClick(View view) {

            String StatusPipa = spinPipa.getSelectedItem().toString();
            String GolBangunan = spinStatusBangunan.getSelectedItem().toString();
            String Status = spinStatus.getSelectedItem().toString();

            String secondStatusPipa = StatusPipa.substring(4);
            String secondBangunan = GolBangunan.substring(4);
            String secondStatus = Status.substring(4);

            if (txtNama.getText().length() == 0) {
                Util.showmsg(InputSurveyActivity.this, "Peringatan :", "Data Calon Pelanggan Belum Ada");
            } else if (txtPjgPipa.getText().toString() == null || txtPjgPipa.getText().toString().trim().equals("")) {
                Util.showmsg(InputSurveyActivity.this, "Peringatan :", "Panjang Pipa Tidak Boleh Kosong");
            } else if (outputFileUri == null) {
                Util.showmsg(InputSurveyActivity.this, "Peringatan :", "Foto Rumah Tidak Boleh Kosong");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(InputSurveyActivity.this);
                builder.setTitle(getString(R.string.action_input))
                        .setMessage("Nama = " + txtNama.getText().toString() + "\n"
                                + "No HP = " + txtNoHP.getText().toString().replace("'", "") + "\n"
                                + "Jumlah Huni = " + txtJmlHuni.getText().toString() + "\n"
                                + "Status Pipa = " + secondStatusPipa + "\n"
                                /*+ "Ukuran Meter = " + spinUkuranMeter.getSelectedItem().toString() + "\n"*/
                                + "Panjang Pipa = " + txtPjgPipa.getText().toString() + "\n"
                                + "Dia Pipa = " + spinDiaPipa.getSelectedItem().toString() + "\n"
                                + "Luas Bangunan = " + txtLB.getText().toString() + "\n"
                                + "Gol Bangunan = " + secondBangunan + "\n"
                                + "Status = " + secondStatus + "\n"
                                + "Tgl Daftar = " + txtTglPendaftaran.getText().toString())
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveData();
                                //	sendData();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }

        }
    }


    /* Button Cancel */
    public class ButtonInputCancelClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            cancelDialog();
        }
    }

    private void cancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan : ");
        builder.setMessage("Batal Melakukan Input Data ? ").setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                InputSurveyActivity.this.finish();
            }
        });
        builder.setNegativeButton("Tidak", null).show();
    }


    /* Button  Foto */
    public class ButtonSurveyClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (txtNama.getText().toString().length() == 0)
                Util.showmsg(InputSurveyActivity.this, "Peringatan :", getString(R.string.msg_scan_pelanggan));
            else {
                Log.v("PHOTO", "Starting Camera app");
                startCameraActivity(view);
            }
        }
    }


    /* Start Camera */
    public void startCameraActivity(View v) {
        fileName = composeFileFoto(txtNoReg.getText().toString());
        sPathFoto = PHOTO_PATH + fileName;

        File file = new File(sPathFoto);
        output = Uri.fromFile(file);
        //	outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, PHOTO);
    }

    private String composeFileFoto(String sNoReg) {
        return "S_" + periodeSurvey + "_" + sNoReg.replace("'", "") + ".jpg";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ON ACTIVITY RESULT", "resultCode: " + resultCode + " On Request: " + requestCode);

        if (resultCode != RESULT_OK) {
            Util.showmsg(this, "Peringatan :", "Pengambilan Foto Gagal");
        } else {

            switch (requestCode) {
                case PHOTO:
                    outputFileUri = output;

                    onPhotoTaken();
                    viewFoto();
                    //	getGPSFoto();
                    break;
                case SCANNER_QRCODE:
                    String contents = data.getStringExtra("SCAN_RESULT");
                    //	String format = data.getStringExtra("SCAN_RESULT_FORMAT");

                    txtNoReg.setText(contents);
                    getData(contents);
                    break;
                default:
                    break;
            }
        }
    }


    private void onPhotoTaken() {

        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        options.inJustDecodeBounds = true;

        //	Bitmap bitmap = BitmapFactory.decodeFile(sPathFoto, options);
        Bitmap bitmap = ShrinkBitmap(sPathFoto, 1280, 720);
        imgSurvey.setImageBitmap(bitmap);

    }


    Bitmap ShrinkBitmap(String file, int width, int height) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int actualHeight = bmpFactoryOptions.outHeight;
        int actualWidth = bmpFactoryOptions.outWidth;
        float maxHeight = 720.0f;
        float maxWidth = 1280.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        bmpFactoryOptions.inSampleSize = calculateInSampleSize(bmpFactoryOptions, actualWidth, actualHeight);
        bmpFactoryOptions.inJustDecodeBounds = false;
        bmpFactoryOptions.inDither = false;
        bmpFactoryOptions.inPurgeable = true;
        bmpFactoryOptions.inInputShareable = true;
        bmpFactoryOptions.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) bmpFactoryOptions.outWidth;
        float ratioY = actualHeight / (float) bmpFactoryOptions.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateTime = sdf.format(Calendar.getInstance().getTime());
        String textPhoto = getString(R.string.cr_pdam_name);
        String idPel = txtNoReg.getText().toString();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        Canvas canvas = new Canvas(scaledBitmap);
        Paint tPaint = new Paint();
        tPaint.setTextSize(50);
        tPaint.setAntiAlias(true);
        tPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setTypeface(typeface);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        float heightNew = tPaint.measureText("yY");
        canvas.drawText(dateTime + " " + idPel, (middleX - bmp.getWidth() / 2) + 15, canvas.getHeight() - 10, tPaint);
        canvas.drawText(textPhoto, 0, 30, tPaint);

        ExifInterface exif;
        try {
            exif = new ExifInterface(file);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp;
    }


    public int calculateInSampleSize(BitmapFactory.Options bmpFactoryOptions, int reqWidth, int reqHeight) {
        final int height = bmpFactoryOptions.outHeight;
        final int width = bmpFactoryOptions.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            System.out.println("Simple Size " + inSampleSize);
        }
        final float totalPixels = width * height;
        System.out.println("Total Pixels " + totalPixels);
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        System.out.println("Total req pix " + totalReqPixelsCap);

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    /* View Foto */
    public void viewFoto() {
        imgSurvey.setImageURI(outputFileUri);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(InputSurveyActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(InputSurveyActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }


    /* GPS */
    public void getGPSFoto() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Toast.makeText(InputSurveyActivity.this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude,
                    Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
    }



    /* Clear All text */
    private void clearText() {
        txtNoReg.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtNoHP.setText("");
        spinPipa.setSelection(0);
        txtPjgPipa.setText("");
        spinDiaPipa.setSelection(0);
        /*spinUkuranMeter.setSelection(0);*/
        txtJmlHuni.setText("");
        ;
        txtLB.setText("");
        spinGolRumah.setSelection(0);
        spinStatusBangunan.setSelection(0);
        spinStatus.setSelection(0);
        txtTglPendaftaran.setText("");
        outputFileUri = null;
        imgSurvey.setImageResource(R.drawable.tidak_ada_foto_rumah);
    }


    /* Save Data to Database */
    private void saveData() {

        String golKodeBangunan = "";
        String GolBangunan = spinStatusBangunan.getSelectedItem().toString();
        String Bangunan = GolBangunan.substring(4).trim();
        System.out.println(Bangunan);

        String kodeTarif = spinStatusBangunan.getSelectedItem().toString().split(" = ")[0].trim();

        List<MBangunan> bangunanDetails = getDetilBangunan(InputSurveyActivity.this, Bangunan);

        String mesgErr = "";

        if (mesgErr.equals("")) {

            for (int i = 0; i < bangunanDetails.size(); i++) {
                MBangunan mbang = bangunanDetails.get(i);
                golKodeBangunan = String.valueOf(mbang.getKD_BANGUNAN());
            }

            TSurvey survey = new TSurvey(getApplicationContext());
            survey.ALMT_PASANG = txtAlamat.getText().toString();
            survey.DIA_PIPA = spinDiaPipa.getSelectedItem().toString();
            survey.FOTO = fileName;
            survey.FOTO_LAT = "" + latitude;
            survey.FOTO_LONG = "" + longitude;
            survey.GOL_BANGUNAN = golKodeBangunan;
            survey.KD_TARIF = kodeTarif.replace(" ", "");
            survey.GOL_RUMAH = spinGolRumah.getSelectedItem().toString().split(" = ")[0].trim();
            survey.J_PGHN_RMH = txtJmlHuni.getText().toString().replace(" Orang", "");
            survey.LB = txtLB.getText().toString().replace(" ", "");
            survey.NAMA = txtNama.getText().toString();
            survey.NO_HP = "'" + txtNoHP.getText().toString();
            survey.NO_REGISTER = "'" + txtNoReg.getText().toString();
            survey.S_PIPA = spinPipa.getSelectedItem().toString().split(" = ")[0].trim();
            survey.PJG_PIPA = txtPjgPipa.getText().toString();
            survey.S_SURVEY = spinStatus.getSelectedItem().toString().split(" = ")[0].trim();
            survey.USER_ID = pref.getString("userid", "");
            survey.S_KIRIM = "0";
            survey.TGL_SURVEY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            survey.TGL_DAFTAR = txtTglPendaftaran.getText().toString();
            /*survey.UKURAN_METER = spinUkuranMeter.getSelectedItem().toString();*/

            survey.update();
            AlertDialog.Builder builder = new AlertDialog.Builder(InputSurveyActivity.this);
            builder.setTitle(getString(R.string.action_input))
                    .setMessage("Input Data Sukses")
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            clearText();
                            txtNoReg.setEnabled(true);
                            txtNoReg.requestFocus();
                            surveyScrollView.scrollTo(0, 0);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();

        }
    }


    private static List<MBangunan> getDetilBangunan(Context Context, String ketBangunan) {

        List<MBangunan> bangunanDetails = new ArrayList<MBangunan>();
        MBangunan mbang = new MBangunan(Context);
        bangunanDetails = mbang.retrieveForBangunan(ketBangunan);

        return bangunanDetails;
    }


    /* Send Data RealTime */
    @SuppressWarnings("static-access")
    public void sendData() {

        String mesgErr = "";
        TSurvey survey = new TSurvey(getApplicationContext());

        MKonfigurasi mk = new MKonfigurasi(getApplicationContext());
        mk.kdKonfigurasi = "1";
        mk = mk.retrieveByID();
        if (mk != null && mk.realtimeURL != null) {

            ConnectivityWS ws = new ConnectivityWS();
            JSONObject postToServer = ws.sendRealtime(survey, mk.getRealtimeURL(), Integer.valueOf(pref.getString("kdWilayah", "")));
            if (postToServer != null) {
                try {
                    TSurvey ts = new TSurvey(getApplicationContext());
                    ts.NO_REGISTER = survey.NO_REGISTER;

                    if (postToServer.getString("CODE").equals("00")) {
                        ts.S_KIRIM = "1";
                        ts.M_KIRIM = postToServer.getString("MESSAGE");
                    } else {
                        ts.S_KIRIM = "0";
                        ts.M_KIRIM = postToServer.getString("MESSAGE");
                    }
                    survey.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("aaa", e.getMessage());
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog create = builder.create();
            create.setMessage(mesgErr);
            create.setTitle("Please check your entry");
            create.show();
        }
    }


    /* When Press Back Button */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (txtNama.getText().length() != 0) {
                dialogOnBackPress();
            } else {
                InputSurveyActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    protected void dialogOnBackPress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan : ");
        builder.setMessage("Apakah Anda Ingin Menyimpan Data Input ? ").setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                InputSurveyActivity.this.ButtonSaveClickHandler();
            }
        });

        builder.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                InputSurveyActivity.this.finish();
            }
        });

        builder.setNegativeButton("Kembali", null).show();
    }


    /* Button Save From Key Back Press */
    protected void ButtonSaveClickHandler() {

        String StatusPipa = spinPipa.getSelectedItem().toString();
        String GolBangunan = spinStatusBangunan.getSelectedItem().toString();
        String Status = spinStatus.getSelectedItem().toString();

        String secondStatusPipa = StatusPipa.substring(4);
        String secondBangunan = GolBangunan.substring(4);
        String secondStatus = Status.substring(4);

        if (txtPjgPipa.getText().toString() == null || txtPjgPipa.getText().toString().trim().equals("")) {
            Util.showmsg(InputSurveyActivity.this, "Peringatan :", "Panjang Pipa Tidak Boleh Kosong");
        } else if (outputFileUri == null) {
            Util.showmsg(InputSurveyActivity.this, "Peringatan :", "Foto Rumah Tidak Boleh Kosong");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(InputSurveyActivity.this);
            builder.setTitle(getString(R.string.action_input))
                    .setMessage("Nama = " + txtNama.getText().toString() + "\n"
                            + "No HP = " + txtNoHP.getText().toString().replace("'", "") + "\n"
                            + "Jumlah Huni = " + txtJmlHuni.getText().toString() + "\n"
                            + "Status Pipa = " + secondStatusPipa + "\n"
                            /*+ "Ukuran Meter = " + spinUkuranMeter.getSelectedItem().toString() + "\n"*/
                            + "Panjang Pipa = " + txtPjgPipa.getText().toString() + "\n"
                            + "Dia Pipa = " + spinDiaPipa.getSelectedItem().toString() + "\n"
                            + "Luas Bangunan = " + txtLB.getText().toString() + "\n"
                            + "Gol Bangunan = " + secondBangunan + "\n"
                            + "Status = " + secondStatus + "\n"
                            + "Tgl Daftar = " + txtTglPendaftaran.getText().toString())
                    .setCancelable(false)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveData();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        }
    }
}
