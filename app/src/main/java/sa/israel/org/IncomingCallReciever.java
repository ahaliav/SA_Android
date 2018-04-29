package sa.israel.org;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Ahaliav on 19/11/2017.
 */

public class IncomingCallReciever extends BroadcastReceiver {

    public static boolean wasAnwerd;
    private Context mContext;
    private Intent mIntent;
    String name = "";
    String email = "";
    boolean exite = true;
    boolean contactExsits = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            exite = true;
            contactExsits = false;

            mContext = context;
            mIntent = intent;

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int events = PhoneStateListener.LISTEN_CALL_STATE;
            tm.listen(phoneStateListener, events);
        } catch (Exception ex) {

        }
    }

    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            checkIfToShowPopup(incomingNumber);

            if (exite || contactExsits == false)
                return;

            try {
                String callState = "UNKNOWN";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE://phone closed
                        callState = "IDLE";

                        if (wasAnwerd == false) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    openUnansweredActivity();
                                }
                            }, 1500);

                        } else {
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: //incoming call
                        wasAnwerd = false;
                        // -- check international call or not.
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                openIncomingCallActivity();
                            }
                        }, 500);

                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: //call answered or calling
                        wasAnwerd = true;
                        try {
                            IncomingCallActivity.instance.finish();
                        } catch (Exception ex) {
                        }

                        try {
                            unansweredCallActivity.instance.finish();
                        } catch (Exception ex) {
                        }

                        String dialingNumber = mIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                        if (dialingNumber.length() > 8)
                            wasAnwerd = true;

                        break;
                }

                super.onCallStateChanged(state, incomingNumber);
            } catch (Exception ex) {

            }
        }

        private void checkIfToShowPopup(String incomingNumber) {
            exite = true;
            contactExsits = false;
            name = "";
            try {
                SQLiteDbHelper db = new SQLiteDbHelper(mContext);
                Cursor result = db.selectSettings("calldialog");


                if (result != null) {
                    while (result.moveToNext()) {
                        int id = result.getInt(0);
                        String key = result.getString(1);
                        String val = result.getString(2);
                        if (val.equals("true"))
                            exite = false;
                    }

                    if (!result.isClosed())
                        result.close();
                }

                result = db.selectContacts(-1, incomingNumber, "");

                if (result != null) {
                    while (result.moveToNext()) {
                        contactExsits = true;
                        name = result.getString(1);
                        email = result.getString(3);
                        break;
                    }

                    if (!result.isClosed())
                        result.close();
                }

            } catch (Exception ex) {

            }
        }

        private void openUnansweredActivity() {

            try {
                IncomingCallActivity.instance.finish();
            } catch (Exception ex) {
            }

            exite = true;
            contactExsits = false;
            String number = mIntent.getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            checkIfToShowPopup(number);

            if (exite || contactExsits == false)
                return;

            Intent curintent = new Intent(mContext, unansweredCallActivity.class);
            mIntent.putExtra("name", name);
            mIntent.putExtra("email", email);
            curintent.putExtras(mIntent);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            curintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            curintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            curintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);



            mContext.startActivity(curintent);
        }

        private void openIncomingCallActivity() {
            Intent curintent = new Intent(mContext, IncomingCallActivity.class);
            mIntent.putExtra("name", name);
            curintent.putExtras(mIntent);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            curintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            curintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            curintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            mContext.startActivity(curintent);

        }


    };

}