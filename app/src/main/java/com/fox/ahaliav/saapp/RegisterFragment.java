package com.fox.ahaliav.saapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
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
        View v= inflater.inflate(R.layout.fragment_register, container, false);

        loadAccounts(v);

        return v;
    }

    private void loadAccounts(View view) {

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
        Cursor result = db.selectUser("");

        ArrayList<String> emails = new ArrayList<>();

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }

        Spinner expAccounts = (Spinner) view.findViewById(R.id.email_address_view);

        ArrayAdapter<String> myAdapter = new AccountsAdapter(getActivity(), R.layout.item_account_spinner, emails);
        expAccounts.setAdapter(myAdapter);

    }
}
