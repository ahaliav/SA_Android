package sa.israel.org;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImportContactsFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView searchgroup = null;
    ListView listview = null;
    ProgressBar progressBar;
    TextView tvLoadText;
    ArrayList<ImportContact> contactsToImport;

    public ImportContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_import_contacts, container, false);
        listview = (ListView) v.findViewById(R.id.listviewConatcts);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        tvLoadText = (TextView) v.findViewById(R.id.tvLoadText);
        searchgroup = (SearchView)v.findViewById(R.id.searchgroup);
        searchgroup.setOnQueryTextListener(this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 12);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 12);
            }
        }



        startHeavyProcessing();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.importPhoneContacts));

        return v;
    }

    public void setResultContacts(ArrayList<ImportContact> list){
        final ImportContactsAdapter adapter = new ImportContactsAdapter(ImportContactsFragment.this, list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        tvLoadText.setVisibility(View.GONE);
    }

    private void startHeavyProcessing() {

        if(MainActivity.Contacts == null){
            progressBar.setVisibility(View.VISIBLE);
            tvLoadText.setVisibility(View.VISIBLE);
            new LongOperation(this).execute();
        }
        else {
            setResultContacts(MainActivity.Contacts);
        }

    }

    private class LongOperation extends AsyncTask<Void, Void, ArrayList<ImportContact>> {

        ImportContactsFragment _fragment;
        public LongOperation(ImportContactsFragment fragment){
            _fragment = fragment;
        }

        public LongOperation() {
            super();
        }

        @Override
        protected ArrayList<ImportContact> doInBackground(Void... params) {
            ArrayList<ImportContact> list = null;
            try {
                list = getContactList();
            } catch (Exception ex) {

            } finally {
            }

            return list;
        }

        @Override
        protected void onPostExecute( ArrayList<ImportContact> result) {
            super.onPostExecute(result);
            try {
                MainActivity.Contacts = result;
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


    private ArrayList<ImportContact> getContactList() {
        ArrayList<ImportContact> contacts = new ArrayList<ImportContact>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String email = "";
                String phoneNo = "";
                if (!name.equals("") && cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;
                    }

                    Cursor eCur1 = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        email = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA));
                        break;
                    }

                    SQLiteDbHelper db = new SQLiteDbHelper(getContext());
                    if (!name.equals(phoneNo) && !db.isContactExist(phoneNo)) {
                        contacts.add(new ImportContact(name, phoneNo, email));
                    }

                    pCur.close();
                    eCur1.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        return contacts;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        ArrayList<ImportContact> searchContacts = new ArrayList<ImportContact>();
        for(int i=0; i< MainActivity.Contacts.size(); i++){
            if(MainActivity.Contacts.get(i).getName().contains(s)){
                searchContacts.add(MainActivity.Contacts.get(i));
            }
        }
        setResultContacts(searchContacts);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<ImportContact> searchContacts = new ArrayList<ImportContact>();
        for(int i=0; i< MainActivity.Contacts.size(); i++){
            if(MainActivity.Contacts.get(i).getName().contains(s)){
                searchContacts.add(MainActivity.Contacts.get(i));
            }
        }
        setResultContacts(searchContacts);

        return false;
    }

}
