package sa.israel.org;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LockActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public String SECRET = "";
    public String SECRET2 = "";
    TextView profile_name;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private int totalseconds = 0;
    private int counter = 0;
    Thread delayThread = null;
    Runnable runnable;
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (profile_name.getText().toString().equals(getResources().getString(R.string.create_your_pin))) {
                SECRET = pin;
                profile_name.setText(getResources().getString(R.string.re_enter_your_pin));
                mPinLockView.resetPinLockView();
            } else if (profile_name.getText().toString().equals(getResources().getString(R.string.re_enter_your_pin))) {
                SECRET2 = pin;
                if (SECRET.equals(SECRET2)) {
                    SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
                    db.insertSettings(Constants.PIN_KEY, pin);
                    MainActivity.IS_OPEN_LOCKED = true;
                    goToMain();
                } else {
                    SECRET = "";
                    SECRET2 = "";
                    profile_name.setText(getResources().getString(R.string.create_your_pin));
                    mPinLockView.resetPinLockView();
                }
            } else {

                SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
                SECRET = db.selectSettingsString(Constants.PIN_KEY);
                if (SECRET.equals(pin)) {
                    MainActivity.IS_OPEN_LOCKED = true;
                    goToMain();
                } else {
                    counter++;
                    totalseconds = 1000 * 10 * counter;

                    if (counter > 2) {
                        mPinLockView.resetPinLockView();
                        if(delayThread == null){
                            runnable = new CountDownRunner();
                            delayThread = new Thread(runnable);
                            delayThread.start();
                        }
                    }
                    else {
                        mPinLockView.resetPinLockView();
                        profile_name.setText(getResources().getString(R.string.wellcome));
                    }
                }
            }
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            if(totalseconds > 0 && counter > 2)
                mPinLockView.resetPinLockView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);

        profile_name = (TextView) findViewById(R.id.profile_name);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        initPin();
    }

    private void initPin() {
        SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
        SECRET = db.selectSettingsString(Constants.PIN_KEY);
        if (SECRET.equals("")) {
            profile_name.setText(getResources().getString(R.string.create_your_pin));
        } else {
            profile_name.setText(getResources().getString(R.string.wellcome));
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    profile_name.setText(getResources().getString(R.string.seconds_left) + ": " + Math.round(totalseconds / 1000) + " " + getResources().getString(R.string.seconds));
                    totalseconds = totalseconds - 1000;
                    if (totalseconds <= 0) {
                        mPinLockView.resetPinLockView();
                        profile_name.setText(getResources().getString(R.string.wellcome));

                    }
                } catch (Exception e) {
                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (totalseconds <= 0) {
                        profile_name.setText(getResources().getString(R.string.wellcome));
                        delayThread = null;
                        runnable = null;
                    }
                    else {
                        Thread.sleep(1000);
                        doWork();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
}
