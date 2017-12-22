package com.fox.ahaliav.saapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements IObjCallbackMethod {
    WebSiteHelper helper;
    EditText txtUserName;
    EditText txtPassword;
    CheckBox chkRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);

        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        chkRememberMe = (CheckBox)findViewById(R.id.chkRememberMe);
        helper = new WebSiteHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    helper.login(txtUserName.getText().toString(),txtPassword.getText().toString());
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(),
                            Toast.LENGTH_LONG).show();
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
        }
        catch (Exception ex){

        }
    }
}
