package com.fox.ahaliav.saapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahaliav_fox on 30 אוקטובר 2017.
 */

public class GroupsAdapter extends ArrayAdapter<Group> {
    ArrayList<Group> dataSet;
    Context mContext;
    Group dataModel;
    private static class ViewHolder {
        TextView tvDay;
        TextView tvFromTime;
        TextView tvToTime;
        TextView tvComment;
        TextView tvLang;
        TextView tvLocation;
        TextView tvKm;
        ImageButton btnWaze;
        FrameLayout mainframe;
    }

    public GroupsAdapter(ArrayList<Group> data, Context context) {
        super(context, R.layout.item_group, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        GroupsAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        //if (convertView == null) {

        viewHolder = new GroupsAdapter.ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_group, parent, false);
        viewHolder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
        viewHolder.tvFromTime = (TextView) convertView.findViewById(R.id.tvFromTime);
        viewHolder.tvToTime = (TextView) convertView.findViewById(R.id.tvToTime);
        viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        viewHolder.tvLang = (TextView) convertView.findViewById(R.id.tvLang);
        viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        viewHolder.tvKm = (TextView) convertView.findViewById(R.id.tvKm);
        viewHolder.btnWaze = (ImageButton) convertView.findViewById(R.id.btnWaze);

        viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        convertView.setTag(viewHolder);
        //} else {
        //   viewHolder = (GroupsAdapter.ViewHolder) convertView.getTag();
        //}

        viewHolder.tvDay.setText(dataModel.getDay());
        viewHolder.tvFromTime.setText(dataModel.getFromTime());
        viewHolder.tvToTime.setText(dataModel.getToTime());
        viewHolder.tvComment.setText(dataModel.getComment());
        viewHolder.tvLang.setText(dataModel.getLang());
        viewHolder.tvLocation.setText(dataModel.getLocation());
        viewHolder.tvKm.setText(String.valueOf(dataModel.getKm()));

        viewHolder.btnWaze.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uri = "https://waze.com/ul?ll=" + dataModel.getLatitude() + "," + dataModel.getLongitude() + "&navigate=yes";
                mContext.startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri)));
            }
        });

        return convertView;
    }
}

