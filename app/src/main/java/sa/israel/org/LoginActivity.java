package sa.israel.org;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;

import sa.israel.org.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements IObjCallbackMethod {
    WebSiteHelper helper;
    EditText txtPassword;
    Spinner email_address_view;
    Button btnSelectAccount;
    //CheckBox chkRememberMe;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnPasswordReset = (Button)findViewById(R.id.btnPasswordReset);
        btnSelectAccount = (Button)findViewById(R.id.btnSelectAccount);

        txtPassword = (EditText)findViewById(R.id.txtPassword);
        //chkRememberMe = (CheckBox)findViewById(R.id.chkRememberMe);
        email_address_view = (Spinner) findViewById(R.id.email_address_view);

        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        helper = new WebSiteHelper(this, getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            email_address_view.setVisibility(View.GONE);
            btnSelectAccount.setVisibility(View.VISIBLE);
        }
        else {
            email_address_view.setVisibility(View.VISIBLE);
            btnSelectAccount.setVisibility(View.GONE);
            loadAccounts();
        }


        btnSelectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUserAccount();
            }
        });

        btnPasswordReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sa-israel.org/password-reset/"));
                startActivity(browserIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(txtPassword.getText().toString().trim().equals("") ){
                    txtPassword.setError(getResources().getString(R.string.field_required));
                }
                else {
                    try{
                        spinner.setVisibility(View.VISIBLE);
                        String userName = "";
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            userName = btnSelectAccount.getText().toString();
                        } else {
                            userName = email_address_view.getSelectedItem().toString();
                        }

                        helper.login(userName,txtPassword.getText().toString());
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    catch (Exception ex){
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(Constants.GO_TO_REGISTER_KEY, "true");
                    startActivity(intent);
                }
                catch (Exception ex){

                }
            }
        });
    }

    @Override
    public void onTaskDone(Object obj) {

        try{
            SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
            boolean result = (Boolean) obj;
            final Intent intent = new Intent(this, MainActivity.class);
            if(result){

                String userName = "";
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    userName = btnSelectAccount.getText().toString();
                } else {
                    userName = email_address_view.getSelectedItem().toString();
                }

                if(MainActivity.IsConfirmed(userName)){
                    db.insertSettings(Constants.IS_REGISTERED_KEY, "true");
                    db.insertSettings(Constants.IS_LOGEDIN_KEY, "true");
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage(getResources().getString(R.string.you_are_note_confirmed));
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
            else {
                db.insertSettings(Constants.IS_LOGEDIN_KEY, "false");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_username_or_password),
                        Toast.LENGTH_LONG).show();

                txtPassword.setText("");
            }
            spinner.setVisibility(View.GONE);
        }
        catch (Exception ex){
            spinner.setVisibility(View.GONE);
        }
    }

    private void loadAccounts() {
        ArrayList<String> emails = new ArrayList<>();

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }

        Spinner expAccounts = (Spinner) findViewById(R.id.email_address_view);

        ArrayAdapter<String> myAdapter = new AccountsAdapter(this, R.layout.item_account_spinner, emails);
        expAccounts.setAdapter(myAdapter);
    }

    public void pickUserAccount() {
        /*This will list all available accounts on device without any filtering*/
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                null, false, null, null, null, null);
        startActivityForResult(intent, 23);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 23) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                btnSelectAccount.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            } else if (resultCode == RESULT_CANCELED) {
                btnSelectAccount.setText(getResources().getString(R.string.select_account));
            }
        }
    }
}
