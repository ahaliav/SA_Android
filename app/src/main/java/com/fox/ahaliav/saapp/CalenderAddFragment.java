package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


public class CalenderAddFragment extends Fragment {

    private DatePicker dpResult;
    Button btnSave;
    Button btnCancel;
    EditText txtName;
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

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveCurrentDateOnView();
            }
        });


        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

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


    public void saveCurrentDateOnView() {

        dpResult = (DatePicker) rootView.findViewById(R.id.dpResult);

        EditText txtName = (EditText)rootView.findViewById(R.id.txtName);

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

        String name = txtName.getText().toString();
        db.insertSubrieties(name, getDateFromDatePicker(dpResult));
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


}
