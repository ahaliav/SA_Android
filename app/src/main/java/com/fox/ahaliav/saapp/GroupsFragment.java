package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment implements SearchView.OnQueryTextListener, ICallbackMethod {

    ListView listview = null;
    SearchView searchgroup = null;
    ArrayList<Group> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;

    public GroupsFragment() {
        // Required empty public constructor
    }


    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_groups, container, false);

        listview = (ListView)v.findViewById(R.id.listviewGroups);

        spinner = (ProgressBar)v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        searchgroup = (SearchView)v.findViewById(R.id.searchgroup);
        list = new ArrayList<Group>();
        loadgroups();

        return v;
    }

    private void loadgroups() {
        WebSiteHelper helper = new WebSiteHelper(this);
        helper.getGroups();
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        for (int i = 0; i < objs.size(); ++i) {

            Group n = (Group)objs.get(i);
            list.add(n);
        }

        adapter = new GroupsAdapter(list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        listview.setTextFilterEnabled(true);
        filter = adapter.getFilter();
        searchgroup.setOnQueryTextListener(this);

        spinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        ArrayList<Group> filterList = new ArrayList<Group>();
        for (int i = 0; i < list.size(); i++) {
            Group dataModel = list.get(i);
            if ((list.get(i).getDay()).contains(s.toUpperCase())
                    || (list.get(i).getLocation()).contains(s.toUpperCase())
                    || (list.get(i).getLang()).contains(s.toUpperCase())
                    || (list.get(i).getFromTime()).contains(s.toUpperCase())) {
                filterList.add(dataModel);
            }
        }

        adapter = new GroupsAdapter(filterList, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Group> filterList = new ArrayList<Group>();
        for (int i = 0; i < list.size(); i++) {
            Group dataModel = list.get(i);
            if ((list.get(i).getDay()).contains(s.toUpperCase())
                    || (list.get(i).getLocation()).contains(s.toUpperCase())
                    || (list.get(i).getLang()).contains(s.toUpperCase())
                    || (list.get(i).getFromTime()).contains(s.toUpperCase())) {
                filterList.add(dataModel);
            }
        }

        adapter = new GroupsAdapter(filterList, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        return false;
    }
}
