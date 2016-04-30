package com.example.lucas.running_cat1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yanzhensong on 4/25/16.
 */
public class InfoAdapter extends ArrayAdapter<Info> {
    private int resourceId;

    public InfoAdapter(Context context, int textViewResourceId, List<Info> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Info info = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView infoImage = (ImageView) view.findViewById(R.id.info_image);
        TextView infoBrief = (TextView) view.findViewById(R.id.info_brief);
        infoImage.setImageResource(info.getImageId());
        infoBrief.setText(info.getBrief());
        return view;
    }
}
