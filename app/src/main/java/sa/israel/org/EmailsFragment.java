package sa.israel.org;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.ProgressBar;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmailsFragment extends Fragment {

    ArrayList<EmailContact> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;
    List<String> listDataHeader;
    ExpandableListView expContacts;
    HashMap<Integer, EmailContact> listDataChild;
    EmailContactAdapter listAdapter;

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

        expContacts = (ExpandableListView) v.findViewById(R.id.expContacts);

        spinner = (ProgressBar)v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        list = new ArrayList<EmailContact>();
        loademails(inflater);

        spinner.setVisibility(View.GONE);

        return v;
    }

    private void loademails(LayoutInflater inflater) {
        list.add(new EmailContact("office@sa-israel.org",getResources().getString(R.string.mainoffice),"",""));
        list.add(new EmailContact("women@sa-israel.org",getResources().getString(R.string.womenhelp),"",""));
        list.add(new EmailContact("contact@sa-israel.org",getResources().getString(R.string.hotline),"",""));
        list.add(new EmailContact("news@sa-israel.org",getResources().getString(R.string.sanews),"",""));
        list.add(new EmailContact("pic@sa-israel.org",getResources().getString(R.string.public_information_committee),"",""));
        list.add(new EmailContact("website@sa-israel.org",getResources().getString(R.string.website),"",""));
        list.add(new EmailContact("12masorot@sa-israel.org",getResources().getString(R.string.traditions),"",""));
        list.add(new EmailContact("litr@sa-israel.org",getResources().getString(R.string.literature_committee),"",""));
        list.add(new EmailContact("cfc@sa-israel.org",getResources().getString(R.string.prison_committee),"",""));
        list.add(new EmailContact("transl@sa-israel.org",getResources().getString(R.string.translation_committee),"",""));
        list.add(new EmailContact("aad@sa-israel.org",getResources().getString(R.string.aad),"",""));
        list.add(new EmailContact("gad@sa-israel.org",getResources().getString(R.string.gad),"",""));
        list.add(new EmailContact("ig-jrslm@sa-israel.org",getResources().getString(R.string.ig_jerusaem),"",""));
        list.add(new EmailContact("ig-center@sa-israel.org",getResources().getString(R.string.ig_center),"",""));
        list.add(new EmailContact("ig-bsh@sa-israel.org",getResources().getString(R.string.ig_baitshemesh),"",""));
        list.add(new EmailContact("ig-north@sa-israel.org",getResources().getString(R.string.ig_northh),"",""));
        list.add(new EmailContact("ig-eng@sa-israel.org",getResources().getString(R.string.ig_eng),"",""));
        list.add(new EmailContact("ig-south@sa-israel.org",getResources().getString(R.string.ig_south),"",""));
        list.add(new EmailContact("trsr@sa-israel.org",getResources().getString(R.string.trsr),"",""));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<Integer, EmailContact>();

        for(int i=0; i<list.size(); i++){

            listDataChild.put(i, list.get(i));
            listDataHeader.add(list.get(i).getTitle());
        }

        listAdapter = new EmailContactAdapter(getActivity(), getContext(), listDataHeader, listDataChild,getFragmentManager());
        expContacts.setAdapter(listAdapter);
    }
}
