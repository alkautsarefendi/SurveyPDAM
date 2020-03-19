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

@Table(name="tsurvey")
public class TSurvey extends BaseDAO{

    public TSurvey(Context context) {
        super(context);
    }

    @Column
    @PrimaryKey
    public String NO_REGISTER;

    @Column
    public String NAMA;

    @Column
    public String ALMT_PASANG;

    @Column
    public String NO_HP;

    @Column
    public String PJG_PIPA;

    @Column
    public String DIA_PIPA;

    @Column
    public String J_PGHN_RMH;

    @Column
    public String S_PIPA;

    @Column
    public String LB;

//	@Column
//	public String WILAYAH;

    @Column
    public String KODE_JALAN;

    @Column
    public String GOL_RUMAH;

    @Column
    public String GOL_BANGUNAN;

    @Column
    public String KD_TARIF;

    @Column
    public String S_SURVEY;

    @Column
    public String FOTO;

    @Column
    public String FOTO_LAT;

    @Column
    public String FOTO_LONG;

    @Column
    public String TGL_SURVEY;

    @Column
    public String UKURAN_METER;

    @Column
    public String TGL_DAFTAR;

    @Column
    public String USER_ID;

    @Column
    public String S_KIRIM;

    @Column
    public String M_KIRIM;



    public String getNO_REGISTER() {
        return NO_REGISTER;
    }



    public void setNO_REGISTER(String nO_REGISTER) {
        NO_REGISTER = nO_REGISTER;
    }



    public String getNAMA() {
        return NAMA;
    }



    public void setNAMA(String nAMA) {
        NAMA = nAMA;
    }



    public String getALMT_PASANG() {
        return ALMT_PASANG;
    }



    public void setALMT_PASANG(String aLMT_PASANG) {
        ALMT_PASANG = aLMT_PASANG;
    }



    public String getNO_HP() {
        return NO_HP;
    }



    public void setNO_HP(String nO_HP) {
        NO_HP = nO_HP;
    }



    public String getPJG_PIPA() {
        return PJG_PIPA;
    }



    public void setPJG_PIPA(String pJG_PIPA) {
        PJG_PIPA = pJG_PIPA;
    }



    public String getDIA_PIPA() {
        return DIA_PIPA;
    }



    public void setDIA_PIPA(String dIA_PIPA) {
        DIA_PIPA = dIA_PIPA;
    }



    public String getLB() {
        return LB;
    }



    public void setLB(String lB) {
        LB = lB;
    }




    public String getJ_PGHN_RMH() {
        return J_PGHN_RMH;
    }



    public void setJ_PGHN_RMH(String j_PGHN_RMH) {
        J_PGHN_RMH = j_PGHN_RMH;
    }



//	public String getWILAYAH() {
//		return WILAYAH;
//	}



//	public void setWILAYAH(String wILAYAH) {
//		WILAYAH = wILAYAH;
//	}



    public String getS_PIPA() {
        return S_PIPA;
    }



    public void setS_PIPA(String s_PIPA) {
        S_PIPA = s_PIPA;
    }



    public String getKODE_JALAN() {
        return this.KODE_JALAN;
    }



    public void setKODE_JALAN(String kODE_JALAN) {
        KODE_JALAN = kODE_JALAN;
    }



    public String getGOL_RUMAH() {
        return GOL_RUMAH;
    }



    public void setGOL_RUMAH(String gOL_RUMAH) {
        GOL_RUMAH = gOL_RUMAH;
    }




    public String getGOL_BANGUNAN() {
        return GOL_BANGUNAN;
    }



    public void setGOL_BANGUNAN(String gOL_BANGUNAN) {
        GOL_BANGUNAN = gOL_BANGUNAN;
    }




    public String getKD_TARIF() {
        return KD_TARIF;
    }



    public void setKD_TARIF(String kD_TARIF) {
        KD_TARIF = kD_TARIF;
    }



    public String getS_SURVEY() {
        return S_SURVEY;
    }



    public void setS_SURVEY(String s_SURVEY) {
        S_SURVEY = s_SURVEY;
    }



    public String getFOTO() {
        return FOTO;
    }



    public void setFOTO(String fOTO) {
        FOTO = fOTO;
    }



