package com.fox.ahaliav.saapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class CalenderFragment extends Fragment {

    GridView gvSubrieties = null;
    ArrayList<Subriety> list = null;
    ArrayList<Integer> listSelected = null;
    FloatingActionButton floatingActionButton = null;
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

        listSelected = new ArrayList<Integer>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calender, container, false);

        gvSubrieties = (GridView) v.findViewById(R.id.gvSubrieties);
        //listview.setItemsCanFocus(false);
        gvSubrieties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Subriety sub = (Subriety) parent.getAdapter().getItem(position);

                if (!listSelected.contains(sub.getId())) {
                    listSelected.add(sub.getId());
                    setHasOptionsMenu(true);
                    FrameLayout v = (FrameLayout) view.findViewById(R.id.mainframe);
                    v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_selected));

                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.mainlayout);
                    ll.setBackground(getResources().getDrawable(R.drawable.ic_check_opacity_black_15dp));
                } else {
                    FrameLayout v = (FrameLayout) view.findViewById(R.id.mainframe);
                    v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.mainlayout);
                    ll.setBackground(null);

                    listSelected.remove(listSelected.indexOf(sub.getId()));
                }

                return true;
            }
        });

        gvSubrieties.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Subriety sub = (Subriety) parent.getAdapter().getItem(position);

                if (listSelected.size() > 0 && listSelected.contains(sub.getId())) {
                    FrameLayout v = (FrameLayout) view.findViewById(R.id.mainframe);
                    v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.mainlayout);
                    ll.setBackground(null);

                    listSelected.remove(listSelected.indexOf(sub.getId()));
                } else if (listSelected.size() > 0 && !listSelected.contains(sub.getId())) {
                    for (Integer i = 0; i < parent.getChildCount(); i++) {
                        sub = (Subriety) parent.getAdapter().getItem(i);
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
                    bundle.putString("date", sub.getDate());

                    CalenderAddFragment calen = new CalenderAddFragment();
                    calen.setArguments(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, calen, "CalenderAddFragment");
                    ft.addToBackStack("CalenderAddFragment");
                    ft.commit();
                }
            }
        });

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new CalenderAddFragment(), "CalenderAddFragment");
                ft.addToBackStack("CalenderAddFragment");
                ft.commit();
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
                SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

                for (Integer i = 0; i < listSelected.size(); i++) {
                    db.deleteSubriety(listSelected.get(i));
                }

                listSelected.clear();

                loadsubrieties();

                return false;
            default:
                break;
        }

        return false;
    }

    private void loadsubrieties() {
        list = new ArrayList<Subriety>();

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectSubrieties("");

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                String date = result.getString(2);
                Subriety s = new Subriety(id, name, date);
                list.add(s);
            }

            if (!result.isClosed()) {
                result.close();
            }

            final CalenderAdapter adapter = new CalenderAdapter(list, getActivity().getApplicationContext());
            gvSubrieties.setAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listSelected.clear();
    }
}
