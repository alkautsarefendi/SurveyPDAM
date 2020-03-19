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

@Table(name="mtarif")
public class MTarif extends BaseDAO {

    @PrimaryKey
    @Column
    public String kdTarif;

    @PrimaryKey
    @Column
    public String maxM3;

    @Column
    public String hargaM3;

    @Column
    public String golTarif;

    @Column
    public String biayaAdmin;

    @Column
    public String biayaDenda;


    public String getKdTarif() {
        return kdTarif;
    }


    public void setKdTarif(String kdTarif) {
        this.kdTarif = kdTarif;
    }


    public String getMaxM3() {
        return maxM3;
    }


    public void setMaxM3(String maxM3) {
        this.maxM3 = maxM3;
    }


    public String getHargaM3() {
        return hargaM3;
    }


    public void setHargaM3(String hargaM3) {
        this.hargaM3 = hargaM3;
    }


    public String getGolTarif() {
        return golTarif;
    }


    public void setGolTarif(String golTarif) {
        this.golTarif = golTarif;
    }


    public String getBiayaAdmin() {
        return biayaAdmin;
    }


    public void setBiayaAdmin(String biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }


    public String getBiayaDenda() {
        return biayaDenda;
    }


    public void setBiayaDenda(String biayaDenda) {
        this.biayaDenda = biayaDenda;
    }


    public MTarif(Context context) {
        super(context);
    }


    /* Generate Tagihan */
    public List<MTarif> retrieveForTagihan(String kodeTarif) {
        SQLiteDatabase readableDatabase = new ConnectivityDB(context).getReadableDatabase();
        List<MTarif> w = new ArrayList<MTarif>();
        String sql = "select "+ FortunaUtils.getCloumns(this)+" from mtarif where kdTarif='"+kodeTarif+"' order by maxM3 asc ";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{}
        );
        if(cursor.moveToFirst()){
            do{
                MTarif m = new MTarif(context);
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


    @Override
    public List<MTarif> retrieveAll() {
        List<MTarif> w = new ArrayList<MTarif>();
        ConnectivityDB db = new ConnectivityDB(context);

        try {
            Cursor cursor = db.getAllData(this);
            if(cursor.moveToFirst()){
                do{
                    MTarif m = new MTarif(context);
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

            return w;
        } finally {
            db.close();
        }
    }

    @Override
    public MTarif retrieveByID() {
        ConnectivityDB db = new ConnectivityDB(context);

        try {
            Cursor cursor = db.getDataByPrimaryKey(this);
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
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
            }else{
                return null;
            }
        } finally {
            db.close();
        }
    }
}
