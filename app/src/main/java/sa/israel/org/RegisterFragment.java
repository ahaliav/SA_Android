package sa.israel.org;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements IObjCallbackMethod {

    Button btnRegister;
    Button btnCancel;
    Button btnLogin;
    EditText txtPassword;
    EditText txtPasswordConf;
    EditText txtPhone;
    EditText txtName;
    EditText txtComments;
    Spinner email_address_view;
    String nonce;
    Menu menu;
    WebSiteHelper helper;
    private ProgressBar spinner;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        setLoginRegisterBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);

        txtPassword = (EditText) v.findViewById(R.id.txtPassword);
        txtPasswordConf = (EditText) v.findViewById(R.id.txtPasswordConf);
        txtPhone = (EditText) v.findViewById(R.id.txtPhone);
        txtName = (EditText) v.findViewById(R.id.txtName);
        txtComments = (EditText) v.findViewById(R.id.txtComments);
        email_address_view = (Spinner) v.findViewById(R.id.email_address_view);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        helper = new WebSiteHelper(this, getContext());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean containsErrors = false;
                if (txtName.getText().toString().trim().equals("")) {
                    txtName.setError(getResources().getString(R.string.field_required));
                    containsErrors = true;
                }

                if (txtPassword.getText().toString().trim().equals("") || txtPassword.getText().toString().trim().length() < 6) {
                    txtPassword.setError(getResources().getString(R.string.password_required));
                    containsErrors = true;
                }

                if (txtPasswordConf.getText().toString().trim().equals("") || !txtPassword.getText().toString().equals(txtPasswordConf.getText().toString().trim())) {
                    txtPassword.setError(getResources().getString(R.string.passwords_do_not_match));
                    containsErrors = true;
                }

                if (txtPhone.getText().toString().trim().equals("")) {
                    txtPhone.setError(getResources().getString(R.string.field_required));
                    containsErrors = true;
                }

                if (txtComments.getText().toString().trim().equals("")) {
                    txtComments.setError(getResources().getString(R.string.field_required));
                    containsErrors = true;
                }

                try {
                    if (containsErrors == false) {
                        spinner.setVisibility(View.VISIBLE);
                        helper.register(
                                email_address_view.getSelectedItem().toString(),
                                txtPassword.getText().toString(),
                                txtName.getText().toString(),
                                txtPhone.getText().toString(),
                                txtComments.getText().toString()
                        );
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.GONE);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, new MainFragment())
                        .commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        loadAccounts(v);

        setHasOptionsMenu(true);

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
        if (obj != null && !obj.toString().toLowerCase().contains("error")) {
            SQLiteDbHelper db = new SQLiteDbHelper(getContext());
            db.insertSettings(Constants.IS_REGISTERED_KEY, "true");

            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(getResources().getString(R.string.reg_success_message));
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            setLoginRegisterBar();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_fragment_container, new MainFragment())
                                    .commit();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            spinner.setVisibility(View.GONE);
        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(getResources().getString(R.string.reg_failed_message) + "Message: " + obj.toString());
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            setLoginRegisterBar();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_fragment_container, new MainFragment())
                                    .commit();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void setLoginRegisterBar() {
        SQLiteDbHelper db = new SQLiteDbHelper(getContext());
        String isReg = db.selectSettingsString(Constants.IS_REGISTERED_KEY);
        String isLoggedin = db.selectSettingsString(Constants.IS_LOGEDIN_KEY);

        MenuItem action_register = menu.findItem(R.id.action_register);
        action_register.setVisible(false);
        MenuItem action_login = menu.findItem(R.id.action_login);
        action_login.setVisible(true);
        MenuItem action_exit = menu.findItem(R.id.action_exit);
        action_exit.setVisible(false);
    }
}
