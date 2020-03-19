package com.project.fortuna.surveypdam.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.fortuna.surveypdam.ConnectivityDB;
import com.project.fortuna.surveypdam.annotation.Column;
import com.project.fortuna.surveypdam.annotation.PrimaryKey;
import com.project.fortuna.surveypdam.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

@Table(name="mkonfigurasiws")
public class MKonfigurasi extends BaseDAO {

    public MKonfigurasi(Context context) {
        super(context);
    }


    @Column
    @PrimaryKey
    public String kdKonfigurasi;

    @Column
    public String userFTP;

    @Column
    public String passwordFTP;

    @Column
    public String urlFTP;

    @PrimaryKey
    public String userURL;

    @Column
    public String wilayahURL;

    @Column
    public String jalanURL;

    @Column
    public String downloadURL;

    @Column
    public String uploadURL;

    @Column
    public String realtimeURL;

    @Column
    public String bangunanURL;

    @Column
    public String pingURL;


    public String getUserFTP() {
        return userFTP;
    }

    public void setUserFTP(String userFTP) {
        this.userFTP = userFTP;
    }


    public String getPasswordFTP() {
        return passwordFTP;
    }

    public void setPasswordFTP(String passwordFTP) {
        this.passwordFTP = passwordFTP;
    }


    public String getUrlFTP() {
        return urlFTP;
    }

    public void setUrlFTP(String urlFTP) {
        this.urlFTP = urlFTP;
    }


    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }


    public String getWilayahURL() {
        return wilayahURL;
    }

    public void setWilayahURL(String wilayahURL) {
        this.wilayahURL = wilayahURL;
    }


    public String getJalanURL() {
        return jalanURL;
    }

    public void setJalanURL(String jalanURL) {
        this.jalanURL = jalanURL;
    }


    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }


    public String getUploadURL() {
        return uploadURL;
    }

    public void setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
    }


    public String getRealtimeURL() {
        return realtimeURL;
    }

    public void setRelatimeURL(String realtimeURL) {
        this.realtimeURL = realtimeURL;
    }

    public void setBangunanURL(String bangunanURL) {
        this.bangunanURL = bangunanURL;
    }

    public String getBangunanURL() {
        return bangunanURL;
    }

    public String getPingURL() {
        return pingURL;
    }

    public void setPingURL(String pingURL) {
        this.pingURL = pingURL;
    }

    @Override
    public List<MKonfigurasi> retrieveAll() {
        List<MKonfigurasi> k = new ArrayList<MKonfigurasi>();
        ConnectivityDB db = new ConnectivityDB(context);

        try {
            Cursor cursor = db.getAllData(this);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    MKonfigurasi mk = new MKonfigurasi(context);
                    String[] columnNames = cursor.getColumnNames();
                    for (String string : columnNames) {
                        try {
                            Field declaredField = this.getClass().getDeclaredField(string);
                            declaredField.set(mk, cursor.getString(cursor.getColumnIndex(string)));
                        } catch (NoSuchFieldException e) {
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        }
                    }
                    k.add(mk);
                } while (cursor.moveToNext());
            }
            return k;
        } finally {
            db.close();
        }
    }

    @Override
    public MKonfigurasi retrieveByID() {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readabledatabase = db.getReadableDatabase();
        String sql = "SELECT downloadURL FROM mkonfigurasiws WHERE kdKonfigurasi=1";
        try {
            Cursor cursor = readabledatabase.rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){
                if(cursor != null) cursor.moveToFirst();
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }

                }
                return this;
            }
            return null;
        } finally {
            db.close();
        }

    }

    public MKonfigurasi retrieveUploadByID() {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readabledatabase = db.getReadableDatabase();
        String sql = "SELECT uploadURL FROM mkonfigurasiws  WHERE kdKonfigurasi=1";
        try {
            Cursor cursor = readabledatabase.rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){
                if(cursor != null) cursor.moveToFirst();
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }

                }
                return this;
            }
            return null;
        } finally {
            db.close();
        }

    }
}
