package sa.israel.org;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImportContactsFragment extends Fragment {

    ListView listview = null;
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

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 12);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 12);
            }
        }

        ArrayList<ImportContact> list = getContactList();
        final ImportContactsAdapter adapter = new ImportContactsAdapter(this, list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);

        return v;
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

                    if(!name.equals(phoneNo)){
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

}
