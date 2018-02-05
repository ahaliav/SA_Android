package sa.israel.org;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import sa.israel.org.R;

public class unansweredCallActivity extends AppCompatActivity implements View.OnClickListener{

    String number = "";
    String email = "";
    ImageButton btnEmail;
    ImageButton btnCall;
    ImageButton btnSms;

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
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            setContentView(R.layout.activity_unanswered_call);

            this.setFinishOnTouchOutside(false);

            number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            String name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            TextView tvName = (TextView) findViewById(R.id.tvName);
            TextView tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);

            tvPhoneNumber.setText(number);
            tvName.setText(name);

            btnEmail=(ImageButton)findViewById(R.id.btnEmail);
            btnCall=(ImageButton)findViewById(R.id.btnCall);
            btnSms=(ImageButton)findViewById(R.id.btnSms);

            btnSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                            + number)));
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(unansweredCallActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(unansweredCallActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                123);
                    }
                    else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                }
            });

            btnEmail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + email);
                    intent.setData(data);
                    startActivity(Intent.createChooser(intent, ""));
                }
            });


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

    @Override
    public void onClick(View v) {

        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
