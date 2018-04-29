package sa.israel.org;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ahaliav on 26/02/2018.
 */

public class SaFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG= "TAG";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(isShowNotifications()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new  NotificationCompat.Builder(this);

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setTicker("Hearty365")
                    .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentInfo("Info");

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean isShowNotifications() {
        SQLiteDbHelper db = new SQLiteDbHelper(this);
        Cursor result = db.selectSettings("");

        if(result != null) {

            while (result.moveToNext()) {

                int id = result.getInt(0);
                String key = result.getString(1);
                String val = result.getString(2);

                if(key.equals("notifications")){
                    if(val.equals("true"))
                        return true;
                    else
                        return false;
                }
            }

            if (!result.isClosed())  {
                result.close();
            }
        }

        return false;
    }
}
