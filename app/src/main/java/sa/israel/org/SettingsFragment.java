package sa.israel.org;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import sa.israel.org.R;

public class SettingsFragment extends Fragment {

    Switch swShowCallDialog;
    Switch swAllowNotifications;
    Switch swLockScreen;
    SQLiteDbHelper db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new SQLiteDbHelper(this.getContext());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        swShowCallDialog = (Switch) v.findViewById(R.id.swShowCallDialog);
        swAllowNotifications = (Switch) v.findViewById(R.id.swAllowNotifications);
        swLockScreen = (Switch) v.findViewById(R.id.swLockScreen);

        loadvalues();


        swShowCallDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.insertSettings("calldialog", (swShowCallDialog.isChecked() == true)? "true":"false");
            }
        });

        swAllowNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.insertSettings("notifications", (swAllowNotifications.isChecked() == true)? "true":"false");
            }
        });

        swLockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.insertSettings(Constants.ENABLE_LOCK_APP, (swLockScreen.isChecked() == true)? "true":"false");
            }
        });

        return v;
    }

    private void loadvalues() {
        Cursor result = db.selectSettings("");

        if(result != null) {

            while (result.moveToNext()) {

                int id = result.getInt(0);
                String key = result.getString(1);
                String val = result.getString(2);

                if(key.equals("calldialog")){
                    if(val.equals("true"))
                        swShowCallDialog.setChecked(true);
                    else
                        swShowCallDialog.setChecked(false);
                }

                if(key.equals("notifications")){
                    if(val.equals("true"))
                        swAllowNotifications.setChecked(true);
                    else
                        swAllowNotifications.setChecked(false);
                }

                if(key.equals(Constants.ENABLE_LOCK_APP)){
                    if(val.equals("true"))
                        swLockScreen.setChecked(true);
                    else
                        swLockScreen.setChecked(false);
                }
            }

            if (!result.isClosed())  {
                result.close();
            }
        }
    }
}
