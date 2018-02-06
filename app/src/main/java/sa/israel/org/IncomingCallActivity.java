package sa.israel.org;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        try {
            //setContentView(R.layout.activity_incoming_call);

            this.setFinishOnTouchOutside(true);

            displayAlert();

        } catch (Exception ex) {
            Log.d("Exception", ex.toString());
        }
    }

    private void displayAlert()
    {
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        final Dialog dialog = new Dialog(IncomingCallActivity.this);
        dialog.setContentView(R.layout.activity_incoming_call);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
        TextView tvPhoneNumber = (TextView) dialog.findViewById(R.id.tvPhoneNumber);

        String number = getIntent().getStringExtra(
                TelephonyManager.EXTRA_INCOMING_NUMBER);
        String name = getIntent().getStringExtra("name");

        tvName.setText(name);
        tvPhoneNumber.setText(number);

        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });
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
