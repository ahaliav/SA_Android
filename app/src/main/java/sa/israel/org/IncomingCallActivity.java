package sa.israel.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import sa.israel.org.R;

public class IncomingCallActivity extends AppCompatActivity implements View.OnClickListener {

    public static IncomingCallActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        try {
            unansweredCallActivity.instance.finish();
        } catch (Exception ex) {

        }

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            setContentView(R.layout.activity_incoming_call);

            this.setFinishOnTouchOutside(false);

            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            String name = getIntent().getStringExtra("name");
            TextView tvName = (TextView) findViewById(R.id.tvName);
            TextView tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);

            tvPhoneNumber.setText(number);
            tvName.setText(name);
        } catch (Exception ex) {
            Log.d("Exception", ex.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent i) {
        if (i.getBooleanExtra("finish", false)) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        finish();

        return super.onKeyDown(keyCode, event);
    }
}
