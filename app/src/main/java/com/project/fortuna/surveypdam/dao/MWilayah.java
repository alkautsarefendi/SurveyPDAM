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

@Table(name="mwilayah")
public class MWilayah extends BaseDAO{

    public MWilayah(Context context) {
        super(context);
    }

    @PrimaryKey
    @Column
    public String kdWilayah;

    @Column
    public String nmWilayah;

    public String getKdWilayah() {
        return kdWilayah;
    }

    public void setKdWilayah(String kdWilayah) {
        this.kdWilayah = kdWilayah;
    }

    public String getNmWilayah() {
        return nmWilayah;
    }

    public void setNmWilayah(String nmWilayah) {
        this.nmWilayah = nmWilayah;
    }



    @Override
    public List<MWilayah> retrieveAll() {
        List<MWilayah> w = new ArrayList<MWilayah>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                MWilayah m = new MWilayah(context);
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
    public MWilayah retrieveByID() {
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
