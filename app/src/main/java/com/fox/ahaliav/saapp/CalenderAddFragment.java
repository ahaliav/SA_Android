package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalenderAddFragment extends Fragment {

    private DatePicker dpResult;
    Button btnSave;
    Button btnCancel;
    EditText txtName;
    View rootView = null;
    int year;
    int month;
    int day;
    Integer id = -1;
    String name = "";
    String date = "";
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

        rootView = inflater.inflate(R.layout.fragment_calender_add, container, false);

        txtName = (EditText)rootView.findViewById(R.id.txtName);

        if(getArguments() != null && getArguments().containsKey("id")){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.calender_edit_title);
            id = getArguments().getInt("id");
            name = getArguments().getString("name");
            date = getArguments().getString("date");
            txtName.setText(name);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date startdate = null;
            try {

                startdate = df.parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(startdate);
                setCurrentDateOnView(c);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.calender__add_title);
            setCurrentDateOnView(Calendar.getInstance());
        }



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


    public void setCurrentDateOnView(Calendar c) {

        dpResult = (DatePicker) rootView.findViewById(R.id.dpResult);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into datepicker
        dpResult.init(year, month, day, null);
    }


    public void saveCurrentDateOnView() {

        dpResult = (DatePicker) rootView.findViewById(R.id.dpResult);

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

        name = txtName.getText().toString();

        if(id <=0){
            db.insertSubrieties(name, getDateFromDatePicker(dpResult));
        }
        else {
            db.updateSubriety(id, name, getDateFromDatePicker(dpResult));
        }

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
