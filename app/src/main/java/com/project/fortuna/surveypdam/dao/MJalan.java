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

@Table(name="mjalan")
public class MJalan extends BaseDAO {

    public MJalan(Context context) {
        super(context);
    }


    @PrimaryKey
    @Column
    public String kdWilayah;

    @Column
    public String kdJalan;

    @Column
    public String nmJalan;


    public String getKdWilayah() {
        return kdWilayah;
    }

    public void setKdWilayah(String kdWilayah) {
        this.kdWilayah = kdWilayah;
    }

    public String getKdJalan() {
        return kdJalan;
    }

    public void setKdJalan(String kdJalan) {
        this.kdJalan = kdJalan;
    }

    public String getNmJalan() {
        return nmJalan;
    }

    public void setNmJalan(String nmJalan) {
        this.nmJalan = nmJalan;
    }



    @Override
    public List<MJalan> retrieveAll() {
        List<MJalan> j = new ArrayList<MJalan>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                MJalan mjal = new MJalan(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mjal, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                j.add(mjal);
            }while(cursor.moveToNext());
        }
        db.close();
        return j;
    }

    public List<MJalan> retrieveByWilayah(String kdWilayah) {
        List<MJalan> j = new ArrayList<MJalan>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getReadDB().rawQuery("SELECT * FROM mjalan where kdWilayah=?", new String[]{kdWilayah});
        if(cursor.moveToFirst()){
            do{
                MJalan mjal = new MJalan(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mjal, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                j.add(mjal);
            }while(cursor.moveToNext());
        }
        db.close();
        return j;
    }

    @Override
    public MJalan retrieveByID() {
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
}
