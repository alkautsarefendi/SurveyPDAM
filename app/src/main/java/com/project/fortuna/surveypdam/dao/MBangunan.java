package com.project.fortuna.surveypdam.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.fortuna.surveypdam.ConnectivityDB;
import com.project.fortuna.surveypdam.FortunaUtils;
import com.project.fortuna.surveypdam.annotation.Column;
import com.project.fortuna.surveypdam.annotation.PrimaryKey;
import com.project.fortuna.surveypdam.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

@Table(name="mbangunan")
public class MBangunan extends BaseDAO {

    public MBangunan(Context context) {
        super(context);
    }

    @PrimaryKey
    @Column
    public String KD_BANGUNAN;

    @Column
    public String KD_TARIF;

    @Column
    public String KETERANGAN;


    public String getKD_BANGUNAN() {
        return KD_BANGUNAN;
    }

    public void setKD_BANGUNAN(String kD_BANGUNAN) {
        KD_BANGUNAN = kD_BANGUNAN;
    }

    public String getKD_TARIF() {
        return KD_TARIF;
    }

    public void setKD_TARIF(String kD_TARIF) {
        KD_TARIF = kD_TARIF;
    }

    public String getKETERANGAN() {
        return KETERANGAN;
    }

    public void setKETERANGAN(String kETERANGAN) {
        KETERANGAN = kETERANGAN;
    }


    @Override
    public List<MBangunan> retrieveAll() {
        List<MBangunan> w = new ArrayList<MBangunan>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                MBangunan m = new MBangunan(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(m, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                w.add(m);
            }while(cursor.moveToNext());
        }
        db.close();
        return w;
    }



    @Override
    public MBangunan retrieveByID() {
        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
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
        db.close();
        return this;
    }



    /* Generate Bangunan */
    public List<MBangunan> retrieveForBangunan(String ketBangunan) {
        SQLiteDatabase readableDatabase = new ConnectivityDB(context).getReadableDatabase();
        List<MBangunan> w = new ArrayList<MBangunan>();
        String sql = "select "+ FortunaUtils.getCloumns(this)+" from mbangunan where KETERANGAN='"+ketBangunan+"'";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{}
        );
        if(cursor.moveToFirst()){
            do{
                MBangunan m = new MBangunan(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(m, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                w.add(m);
            }while(cursor.moveToNext());
        }
        readableDatabase.close();
        return w;
    }
}
