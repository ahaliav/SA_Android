package com.fox.ahaliav.saapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<Integer, Contact> _listDataChild;
    FragmentManager manager;


    private ArrayList<Contact> dataSet;

    public ContactAdapter(Context context, List<String> listDataHeader,
                          HashMap<Integer, Contact> listChildData, FragmentManager manager) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.manager = manager;

    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
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
        return this._listDataChild.get(groupPosition);
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
        //tvListHeader.setTypeface(null, Typeface.BOLD);
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
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        FrameLayout mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        tvName.setText(contact.getName());
        tvPhoneNumber.setText(contact.getPhoneNumber());
        tvComments.setText(contact.getComments());
        tvEmail.setText(contact.getEmail());

        ImageButton btnEmail=(ImageButton)convertView.findViewById(R.id.btnEmail);
        ImageButton btnCall=(ImageButton)convertView.findViewById(R.id.btnCall);
        ImageButton btnEdit=(ImageButton)convertView.findViewById(R.id.btnEdit);
        ImageButton btnSms=(ImageButton)convertView.findViewById(R.id.btnSms);
        ImageButton btnShare=(ImageButton)convertView.findViewById(R.id.btnShare);

        btnSms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.findFragmentByTag("ContactsFragment").startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + contact.getPhoneNumber())));

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String sharevia = _context.getResources().getString(R.string.sharevia);
                String sharecontact = _context.getResources().getString(R.string.sharecontact);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = contact.getName() + "\n" + contact.getPhoneNumber() + "\n" + contact.getEmail();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sharecontact);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                _context.startActivity(Intent.createChooser(sharingIntent, sharevia));

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity activity = manager.findFragmentByTag("ContactsFragment").getActivity();
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CALL_PHONE},
                            123);
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhoneNumber()));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    manager.findFragmentByTag("ContactsFragment").startActivity(callIntent);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = manager.beginTransaction();
                ContactDetailsFragment contactdetails = new ContactDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("id", contact.getId());
                bundle.putString("name", contact.getName());
                bundle.putString("phone", contact.getPhoneNumber());
                bundle.putString("comments", contact.getComments());
                bundle.putString("email", contact.getEmail());

                contactdetails.setArguments(bundle);

                ft.replace(R.id.main_fragment_container, contactdetails, "ContactDetailsFragment");
                ft.addToBackStack("ContactDetailsFragment");
                ft.commit();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + contact.getEmail());
                testIntent.setData(data);
                manager.findFragmentByTag("ContactsFragment").startActivity(testIntent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
