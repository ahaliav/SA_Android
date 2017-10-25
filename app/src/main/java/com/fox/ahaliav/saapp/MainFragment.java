package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView = null;
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        addListenerOnButton();

        return rootView;
    }

    public void addListenerOnButton() {
        LinearLayout linearGroups = (LinearLayout) rootView.findViewById(R.id.linearGroups);

        linearGroups.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearGroups = (LinearLayout) rootView.findViewById(R.id.linearGroups);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearGroups.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearGroups.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearCalender = (LinearLayout) rootView.findViewById(R.id.linearCalender);

        linearCalender.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearCalender = (LinearLayout) rootView.findViewById(R.id.linearCalender);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearCalender.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearCalender.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearNews = (LinearLayout) rootView.findViewById(R.id.linearNews);

        linearNews.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearNews = (LinearLayout) rootView.findViewById(R.id.linearNews);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearNews.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearNews.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearEvents = (LinearLayout) rootView.findViewById(R.id.linearEvents);

        linearEvents.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearEvents = (LinearLayout) rootView.findViewById(R.id.linearEvents);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearEvents.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearEvents.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

    }
}
