package com.fox.ahaliav.saapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements IObjCallbackMethod {

    Button btnRegister;
    Button btnCancel;
    EditText txtPassword;
    EditText txtPhone;
    EditText txtName;
    EditText txtComments;
    Spinner email_address_view;
    String nonce;

    WebSiteHelper helper;

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
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        txtPassword = (EditText) v.findViewById(R.id.txtPassword);
        txtPhone = (EditText) v.findViewById(R.id.txtPhone);
        txtName = (EditText) v.findViewById(R.id.txtName);
        txtComments = (EditText) v.findViewById(R.id.txtComments);
        email_address_view = (Spinner) v.findViewById(R.id.email_address_view);

        helper = new WebSiteHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.register(
                        email_address_view.getSelectedItem().toString(),
                        txtPassword.getText().toString(),
                        txtName.getText().toString(),
                        txtPhone.getText().toString(),
                        txtComments.getText().toString()
                );
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
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

    @Override
    public void onTaskDone(Object obj) {
        if (obj != null) {

            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(getResources().getString(R.string.reg_success_message));
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            getFragmentManager().popBackStack();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}
