package sa.israel.org;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Traditions7Fragment extends Fragment {
    View rootView = null;
    Menu menu;
    public Traditions7Fragment() {
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
        rootView = inflater.inflate(R.layout.fragment_traditions7, container, false);

        addListenerOnButton();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.tradition7);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void addListenerOnButton() {
        LinearLayout linearStandingOrderI = (LinearLayout) rootView.findViewById(R.id.linearStandingOrderI);

        linearStandingOrderI.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearStandingOrderI = (LinearLayout) rootView.findViewById(R.id.linearStandingOrderI);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearStandingOrderI.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        Bundle bundle = new Bundle();
                        bundle.putString("url", "https://app.icount.co.il/m/976be");
                        Tradition7Fragment frag = new Tradition7Fragment();
                        frag.setArguments(bundle);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, frag, "Tradition7Fragment");
                        ft.addToBackStack("Tradition7Fragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearStandingOrderI.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearStandingOrderR = (LinearLayout) rootView.findViewById(R.id.linearStandingOrderR);

        linearStandingOrderR.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearStandingOrderR = (LinearLayout) rootView.findViewById(R.id.linearStandingOrderR);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearStandingOrderR.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        Bundle bundle = new Bundle();
                        bundle.putString("url", "https://app.icount.co.il/m/3b61a");
                        Tradition7Fragment frag = new Tradition7Fragment();
                        frag.setArguments(bundle);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, frag, "Tradition7Fragment");
                        ft.addToBackStack("Tradition7Fragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearStandingOrderR.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearOneTimeI = (LinearLayout) rootView.findViewById(R.id.linearOneTimeI);

        linearOneTimeI.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearOneTimeI = (LinearLayout) rootView.findViewById(R.id.linearOneTimeI);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearOneTimeI.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        Bundle bundle = new Bundle();
                        bundle.putString("url", "https://app.icount.co.il/m/d44bc");
                        Tradition7Fragment frag = new Tradition7Fragment();
                        frag.setArguments(bundle);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, frag, "Tradition7Fragment");
                        ft.addToBackStack("Tradition7Fragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearOneTimeI.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
                        break;
                }
                return true;
            }
        });

        LinearLayout linearOneTimeR = (LinearLayout) rootView.findViewById(R.id.linearOneTimeR);

        linearOneTimeR.setOnTouchListener( new View.OnTouchListener()
        {
            LinearLayout linearOneTimeR = (LinearLayout) rootView.findViewById(R.id.linearOneTimeR);
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        linearOneTimeR.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border_click, null));
                        Bundle bundle = new Bundle();
                        bundle.putString("url", "https://app.icount.co.il/m/a23fd");
                        Tradition7Fragment frag = new Tradition7Fragment();
                        frag.setArguments(bundle);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, frag, "Tradition7Fragment");
                        ft.addToBackStack("Tradition7Fragment");
                        ft.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        //set color back to default
                        linearOneTimeR.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.linear_layout_border, null));
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
        Cursor result = db.selectContacts(-1, "", "");

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                String phone = result.getString(2);
                Contact s = new Contact(id, name, phone, "","",0);
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
