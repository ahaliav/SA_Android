package com.fox.ahaliav.saapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class CalenderFragment extends Fragment {

    ListView listview = null;
    ArrayList<Subriety> list = null;
    public CalenderFragment() {
        // Required empty public constructor
    }

    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calender, container, false);

        listview = (ListView)v.findViewById(R.id.listviewSubrieties);
        list = new ArrayList<Subriety>();
        loadsubrieties();

        return v;
    }

    private void loadsubrieties() {

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectSubrieties("");

        if(result != null) {

            while (result.moveToNext()) {

                int id = result.getInt(0);
                String name = result.getString(1);
                String date = result.getString(2);

                Subriety s = new Subriety(id,name, date);
                list.add(s);
            }

            if (!result.isClosed())  {
                result.close();
            }

            final CalenderAdapter adapter = new CalenderAdapter(list, getActivity().getApplicationContext());
            listview.setAdapter(adapter);
        }
    }
}
