package com.project.fortuna.surveypdam.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.fortuna.surveypdam.R;
import com.project.fortuna.surveypdam.dao.MUser;

import java.util.List;

/**
 * Created by Akautsar Efendi on 1/9/2018.
 */

public class ProfileSurveyAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<MUser> listUser;

    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/Survey/Icon/";

    public ProfileSurveyAdapter(Activity activity, List<MUser> listUser) {
        this.activity = activity;
        this.listUser = listUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_profile_adapter, parent, false);

        TextView namaPetugas = (TextView) convertView.findViewById(R.id.txtNamaPetugas);
        TextView username = (TextView) convertView.findViewById(R.id.txtUsername);
        TextView jabatan = (TextView) convertView.findViewById(R.id.txtJabatan);
        TextView nip = (TextView) convertView.findViewById(R.id.txtNIP);
        TextView kodewilayah = (TextView) convertView.findViewById(R.id.txtKodeWilayah);
        TextView namawilayah = (TextView) convertView.findViewById(R.id.txtNamaWilayah);

        MUser mu = listUser.get(position);

        namaPetugas.setText(mu.getNmPegawai());
        username.setText(mu.getUsername());
        jabatan.setText(mu.getNmJabatan());
        nip.setText(mu.getNip());
        kodewilayah.setText(mu.getKdWilayah());
        namawilayah.setText(mu.getNmWilayah());

        return convertView;
    }


    @Override
    public int getCount() {
        return listUser.size();
    }


    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ThumbnailClickHandler implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

        }

    }
}
