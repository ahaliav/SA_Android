package com.fox.ahaliav.saapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CalenderFragment extends Fragment {

    ListView listview = null;
    ArrayList<Subriety> list = null;
    Menu menu;
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
        //listview.setItemsCanFocus(false);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                setHasOptionsMenu(true);
                //Toast.makeText(getActivity(), "Please long long press the key", Toast.LENGTH_LONG ).show();
                return true;
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Toast.makeText(getActivity(), "Please long press the key", Toast.LENGTH_LONG ).show();

            }
        });

        list = new ArrayList<Subriety>();
        loadsubrieties();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;

        MenuItem item = menu.findItem(R.id.action_delete);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                setHasOptionsMenu(false);
                return false;
            default:
                break;
        }

        return false;
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
