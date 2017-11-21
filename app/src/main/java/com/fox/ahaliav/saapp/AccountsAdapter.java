package com.fox.ahaliav.saapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccountsAdapter extends ArrayAdapter<String> {

    private List<String> objects;
    private Context context;

    public AccountsAdapter(Context context, int resourceId,
                              List<String> objects) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomHeader(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.item_account_spinner, parent, false);
        TextView tvListItem=(TextView)row.findViewById(R.id.tvListItem);
        tvListItem.setText(objects.get(position));

        if (position == 0) {//Special style for dropdown header
            tvListItem.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        return row;
    }


    public View getCustomHeader(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.item_account_header_spinner, parent, false);
        TextView tvListItem=(TextView)row.findViewById(R.id.tvListItem);
        tvListItem.setText(objects.get(position));

        if (position == 0) {//Special style for dropdown header
            //tvListItem.setTextColor(context.getResources().getColor(R.color.));
        }

        return row;
    }


}
