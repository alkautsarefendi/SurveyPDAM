package com.project.fortuna.surveypdam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.project.fortuna.surveypdam.activity.InputSurveyActivity;
import com.project.fortuna.surveypdam.dao.BaseDAO;
import com.project.fortuna.surveypdam.dao.TSurvey;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

public class ConnectivityWS {
    public static JSONObject postSurvey(BaseDAO domain, String url, int kdWilayah){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        TSurvey survey = (TSurvey) domain;
        nameValuePairs.add(new BasicNameValuePair("USERID", survey.getUSER_ID()));
        nameValuePairs.add(new BasicNameValuePair("KD_DAFTAR", survey.getNO_REGISTER().replace("'", "")));
        nameValuePairs.add(new BasicNameValuePair("NAMA", survey.getNAMA()));
        nameValuePairs.add(new BasicNameValuePair("KD_JALAN", "0"));
        nameValuePairs.add(new BasicNameValuePair("ALAMAT", survey.getALMT_PASANG()));
        nameValuePairs.add(new BasicNameValuePair("LUAS_BANGUNAN", survey.getLB()));
        nameValuePairs.add(new BasicNameValuePair("PHOTO_RUMAH", survey.getFOTO()));
        nameValuePairs.add(new BasicNameValuePair("LATITUDE", survey.getFOTO_LAT()));
        nameValuePairs.add(new BasicNameValuePair("LONGITUDE", survey.getFOTO_LONG()));
        nameValuePairs.add(new BasicNameValuePair("S_PIPA", survey.getS_PIPA()));
        nameValuePairs.add(new BasicNameValuePair("PANJANG_PIPA", survey.getPJG_PIPA()));
        nameValuePairs.add(new BasicNameValuePair("DIAMETER_PIPA", survey.getDIA_PIPA()+""));
        nameValuePairs.add(new BasicNameValuePair("JUMLAH_PENGHUNI", survey.getJ_PGHN_RMH()+""));
        nameValuePairs.add(new BasicNameValuePair("TGL_DAFTAR", survey.getTGL_DAFTAR()));
        nameValuePairs.add(new BasicNameValuePair("TANGGAL_SURVEY", survey.getTGL_SURVEY()));
        nameValuePairs.add(new BasicNameValuePair("STATUS_SURVEY", survey.getS_SURVEY()+""));
        nameValuePairs.add(new BasicNameValuePair("KD_WILAYAH", kdWilayah+""));
        nameValuePairs.add(new BasicNameValuePair("KD_TARIF", survey.getKD_TARIF()+""));
        nameValuePairs.add(new BasicNameValuePair("GOL_BANGUNAN", survey.getGOL_BANGUNAN()+""));
        nameValuePairs.add(new BasicNameValuePair("GOL_RUMAH", survey.getGOL_RUMAH()+""));
        nameValuePairs.add(new BasicNameValuePair("UKURAN_METER", survey.getUKURAN_METER()+""));

        String pathName = InputSurveyActivity.PHOTO_PATH+survey.getFOTO();

        System.out.println(pathName);

		/*
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BitmapFactory.Options options = null;
		options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

		//	bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		byte[] ba = stream.toByteArray();
		String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);


		nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", ba1.replace('+','-').replace('/','_')));
		*/

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);

        nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", encodedString));

        // Upload image to server
        // new uploadToServer().execute();

		/*
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

		String encodedString = Base64.encodeToString(Util.convertBitmapToByteArray(bitmap),Base64.DEFAULT);
		String safeString = encodedString.replace('+','-').replace('/','_');

		nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", safeString));
		*/

		/*Generate Params from Domain*/
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try{
                if(field.get(domain) != null){
                    nameValuePairs.add(
                            new BasicNameValuePair(field.getName().toUpperCase(),
                                    String.valueOf(field.get(domain))
                            )
                    );
                }
            }catch(Exception e){
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }

