package com.project.fortuna.surveypdam.export;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.dao.TSurvey;

import java.util.List;

/**
 * Created by Akautsar Efendi on 1/10/2018.
 */

public class ExportFileSurveyAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<TSurvey> listSurvey;
    protected CheckBox cb;
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().toString() + "/Survey/Foto/";
    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/Survey/Icon/";

    public ExportFileSurveyAdapter(Activity activity, List<TSurvey> listSurvey) {
        this.activity = activity;
        this.listSurvey = listSurvey;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.v("Export", "Data ke " + position);

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_export_to_network_adapter, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(new ThumbnailExportClickHandler());
        cb = (CheckBox) convertView.findViewById(R.id.cb);

        // getting movie data for the row
        TSurvey m = listSurvey.get(position);
        title.setText(m.getNAMA());
        rating.setText("No Reg : " + m.getNO_REGISTER().replace("'", ""));
        genre.setText(m.getALMT_PASANG());

        if (m.FOTO == null || m.FOTO.trim().equals("")) {
            String defIcon = "tidak_ada_foto_rumah.png";

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(ICON_DEFAULT + defIcon, options);
            thumbnail.setImageBitmap(bitmap);
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_PATH + m.FOTO, options);
            thumbnail.setImageBitmap(bitmap);
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return listSurvey.size();
    }


    @Override
    public Object getItem(int position) {
        return listSurvey.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ThumbnailExportClickHandler implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

        }

    }
}
