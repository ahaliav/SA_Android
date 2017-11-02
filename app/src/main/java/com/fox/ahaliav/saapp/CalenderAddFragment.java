package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;


public class CalenderAddFragment extends Fragment {

    private DatePicker dpResult;
    View rootView = null;
    int year;
    int month;
    int day;

    public CalenderAddFragment() {
        // Required empty public constructor
    }

    public static CalenderAddFragment newInstance(String param1, String param2) {
        CalenderAddFragment fragment = new CalenderAddFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calender_add, container, false);

        setCurrentDateOnView();
        return rootView;
    }


    public void setCurrentDateOnView() {

        dpResult = (DatePicker) rootView.findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into datepicker
        dpResult.init(year, month, day, null);
    }



}
