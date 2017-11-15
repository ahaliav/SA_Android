package com.fox.ahaliav.saapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {
    private ArrayList<Contact> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvName;
        TextView tvPhoneNumber;
        TextView tvComments;

        FrameLayout mainframe;
    }

    public ContactAdapter(ArrayList<Contact> data, Context context) {
        super(context, R.layout.item_contact, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_contact, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            viewHolder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);
            viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvName.setText(dataModel.getName());
        viewHolder.tvComments.setText(dataModel.getComments());
        viewHolder.tvPhoneNumber.setText(dataModel.getPhoneNumber());

        // viewHolder.mainframe.setOnClickListener(this);
        viewHolder.mainframe.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}
