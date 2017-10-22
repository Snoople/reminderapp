package com.example.r2m.reminderapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Snoople on 10/9/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received ["+remoteMessage+"]");
Intent intent;
        // Create Notification
        String click_action = remoteMessage.getNotification().getClickAction();
        if(click_action=="") {
            intent =  new Intent(this, MainActivity.class);
        }else{
            intent =  new Intent(click_action);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            Log.d("message","s");
            //Log.d("message", remoteMessage.getTo());
            Log.d("message", remoteMessage.getFrom());
            Log.d("message", remoteMessage.getData().get("chat with"));
            Log.d("message", remoteMessage.getData().get("chat from"));
            intent.putExtra("chat with",remoteMessage.getData().get("chat with"));
            intent.putExtra("chat from",remoteMessage.getData().get("chat from"));
            Log.d("message","s");
        }
        catch(NullPointerException e){
            Log.d("message","null");
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);
//intent.putExtra("chat with",remoteMessage.get)



        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
    }
}
