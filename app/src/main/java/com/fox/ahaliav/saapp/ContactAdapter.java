package com.fox.ahaliav.saapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.Typeface;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<Integer, Contact> _listDataChild;

    private ArrayList<Contact> dataSet;
    Context mContext;

    public ContactAdapter(Context context, List<String> listDataHeader,
                          HashMap<Integer, Contact> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_contact_group, null);
        }

        TextView tvListHeader = (TextView) convertView
                .findViewById(R.id.tvListHeader);
        tvListHeader.setTypeface(null, Typeface.BOLD);
        tvListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Contact contact = (Contact) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_contact, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        FrameLayout mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        tvName.setText(contact.getName());
        tvPhoneNumber.setText(contact.getPhoneNumber());
        tvComments.setText(contact.getComments());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
