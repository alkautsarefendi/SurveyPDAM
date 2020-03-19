package com.project.fortuna.surveypdam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.project.fortuna.surveypdam.annotation.Column;
import com.project.fortuna.surveypdam.annotation.Table;
import com.project.fortuna.surveypdam.dao.BaseDAO;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

public class FortunaUtils {

    public static String getTableName(BaseDAO domain){
        Table table = domain.getClass().getAnnotation(Table.class);
        if(table != null){
            return table.name();
        }else{
            throw new RuntimeException("Annotation Table Not Found on Domain "+domain.getClass().getName());
        }
    }

    public static  String getCloumns(BaseDAO domain){
        String column = "";
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if(annotations.length > 0){
                column = column + field.getName();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Column) {
                        Column cc = (Column) annotation;
                        String name = cc.name();
                        if(name.equals("")) name = field.getName();
                        column = column +" "+name;
                    }
                }
                column = column+",";
            }
        }
        return column.substring(0, column.length() - 1);
    }

    public static String formatField(String columnName){
        String[] splitColumnNames = columnName.split("_");
        String fieldName = "";
        for (int i = 0; i < splitColumnNames.length; i++) {
            if(i == 0) fieldName = fieldName+splitColumnNames[i].toLowerCase();
            else fieldName = fieldName+splitColumnNames[i].substring(0,1).toUpperCase()+splitColumnNames[i].substring(1).toLowerCase();
        }
        return fieldName;
    }


    public static ArrayAdapter<?> getSimpleArrayAdapter(Context con, List<?> list, String idName, String lblName){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(con, android.R.layout.simple_spinner_item, getLabel(list, idName, lblName));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    public static List<String> getLabel(List<?> list, String idName, String lblName){
        List<String> listLabel = new ArrayList<String>();

        for (Object item : list) {
            try{
                String id = String.valueOf(item.getClass().getDeclaredField(idName).get(item));
                String label = String.valueOf(item.getClass().getDeclaredField(lblName).get(item));

                listLabel.add(id+" =  "+label);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return listLabel;
    }

    @SuppressWarnings("unchecked")
    public static int getPosition(Adapter adapters, String label){
        if(label != null && !label.trim().equals("")){
            ArrayAdapter<String> adapter = (ArrayAdapter<String> ) adapters;
            for (int i = 0; i < adapter.getCount(); i++) {
                String[] item = adapter.getItem(i).split("=");
                if(item[0].trim().equals(label)){
                    return i;
                }
            }
        }
        return -1;
    }


    @SuppressLint("MissingPermission")
    public static String getDeviceID(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }
}
