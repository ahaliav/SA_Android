package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment implements ICallbackMethod {

    ListView listview = null;
    ArrayList<Group> list = null;

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

        final GroupsAdapter adapter = new GroupsAdapter(list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Group dataModel= list.get(position);

                Snackbar.make(view, dataModel.getDay(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
}
