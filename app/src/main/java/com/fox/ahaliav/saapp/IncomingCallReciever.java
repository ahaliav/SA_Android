package com.fox.ahaliav.saapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ahaliav on 19/11/2017.
 */

public class IncomingCallReciever extends BroadcastReceiver {

    public static boolean wasAnwerd;
    private Context mContext;
    private Intent mIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            mContext = context;
            mIntent = intent;

            //Intent i = new Intent(context, ViewDialog.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int events = PhoneStateListener.LISTEN_CALL_STATE;
            tm.listen(phoneStateListener, events);
        } catch (Exception ex) {

        }

    }

    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                SQLiteDbHelper db = new SQLiteDbHelper(mContext);
                Cursor result = db.selectSettings("calldialog");

                boolean exite = false;
                if (result != null) {
                    while (result.moveToNext()) {
                        int id = result.getInt(0);
                        String key = result.getString(1);
                        String val = result.getString(2);
                        if (val.equals("false"))
                            exite = true;
                    }

                    if (!result.isClosed())
                        result.close();

                    if (exite)
                        return;
                }
            } catch (Exception ex) {

            }


            try {
                String callState = "UNKNOWN";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
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
                    case TelephonyManager.CALL_STATE_RINGING:
                        wasAnwerd = false;
                        // -- check international call or not.
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                openIncomingCallActivity();
                            }
                        }, 500);

                        if (incomingNumber.startsWith("00")) {
                            Toast.makeText(mContext, "International Call- " + incomingNumber, Toast.LENGTH_LONG).show();
                            callState = "International - Ringing (" + incomingNumber + ")";
                        } else {
                            Toast.makeText(mContext, "Local Call - " + incomingNumber, Toast.LENGTH_LONG).show();
                            callState = "Local - Ringing (" + incomingNumber + ")";


                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        wasAnwerd = true;
                        try {
                            IncomingCallActivity.instance.finish();
                        } catch (Exception ex) {

                        }
                        String dialingNumber = mIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                        if (dialingNumber.startsWith("00")) {
                            Toast.makeText(mContext, "International - " + dialingNumber, Toast.LENGTH_LONG).show();
                            callState = "International - Dialing (" + dialingNumber + ")";
                        } else {

                            Toast.makeText(mContext, "Local Call - " + dialingNumber, Toast.LENGTH_LONG).show();
                            callState = "Local - Dialing (" + dialingNumber + ")";
                        }

                        break;
                }

                super.onCallStateChanged(state, incomingNumber);
            } catch (Exception ex) {
                Log.d("Exception", ex.toString());
            }
        }

        private void openUnansweredActivity() {

            Intent curintent = new Intent(mContext, unansweredCallActivity.class);
            curintent.putExtras(mIntent);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            curintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            mContext.startActivity(curintent);
        }

        private void openIncomingCallActivity() {

            Intent curintent = new Intent(mContext, IncomingCallActivity.class);
            curintent.putExtras(mIntent);
            curintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            curintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            mContext.startActivity(curintent);
        }
    };

}