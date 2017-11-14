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
 * Created by ahaliav_fox on 14 נובמבר 2017.
 */

public class EmailContactAdapter  extends ArrayAdapter<EmailContact>  {

    private ArrayList<EmailContact> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDays;

        FrameLayout mainframe;
    }

    public EmailContactAdapter(ArrayList<EmailContact> data, Context context) {
        super(context, R.layout.item_emailcontact, data);
        this.dataSet = data;
        this.mContext = context;
    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EmailContact dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EmailContactAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new EmailContactAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_emailcontact, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDays = (TextView) convertView.findViewById(R.id.tvDays);
            viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmailContactAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvTitle.setText(dataModel.getTitle());
        viewHolder.tvDays.setText(dataModel.getContent());

        // viewHolder.mainframe.setOnClickListener(this);
        viewHolder.mainframe.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }

}
