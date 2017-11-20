package com.fox.ahaliav.saapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class unansweredCallActivity extends AppCompatActivity {

    public static unansweredCallActivity instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        try{
            IncomingCallActivity.instance.finish();
        }
        catch (Exception ex){

        }
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            setContentView(R.layout.activity_unanswered_call);

            this.setFinishOnTouchOutside(false);

            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            TextView tvName = (TextView) findViewById(R.id.tvName);
            TextView tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);

            tvPhoneNumber.setText(number);
            tvName.setText("אליאב הפלצן");
        }
        catch (Exception ex){
            Log.d("Exception", ex.toString());
        }
    }

    @Override
    protected void onNewIntent (Intent i){
        if( i.getBooleanExtra("finish",false) ){
            finish();
        }
    }
}
