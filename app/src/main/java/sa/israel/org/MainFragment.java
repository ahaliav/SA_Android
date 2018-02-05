package sa.israel.org;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import sa.israel.org.R;

import java.util.ArrayList;

public class MainFragment extends Fragment  {

    View rootView = null;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        addListenerOnButton();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.favicon_sa);
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
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new GroupsFragment(), "GroupsFragment");
                        ft.addToBackStack("GroupsFragment");
                        ft.commit();
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
                        loadsubrieties();
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
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new NewsFragment(), "NewsFragment");
                        ft.addToBackStack("NewsFragment");
                        ft.commit();
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
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new EventsFragment(), "EventsFragment");
                        ft.addToBackStack("EventsFragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearEvents.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }


                return true;
            }
        });

        LinearLayout linearContacts = (LinearLayout) rootView.findViewById(R.id.linearContacts);

        linearContacts.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearContacts = (LinearLayout) rootView.findViewById(R.id.linearContacts);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        loadcontacts();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearContacts.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }

                return true;
            }
        });

        LinearLayout linearEmail = (LinearLayout) rootView.findViewById(R.id.linearEmail);

        linearEmail.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearEmail = (LinearLayout) rootView.findViewById(R.id.linearEmail);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearEmail.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new EmailsFragment(), "EmailsFragment");
                        ft.addToBackStack("EmailsFragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearEmail.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }


                return true;
            }
        });
    }

    private void loadsubrieties() {

        ArrayList<Subriety> list = new ArrayList<Subriety>();
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

            if(list.size() == 0){
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new CalenderAddFragment(), "CalenderAddFragment");
                ft.addToBackStack("CalenderAddFragment");
                ft.commit();
            }
            else {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new CalenderFragment(), "CalenderFragment");
                ft.addToBackStack("CalenderFragment");
                ft.commit();
            }

        }
    }

    private void loadcontacts() {
        ArrayList<Contact> list = new ArrayList<Contact>();

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectContacts("", "");

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                String phone = result.getString(2);
                Contact s = new Contact(id, name, phone, "","");
                list.add(s);
            }


            if (!result.isClosed()) {
                result.close();
            }
        }

        if(list.size() == 0){
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_container, new ContactDetailsFragment(), "ContactDetailsFragment");
            ft.addToBackStack("ContactDetailsFragment");
            ft.commit();
        }
        else {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_container, new ContactsFragment(), "ContactsFragment");
            ft.addToBackStack("ContactsFragment");
            ft.commit();
        }
    }
}
