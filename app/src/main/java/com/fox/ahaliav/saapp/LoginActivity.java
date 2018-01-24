package com.fox.ahaliav.saapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements IObjCallbackMethod {
    WebSiteHelper helper;
    EditText txtUserName;
    EditText txtPassword;
    CheckBox chkRememberMe;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnPasswordReset = (Button)findViewById(R.id.btnPasswordReset);

        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        chkRememberMe = (CheckBox)findViewById(R.id.chkRememberMe);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        helper = new WebSiteHelper(this);

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

                if(txtUserName.getText().toString().trim().equals("") ){
                    txtUserName.setError(getResources().getString(R.string.field_required));
                }
                else if(txtPassword.getText().toString().trim().equals("") ){
                    txtPassword.setError(getResources().getString(R.string.field_required));
                }
                else {
                    try{
                        spinner.setVisibility(View.VISIBLE);
                        helper.login(txtUserName.getText().toString(),txtPassword.getText().toString());
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
            if(result){
                MainActivity.IsLoggedIn = true;
                //check if to save login
                if(chkRememberMe.isChecked()){
                    //save to db
                    db.insertSettings(Constants.IS_LOGEDIN_KEY, "true");
                }
                else {
                    db.insertSettings(Constants.IS_LOGEDIN_KEY, "false");
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.IS_LOGEDIN_KEY, "true");
                startActivity(intent);
            }
            else {
                MainActivity.IsLoggedIn = false;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_username_or_password),
                        Toast.LENGTH_LONG).show();
                txtUserName.setText("");
                txtPassword.setText("");
            }
            spinner.setVisibility(View.GONE);
        }
        catch (Exception ex){
            spinner.setVisibility(View.GONE);
        }
    }
}
