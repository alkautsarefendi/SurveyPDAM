package com.project.fortuna.surveypdam.dao;

import android.content.Context;
import android.database.Cursor;

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

@Table(name="tinfostatus")
public class TInfoStatus extends BaseDAO {
    public TInfoStatus(Context context) {
        super(context);
    }

    @Column
    @PrimaryKey
    public String PERIODE;

    @Column
    public String TGL_IMPORT;

    @Column
    public String PETUGAS;

    @Column
    public int NO_RUTE;

    @Column
    public int TARGET;

    @Column
    public int BACA;

    @Column
    public int GAGAL;

    @Column
    public int BELUM;

    @Column
    public String TGL_EXPORT;




    public String getPERIODE() {
        return PERIODE;
    }



    public void setPERIODE(String pERIODE) {
        PERIODE = pERIODE;
    }



    public String getTGL_IMPORT() {
        return TGL_IMPORT;
    }



    public void setTGL_IMPORT(String tGL_IMPORT) {
        TGL_IMPORT = tGL_IMPORT;
    }



    public String getPETUGAS() {
        return PETUGAS;
    }



    public void setPETUGAS(String pETUGAS) {
        PETUGAS = pETUGAS;
    }



    public int getNO_RUTE() {
        return NO_RUTE;
    }



    public void setNO_RUTE(int nO_RUTE) {
        NO_RUTE = nO_RUTE;
    }



    public int getTARGET() {
        return TARGET;
    }



    public void setTARGET(int tARGET) {
        TARGET = tARGET;
    }



    public int getBACA() {
        return BACA;
    }



    public void setBACA(int bACA) {
        BACA = bACA;
    }



    public int getGAGAL() {
        return GAGAL;
    }



    public void setGAGAL(int gAGAL) {
        GAGAL = gAGAL;
    }



    public int getBELUM() {
        return BELUM;
    }



    public void setBELUM(int bELUM) {
        BELUM = bELUM;
    }



    public String getTGL_EXPORT() {
        return TGL_EXPORT;
    }



    public void setTGL_EXPORT(String tGL_EXPORT) {
        TGL_EXPORT = tGL_EXPORT;
    }



    @Override
    public List<TInfoStatus> retrieveAll() {
        List<TInfoStatus> i = new ArrayList<TInfoStatus>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                TInfoStatus mi = new TInfoStatus(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mi, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                i.add(mi);
            }while(cursor.moveToNext());
        }

        return i;
    }



    @Override
    public TInfoStatus retrieveByID() {
        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
        if(cursor.getCount() > 0 ){
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
    }
}
