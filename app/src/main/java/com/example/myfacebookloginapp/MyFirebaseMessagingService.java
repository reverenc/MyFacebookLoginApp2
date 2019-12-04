package com.example.myfacebookloginapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData()!=null)
                sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> data=remoteMessage.getData();
         String title=data.get("title");
         String content=data.get("content");

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID= "sana";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"sana Notification",NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("sana channel for app test FCM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setTicker("Sandhya2105")
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("Info");

        notificationManager.notify(999,notificationBuilder.build());
    }

  /*  @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
       Log.d(TAG,"New token: "+s);
        String a=s;
    }*/

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
        storeToken(s);
    }

    public void storeToken(String s1)
    {
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(s1);
    }

//    public static String getToken(Context context) {
//
//        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
//    }
}