    public String getFOTO_LAT() {
        return FOTO_LAT;
    }



    public void setFOTO_LAT(String fOTO_LAT) {
        FOTO_LAT = fOTO_LAT;
    }



    public String getFOTO_LONG() {
        return FOTO_LONG;
    }



    public void setFOTO_LONG(String fOTO_LONG) {
        FOTO_LONG = fOTO_LONG;
    }



    public String getTGL_SURVEY() {
        return TGL_SURVEY;
    }



    public void setTGL_SURVEY(String tGL_SURVEY) {
        TGL_SURVEY = tGL_SURVEY;
    }



    public String getUKURAN_METER() {
        return UKURAN_METER;
    }



    public void setUKURAN_METER(String uKURAN_METER) {
        UKURAN_METER = uKURAN_METER;
    }



    public String getTGL_DAFTAR() {
        return TGL_DAFTAR;
    }



    public void setTGL_DAFTAR(String tGL_DAFTAR) {
        TGL_DAFTAR = tGL_DAFTAR;
    }



    public String getUSER_ID() {
        return USER_ID;
    }



    public void setUSER_ID(String uSER_ID) {
        USER_ID = uSER_ID;
    }




    /* Retrieve All */
    @Override
    public List<TSurvey> retrieveAll() {
        List<TSurvey> w = new ArrayList<TSurvey>();

        ConnectivityDB db = new ConnectivityDB(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                TSurvey m = new TSurvey(context);
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



    /* Retrieve by ID */
    @Override
    public TSurvey retrieveByID() {
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
        db.close();
        return null;
    }



    /* Retrieve Survey */
    public TSurvey retrieveForSurvey(String user, String noreg) {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from tsurvey where USER_ID=? and NO_REGISTER=?",
                new String[]{user, noreg}
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
        db.close();
        return null;
    }



    /* Retrieve Review Data */
    public List<TSurvey> retrieveForReview(String user, String sTglSurvey, boolean isBelum, boolean isSukses) {
        List<TSurvey> w = new ArrayList<TSurvey>();

        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select "+ FortunaUtils.getCloumns(this)+" from tsurvey where USER_ID=? ";
        if(isBelum) sql=sql+" and ( S_SURVEY < 0 ) ORDER BY NO_REGISTER ASC";
        else if(isSukses) sql=sql+" and ( TGL_SURVEY LIKE '"+sTglSurvey+"%' ) and ( S_SURVEY between 0 and 4 ) ORDER BY NO_REGISTER ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                TSurvey m = new TSurvey(context);
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



    /* Export to Server */
    public List<TSurvey> retrieveForExportServer(String user) {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        List<TSurvey> w = new ArrayList<TSurvey>();
        String sql = "select "+FortunaUtils.getCloumns(this)+" from tsurvey where USER_ID=? and ( S_SURVEY between 0 and 4 ) and S_KIRIM = 0 ORDER BY NO_REGISTER ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                TSurvey m = new TSurvey(context);
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



    /* Retrieve Export to Local */
    public List<TSurvey> retrieveForExport(String user, String sTglSurvey) {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        List<TSurvey> w = new ArrayList<TSurvey>();
        String sql = "select "+FortunaUtils.getCloumns(this)+" from tsurvey where USER_ID=? and ( TGL_SURVEY LIKE '"+sTglSurvey+"%' ) and ( S_SURVEY between 0 and 4 ) ORDER BY NO_REGISTER ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                TSurvey m = new TSurvey(context);
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



    /* Hapus Data Belum di Baca */
    public List<TSurvey> doClearDatabaseSurveyBelumdiBaca() {
        ConnectivityDB db = new ConnectivityDB(context);
        SQLiteDatabase readableDatabase = db.getWritableDatabase();
        List<TSurvey> w = new ArrayList<TSurvey>();
        String sql = "delete from tsurvey where S_SURVEY < 0 ";

		/*Cursor cursor = */readableDatabase.execSQL(sql);/*(sql,
					new String[]{}
				);*/
		/*if(cursor.moveToFirst()){
			do{
				TSurvey m = new TSurvey(context);
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
		}*/
        db.close();
        return w;
    }
}
