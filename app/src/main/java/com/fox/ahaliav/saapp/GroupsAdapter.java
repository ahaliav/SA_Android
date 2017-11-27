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
    double cur_latitude = 0;
    double cur_longitude = 0;

    //FragmentManager manager;
    private static class ViewHolder {
        TextView tvDay;
        TextView tvFromTime;
        TextView tvToTime;
        TextView tvComment;
        TextView tvLang;
        TextView tvLocation;
        TextView tvKm;
        ImageButton btnWaze;
        ImageButton btnGooglemaps;
        ImageButton btnEdit;
        ImageButton btnDelete;
        FrameLayout mainframe;
    }

    public GroupsAdapter(ArrayList<Group> data, Context context, double cur_latitude, double cur_longitude) {
        super(context, R.layout.item_group, data);
        this.dataSet = data;
        this.mContext = context;
        this.cur_latitude = cur_latitude;
        this.cur_longitude = cur_longitude;
        //this.manager = manager;
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
        viewHolder.btnGooglemaps = (ImageButton) convertView.findViewById(R.id.btnGooglemaps);
        viewHolder.btnEdit = (ImageButton) convertView.findViewById(R.id.btnEdit);
        viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
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

        if (mContext.getResources().getString(R.string.local) == "he")
            viewHolder.tvKm.setText(" קמ" + String.valueOf(dataModel.getKm()));
        else
            viewHolder.tvKm.setText(String.valueOf(dataModel.getKm()) + " " + mContext.getResources().getString(R.string.KM));

        viewHolder.btnWaze.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?saddr="
                        + cur_latitude + ","
                        + cur_longitude + "&daddr="
                        + dataModel.getLatitude() + "," + dataModel.getLongitude();
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(navigation);
            }
        });

        viewHolder.btnGooglemaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://maps.google.com/maps?saddr="
                                + cur_latitude + ","
                                + cur_longitude + "&daddr="
                                + dataModel.getLatitude() + "," + dataModel.getLongitude()));
                navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(navigation);
            }
        });


        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String message = mContext.getResources().getString(R.string.city) + ": " + dataModel.getLocation() + "\n";
                message += mContext.getResources().getString(R.string.day) + ": " + dataModel.getDay() + "\n";
                message += mContext.getResources().getString(R.string.fromtime) + ": " + dataModel.getFromTime() + "\n";
                message += mContext.getResources().getString(R.string.totime) + ": " + dataModel.getToTime() + "\n";
                message += mContext.getResources().getString(R.string.mynameis) + ": \n";
                message += mContext.getResources().getString(R.string.iwoulsliketoupdate) + ": \n";
                message += mContext.getResources().getString(R.string.myphoneis) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri data = Uri.parse("mailto:?subject=" + mContext.getResources().getString(R.string.hisaiwouldliketoupdateagroup) + "&body=" + message + "&to=office@sa-israel.org;website@sa-israel.org");
                intent.setData(data);

                mContext.startActivity(intent);

            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String message = mContext.getResources().getString(R.string.city) + ": " + dataModel.getLocation() + "\n";
                message += mContext.getResources().getString(R.string.day) + ": " + dataModel.getDay() + "\n";
                message += mContext.getResources().getString(R.string.fromtime) + ": " + dataModel.getFromTime() + "\n";
                message += mContext.getResources().getString(R.string.totime) + ": " + dataModel.getToTime() + "\n";
                message += mContext.getResources().getString(R.string.mynameis) + ": \n";
                message += mContext.getResources().getString(R.string.iwoulsliketoupdate) + ": \n";
                message += mContext.getResources().getString(R.string.myphoneis) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + mContext.getResources().getString(R.string.hisaiwouldliketoupdatethisremoved) + "&body=" + message + "&to=office@sa-israel.org;website@sa-israel.org");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(data);

                mContext.startActivity(intent);

            }
        });


        return convertView;
    }
}

