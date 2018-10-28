package sawachats.apps.alirabie.com.sawachats;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Eng Ali on 10/28/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String click_action= remoteMessage.getNotification().getClickAction();
        String fromUserId = remoteMessage.getData().get("from_user_id");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("userId",fromUserId);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId=(int)System.currentTimeMillis();
        NotificationManager mNotfyManger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotfyManger.notify(mNotificationId,mBuilder.build());



    }




}