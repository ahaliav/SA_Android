package sa.israel.org;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView searchgroup = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar progressBar;
    FloatingActionButton floatingActionButton = null;
    ArrayList<Integer> listSelected = null;
    FloatingActionButton floatingActionButtonImport;
    ContactAdapter listAdapter;
    ExpandableListView expContacts;
    ArrayList<Contact> listDataHeader;
    HashMap<Integer, Contact> listDataChild;

    Menu menu;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        // get the listview
        expContacts = (ExpandableListView) v.findViewById(R.id.expContacts);

        listSelected = new ArrayList<Integer>();

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        floatingActionButtonImport = (FloatingActionButton) v.findViewById(R.id.floatingActionButtonImport);
        searchgroup = (SearchView) v.findViewById(R.id.searchgroup);
        searchgroup.setOnQueryTextListener(this);

        expContacts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Contact sub = (Contact) parent.getAdapter().getItem(position);

                if (listSelected.size() > 0 && listSelected.contains(sub.getId())) {
                    FrameLayout v = (FrameLayout) view.findViewById(R.id.mainframe);
                    v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.mainlayout);
                    ll.setBackground(null);

                    listSelected.remove(listSelected.indexOf(sub.getId()));
                } else if (listSelected.size() > 0 && !listSelected.contains(sub.getId())) {
                    for (Integer i = 0; i < parent.getChildCount(); i++) {
                        sub = (Contact) parent.getAdapter().getItem(i);
                        View view_chiled = parent.getChildAt(i);
                        FrameLayout v = (FrameLayout) view_chiled.findViewById(R.id.mainframe);
                        v.setBackground(getResources().getDrawable(R.drawable.subriety_item_style_not_selected));

                        LinearLayout ll = (LinearLayout) view_chiled.findViewById(R.id.mainlayout);
                        ll.setBackground(null);

                        if (listSelected.indexOf(sub.getId()) > -1) {
                            listSelected.remove(listSelected.indexOf(sub.getId()));
                        }
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", sub.getId());
                    bundle.putString("name", sub.getName());
                    bundle.putString("phone", sub.getPhoneNumber());
                    bundle.putString("comments", sub.getComments());
                    bundle.putString("email", sub.getEmail());

                    ContactDetailsFragment calen = new ContactDetailsFragment();
                    calen.setArguments(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, calen, "ContactDetailsFragment");
                    ft.addToBackStack("ContactDetailsFragment");
                    ft.commit();
                }
            }
        });

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new ContactDetailsFragment(), "ContactDetailsFragment");
                ft.addToBackStack("ContactDetailsFragment");
                ft.commit();
            }
        });

        floatingActionButtonImport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new ImportContactsFragment(), "ImportContactsFragment");
                ft.addToBackStack("ImportContactsFragment");
                ft.commit();
            }
        });

        startHeavyProcessing();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.contacts));
        return v;
    }

    public void loadcontacts(String search) {
        listDataHeader = new ArrayList<Contact>();
        listDataChild = new HashMap<Integer, Contact>();
        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectContacts(-1, "", search);

        int i = 0;
        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                String phone = result.getString(2);
                String email = result.getString(3);
                String comments = result.getString(4);
                int orderby = result.getInt(5);
                Contact s = new Contact(id, name, phone, comments, email, orderby);

                listDataChild.put(i++, s);
                listDataHeader.add(s);
            }

            if (!result.isClosed()) {
                result.close();
            }
        }

        listAdapter = new ContactAdapter(getActivity(),this, getActivity().getApplicationContext(), listDataHeader, listDataChild, getFragmentManager(), MainActivity.WhatsupNumbers);
        expContacts.setAdapter(listAdapter);

    }

    public void setResultContacts(HashMap<String, String> list){
        loadcontacts("");
        progressBar.setVisibility(View.GONE);
    }

    private HashMap<String, String> getWhatsupContacts() {
        HashMap<String, String> myWhatsappNumbers = new HashMap<String, String>();
        try{
            ContentResolver cr = getContext().getContentResolver();

            Cursor contactCursor = cr.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts._ID,
                            ContactsContract.RawContacts.CONTACT_ID},
                    ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                    new String[]{"com.whatsapp"},
                    null);



            if (contactCursor != null) {
                if (contactCursor.getCount() > 0) {
                    if (contactCursor.moveToFirst()) {
                        do {
                            //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                            String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                            if (whatsappContactId != null) {
                                //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                                Cursor whatsAppContactCursor = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{whatsappContactId}, null);

                                if (whatsAppContactCursor != null) {
                                    whatsAppContactCursor.moveToFirst();
                                    String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                    String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    whatsAppContactCursor.close();

                                    String phoneToStore = number.replace(" ", "").replace("-", "");
                                    //Add Number to ArrayList
                                    if(!myWhatsappNumbers.containsValue(phoneToStore))
                                        myWhatsappNumbers.put(phoneToStore, id);
                                }
                            }
                        } while (contactCursor.moveToNext());
                        contactCursor.close();
                    }
                }
            }

        }
        catch (Exception ex){

        }

        return myWhatsappNumbers;
    }

    private void startHeavyProcessing() {
        if(MainActivity.WhatsupNumbers == null){
            progressBar.setVisibility(View.VISIBLE);
            new ContactsFragment.LongOperation(this).execute();
        }
        else{
            setResultContacts(MainActivity.WhatsupNumbers);
        }

    }
    private class LongOperation extends AsyncTask<Void, Void, HashMap<String, String>> {

        ContactsFragment _fragment;

        public LongOperation(ContactsFragment fragment) {
            _fragment = fragment;
        }

        public LongOperation() {
            super();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            HashMap<String, String> list = null;
            try {
                list = getWhatsupContacts();

            } catch (Exception ex) {

            } finally {
            }

            return list;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            super.onPostExecute(result);
            try {
                MainActivity.WhatsupNumbers = result;
                _fragment.setResultContacts(result);
            } catch (Exception ex) {
                String e = ex.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        loadcontacts(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        loadcontacts(s);

        return false;
    }
}
