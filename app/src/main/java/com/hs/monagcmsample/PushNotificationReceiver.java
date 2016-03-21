package com.hs.monagcmsample;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created on 21/03/16.
 */
public class PushNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String message = intent.getExtras().getString("message");
            if(message!=null){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Mona GCM Sample")
                        .setContentText(message)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setAutoCancel(true);

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1,mBuilder.build());
            } else {
                Log.e("Mona GCM Sample", "Missing message from the payload");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
