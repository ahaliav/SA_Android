package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class EmailsFragment extends Fragment {

    ListView listview = null;
    ArrayList<EmailContact> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;

    public EmailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_emails, container, false);

        listview = (ListView)v.findViewById(R.id.listviewEmails);

        spinner = (ProgressBar)v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        list = new ArrayList<EmailContact>();
        loademails();

        spinner.setVisibility(View.GONE);

        return v;
    }

    private void loademails() {
        list.add(new EmailContact("office@sa-israel.org",getResources().getString(R.string.mainoffice),"",""));
        list.add(new EmailContact("women@sa-israel.org",getResources().getString(R.string.womenhelp),"",""));
        list.add(new EmailContact("contact@sa-israel.org",getResources().getString(R.string.hotline),"",""));
        list.add(new EmailContact("office@sa-israel.org",getResources().getString(R.string.mainoffice),"",""));
        list.add(new EmailContact("news@sa-israel.org",getResources().getString(R.string.sanews),"",""));
        list.add(new EmailContact("website@sa-israel.org",getResources().getString(R.string.website),"",""));
        list.add(new EmailContact("12masorot@sa-israel.org",getResources().getString(R.string.traditions),"",""));
    }

}