        if(url.contains("?")){
            String dataParams = url.split("\\?")[1];
            String[] params = dataParams.split("&");
            for (String param : params) {
                if (!param.equals("USERID")){
                    String[] data = param.split("=");
                    nameValuePairs.add(new BasicNameValuePair(data[0], data[1]));
                }
            }
        }
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpContext localContext = new BasicHttpContext();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost, localContext);

            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
            try {
                return new JSONObject("{CODE:99,MESSAGE:"+e.getMessage()+"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }




    public static JSONObject sendRealtime(BaseDAO domain, String url, int kdWilayah){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        TSurvey survey = (TSurvey) domain;
        nameValuePairs.add(new BasicNameValuePair("USERID", survey.getUSER_ID()));
        nameValuePairs.add(new BasicNameValuePair("KD_DAFTAR", survey.getNO_REGISTER().replace("'", "")));
        nameValuePairs.add(new BasicNameValuePair("NAMA", survey.getNAMA()));
        nameValuePairs.add(new BasicNameValuePair("ALAMAT", survey.getALMT_PASANG()));
        nameValuePairs.add(new BasicNameValuePair("LUAS_BANGUNAN", survey.getLB()));
        nameValuePairs.add(new BasicNameValuePair("PHOTO_RUMAH", survey.getFOTO()));
        nameValuePairs.add(new BasicNameValuePair("LATITUDE", survey.getFOTO_LAT()));
        nameValuePairs.add(new BasicNameValuePair("LONGITUDE", survey.getFOTO_LONG()));
        nameValuePairs.add(new BasicNameValuePair("PANJANG_PIPA", survey.getPJG_PIPA()));
        nameValuePairs.add(new BasicNameValuePair("DIAMETER_PIPA", survey.getDIA_PIPA()+""));
        nameValuePairs.add(new BasicNameValuePair("JUMLAH_PENGHUNI", survey.getJ_PGHN_RMH()+""));
        nameValuePairs.add(new BasicNameValuePair("TANGGAL_SURVEY", survey.getTGL_SURVEY()));
        nameValuePairs.add(new BasicNameValuePair("STATUS_SURVEY", survey.getS_SURVEY()+""));
        nameValuePairs.add(new BasicNameValuePair("KD_WILAYAH", kdWilayah+""));
        nameValuePairs.add(new BasicNameValuePair("KD_JALAN", survey.getKODE_JALAN()+""));
        nameValuePairs.add(new BasicNameValuePair("GOL_BANGUNAN", survey.getGOL_BANGUNAN()+""));
        nameValuePairs.add(new BasicNameValuePair("GOL_RUMAH", survey.getGOL_RUMAH()+""));
        nameValuePairs.add(new BasicNameValuePair("UKURAN_METER", survey.getUKURAN_METER()+""));

        String pathName = InputSurveyActivity.PHOTO_PATH+survey.getFOTO();

        System.out.println(pathName);

		/*
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BitmapFactory.Options options = null;
		options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

		//	bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		byte[] ba = stream.toByteArray();
		String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);


		nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", ba1.replace('+','-').replace('/','_')));
		*/

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);

        nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", encodedString));

        // Upload image to server
        // new uploadToServer().execute();

		/*
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

		String encodedString = Base64.encodeToString(Util.convertBitmapToByteArray(bitmap),Base64.DEFAULT);
		String safeString = encodedString.replace('+','-').replace('/','_');

		nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", safeString));
		*/

		/*Generate Params from Domain*/
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try{
                if(field.get(domain) != null){
                    nameValuePairs.add(
                            new BasicNameValuePair(field.getName().toUpperCase(),
                                    String.valueOf(field.get(domain))
                            )
                    );
                }
            }catch(Exception e){
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }

        if(url.contains("?")){
            String dataParams = url.split("\\?")[1];
            String[] params = dataParams.split("&");
            for (String param : params) {
                if (!param.equals("USERID")){
                    String[] data = param.split("=");
                    nameValuePairs.add(new BasicNameValuePair(data[0], data[1]));
                }
            }
        }
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpContext localContext = new BasicHttpContext();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost, localContext);

            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
            try {
                return new JSONObject("{CODE:99,MESSAGE:"+e.getMessage()+"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }




    public static JSONObject checkServerAvailability(String url){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //HttpGet httpRequest = new HttpGet(new URL(url).toURI());
            //HttpClient httpClient = new DefaultHttpClient();
            //HttpResponse response = (HttpResponse) httpClient.execute(httpRequest);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static JSONObject postToServer(BaseDAO domain, String url){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		/*Generate Params from Domain*/
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try{
                if(field.get(domain) != null){
                    nameValuePairs.add(
                            new BasicNameValuePair(field.getName().toUpperCase(),
                                    String.valueOf(field.get(domain))
                            )
                    );
                }
            }catch(Exception e){
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }

        if(url.contains("?")){
            String dataParams = url.split("\\?")[1];
            String[] params = dataParams.split("&");
            for (String param : params) {
                String[] data = param.split("=");
                nameValuePairs.add(new BasicNameValuePair(data[0], data[1]));
            }
        }
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpContext localContext = new BasicHttpContext();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost, localContext);

            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
            try {
                return new JSONObject("{CODE:99,MESSAGE:"+e.getMessage()+"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getFromServer(String url, BaseDAO domain){
		/*Generate Params from Domain*/
        String params="";
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try{
                if(field.get(domain) != null){
                    params=params+field.getName()+"="+field.get(domain)+"&";
                }
            }catch(Exception e){
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }
        params = params.substring(0, params.length()-1);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI(url);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return new JSONObject(result.toString());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
