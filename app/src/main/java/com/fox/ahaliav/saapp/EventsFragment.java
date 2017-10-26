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
import java.util.Date;
import java.util.List;
import java.util.Map;


public class EventsFragment extends Fragment implements ICallbackMethod {
    ListView listview = null;
    ArrayList<News> list = null;
    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();

        return fragment;
    }

    private void loadevents() {
        WebSiteHelper helper = new WebSiteHelper(this);
        helper.getEventsTitles();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        listview = (ListView)v.findViewById(R.id.listviewEvents);
        list = new ArrayList<News>();
        loadevents();

        return v;
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        for (int i = 0; i < objs.size(); ++i) {
            Map<String,Object> mapPost = (Map<String,Object>)objs.get(i);
            Map<String,Object> mapTitle = (Map<String, Object>) mapPost.get("title");
            News n = new News("", (String) mapTitle.get("rendered"), new Date());
            n.setTitle(n.getTitle().replaceAll("\\<[^>]*>","").replaceAll("\\&.*?\\;", ""));
            list.add(n);
        }

        final NewsAdapter adapter = new NewsAdapter(list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News dataModel= list.get(position);

                Snackbar.make(view, dataModel.getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
}
