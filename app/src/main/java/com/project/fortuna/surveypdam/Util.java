package com.project.fortuna.surveypdam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

public class Util {

    public static int nShowMsg = 0;
    public static void showmsg(Context context, String sTitle, String sMsg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(sTitle)
                .setMessage(sMsg)
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        nShowMsg = 10;
                    }
                });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    public static void showmsg2(Context context, String sTitle, String sMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(sTitle)
                .setMessage(sMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        nShowMsg = 10;
                    }
                })

                .setNegativeButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        nShowMsg = 11;
                    }
                });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    public static void popup(Context context, String sMsg) {
        Toast.makeText(context, sMsg, Toast.LENGTH_LONG).show();
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer);
        return buffer.toByteArray();
    }

    public static boolean writeFile(String sFileName, String content){
        try{
            File file = new File(sFileName);
            //Blob blob = connection.createBlob();
            file.setReadOnly();

            FileWriter filewriter = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(filewriter);
            out.append(content);
            out.close();

            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
