package sa.israel.org;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<Contact> _listDataHeader; // header titles
    private HashMap<Integer, Contact> _listDataChild;
    FragmentManager manager;
    Activity activity;
    HashMap<String, String> watsupContacts;
    private ArrayList<Contact> dataSet;
    private final boolean[] mCheckedState;
    private int selectedPosition = -1;
    ContactsFragment _fragment;
    public ContactAdapter(Activity activity,ContactsFragment fragment, Context context, ArrayList<Contact> listDataHeader,
                          HashMap<Integer, Contact> listChildData, FragmentManager manager, HashMap<String, String> whatsupNumbers) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.manager = manager;
        this.activity = activity;
        watsupContacts = whatsupNumbers;
        _fragment = fragment;
        mCheckedState = new boolean[listDataHeader.size()];
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Contact headerTitle = (Contact) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_contact_group, null);
        }

        TextView tvListHeader = (TextView) convertView.findViewById(R.id.tvListHeader);
        final ToggleButton btnFavorites = (ToggleButton) convertView.findViewById(R.id.btnFavorites);


        if(headerTitle.getOrderby() < 3000){
            btnFavorites.setChecked(true);
        }else {
            btnFavorites.setChecked(false);
        }
        btnFavorites.setTag(groupPosition);

        btnFavorites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SQLiteDbHelper db = new SQLiteDbHelper(_context);

                selectedPosition = (int) v.getTag();
                Contact tt = (Contact) getGroup(selectedPosition);
                try {
                    if(btnFavorites.isChecked()){
                        db.updateContactOrderBy(tt.getName(), 0);
                    }
                    else{
                        db.updateContactOrderBy(tt.getName(), 3000);
                    }
                    _fragment.loadcontacts("");
                    //Button is OFF
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //tvListHeader.setTypeface(null, Typeface.BOLD);
        tvListHeader.setText(headerTitle.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final View view = convertView;
        final Contact contact = (Contact) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_contact, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        FrameLayout mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        tvName.setText(contact.getName());
        tvPhoneNumber.setText(contact.getPhoneNumber());
        tvComments.setText(contact.getComments());
        tvEmail.setText(contact.getEmail());

        ImageButton btnEmail = (ImageButton) convertView.findViewById(R.id.btnEmail);
        ImageButton btnCall = (ImageButton) convertView.findViewById(R.id.btnCall);
        ImageButton btnEdit = (ImageButton) convertView.findViewById(R.id.btnEdit);
        ImageButton btnSms = (ImageButton) convertView.findViewById(R.id.btnSms);
        ImageButton btnShare = (ImageButton) convertView.findViewById(R.id.btnShare);
        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
        ImageButton btnWup = (ImageButton) convertView.findViewById(R.id.btnWup);

        String phone = contact.getPhoneNumber();
        try{
            if(phone.length() > 9){
                btnWup.setVisibility(View.VISIBLE);
                boolean found = false;
                for(Map.Entry<String, String> entry : watsupContacts.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.substring(key.length()-8).contains(phone.substring(phone.length() -8))){
                        found = true;
                    }
                }

                if(found == false)
                    btnWup.setVisibility(View.GONE);
            }
            else {
                btnWup.setVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            btnWup.setVisibility(View.GONE);
        }


        btnWup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    String wUphone = "";
                    String phone = contact.getPhoneNumber();

                    if (phone.length() >= 9) {
                        Intent whatsup = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + GetFixedPhoneZipCode(phone)));
                        whatsup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(whatsup);
                    }
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Uri uri = Uri.parse("sms:" + contact.getPhoneNumber());
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    String sharevia = _context.getResources().getString(R.string.sharevia);
                    String sharecontact = _context.getResources().getString(R.string.sharecontact);

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = contact.getName() + "\n" + contact.getPhoneNumber() + "\n" + contact.getEmail();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sharecontact);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

                    sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Intent.createChooser(sharingIntent, sharevia));
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Activity activity = manager.findFragmentByTag("ContactsFragment").getActivity();
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CALL_PHONE},
                                123);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhoneNumber()));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(callIntent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    final FragmentTransaction ft = manager.beginTransaction();
                    ContactDetailsFragment contactdetails = new ContactDetailsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("id", contact.getId());
                    bundle.putString("name", contact.getName());
                    bundle.putString("phone", contact.getPhoneNumber());
                    bundle.putString("comments", contact.getComments());
                    bundle.putString("email", contact.getEmail());

                    contactdetails.setArguments(bundle);

                    ft.replace(R.id.main_fragment_container, contactdetails, "ContactDetailsFragment");
                    ft.addToBackStack("ContactDetailsFragment");
                    ft.commit();
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    MainActivity main = (MainActivity) manager.findFragmentByTag("ContactsFragment").getActivity();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(main);
                    builder1.setMessage(_context.getResources().getString(R.string.are_you_sure));
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            _context.getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SQLiteDbHelper db = new SQLiteDbHelper(_context);
                                    db.deleteContact(contact.getId(), "");
                                    final FragmentTransaction ft = manager.beginTransaction();
                                    ContactsFragment contacts = new ContactsFragment();

                                    ft.replace(R.id.main_fragment_container, contacts, "ContactsFragment");
                                    ft.addToBackStack("ContactsFragment");
                                    ft.commit();
                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            _context.getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + contact.getEmail());
                    intent.setData(data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Intent.createChooser(intent, ""));
                } catch (Exception ex) {
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        return convertView;
    }

    public String GetFixedPhoneZipCode(String phone) {
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=activity.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        CountryZipCode = CountryZipCode.replace("+", "");
        //phone = phone.replace("+","").replace(" ","").replace("-","");
        phone = phone.replaceFirst ("^0*", "");
        if(phone.length() < 10 && !phone.replace("+","").replace(" ","").startsWith(CountryZipCode)) {
            return CountryZipCode + phone;
        }
        else {
            return phone.replace("+","");
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
