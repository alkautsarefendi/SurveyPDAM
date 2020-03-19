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

@Table(name="muser")
public class MUser extends BaseDAO {

    public MUser(Context context) {
        super(context);
    }

    @Column
    @PrimaryKey
    public String userid;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String nmPegawai;

    @Column
    public String nmJabatan;

    @Column
    public String nip;

    @Column
    public String kdWilayah;

    @Column
    public String nmWilayah;

    public String imei;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String uername) {
        this.username = uername;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getNmPegawai() {
        return nmPegawai;
    }

    public void setNmPegawai(String nmPegawai) {
        this.nmPegawai = nmPegawai;
    }


    public String getNmJabatan() {
        return nmJabatan;
    }

    public void setNmJabatan(String nmJabatan) {
        this.nmJabatan = nmJabatan;
    }


    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }


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


    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }


    public MUser getUser(String username){
        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getReadableDatabase().query(
                FortunaUtils.getTableName(this), FortunaUtils.getCloumns(this).split(","),
                "username=?", new String[]{username}, null, null, null);
        if(cursor.getCount() > 0){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                }

            }
            return this;
        }

        return null;
    }

    @Override
    public List<?> retrieveAll() {

        return null;
    }

    @Override
    public MUser retrieveByID() {
        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
        if(cursor.getCount() > 0){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                }

            }
            return this;
        }
        db.close();
        return null;
    }



    /* Retrieve Review Data */
    public List<MUser> retrieveForUser(String user) {
        SQLiteDatabase readableDatabase = new ConnectivityDB(context).getReadableDatabase();
        List<MUser> w = new ArrayList<MUser>();
        String sql = "select "+FortunaUtils.getCloumns(this)+" from muser where userid=? ";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                MUser m = new MUser(context);
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



    /* Retrieve User */
    public MUser retrieveForUserHome(String user) {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from muser where userid=?", new String[]{user}
        );
        if(cursor.getCount() > 0 ){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    System.out.println("test "+string+"="+cursor.getString(cursor.getColumnIndex(string)));
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

            }
            return this;
        }
        readableDatabase.close();
        return null;
    }
}
