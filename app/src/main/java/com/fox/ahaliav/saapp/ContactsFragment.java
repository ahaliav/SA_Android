package com.fox.ahaliav.saapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;

import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class ContactsFragment extends Fragment {

    ListView listview = null;
    SearchView searchgroup = null;
    ArrayList<Contact> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;
    FloatingActionButton floatingActionButton = null;
    ArrayList<Integer> listSelected = null;

    ContactAdapter listAdapter;
    ExpandableListView expContacts;
    List<String> listDataHeader;
    HashMap<Integer, Contact> listDataChild;

    Menu menu;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        // get the listview
        expContacts = (ExpandableListView) v.findViewById(R.id.expContacts);

        listSelected = new ArrayList<Integer>();
        listview = (ListView)v.findViewById(R.id.listviewContacts);

        spinner = (ProgressBar)v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        searchgroup = (SearchView)v.findViewById(R.id.searchgroup);
        list = new ArrayList<Contact>();

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Contact sub = (Contact) parent.getAdapter().getItem(position);

                if (listSelected.size() > 0 && listSelected.contains(sub.getId())) {
                    FrameLayout v = (FrameLayout) view.findViewById(R.id.mainframe);
                    v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.mainlayout);
                    ll.setBackground(null);

                    listSelected.remove(listSelected.indexOf(sub.getId()));
                } else if (listSelected.size() > 0 && !listSelected.contains(sub.getId())) {
                    for (Integer i = 0; i < parent.getChildCount(); i++) {
                        sub = (Contact) parent.getAdapter().getItem(i);
                        View view_chiled = parent.getChildAt(i);
                        FrameLayout v = (FrameLayout) view_chiled.findViewById(R.id.mainframe);
                        v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                        LinearLayout ll = (LinearLayout) view_chiled.findViewById(R.id.mainlayout);
                        ll.setBackground(null);

                        if(listSelected.indexOf(sub.getId()) > -1){
                            listSelected.remove(listSelected.indexOf(sub.getId()));
                        }
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", sub.getId());
                    bundle.putString("name", sub.getName());
                    bundle.putString("phone", sub.getPhoneNumber());
                    bundle.putString("comments", sub.getComments());

                    ContactDetailsFragment calen = new ContactDetailsFragment();
                    calen.setArguments(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, calen, "ContactDetailsFragment");
                    ft.addToBackStack("ContactDetailsFragment");
                    ft.commit();
                }
            }
        });

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new ContactDetailsFragment(), "ContactDetailsFragment");
                ft.addToBackStack("ContactDetailsFragment");
                ft.commit();
            }
        });

        loadcontacts();

        spinner.setVisibility(View.GONE);
        return v;
    }

    private void loadcontacts() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<Integer, Contact>();

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectContacts("");

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                String phone = result.getString(2);
                String comments = result.getString(3);
                Contact s = new Contact(id, name, phone, comments);

                list.add(s);
                listDataChild.put(s.getId(), s);
                listDataHeader.add(result.getString(1));
            }

            if (!result.isClosed()) {
                result.close();
            }

            listAdapter = new ContactAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);
            expContacts.setAdapter(listAdapter);
        }
    }
}
