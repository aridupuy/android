package com.cobrodigital.com.cobrodigital2.Services.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notificador extends FirebaseMessagingService {
    int notificationID=0;
    public Notificador() {

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        CharSequence ticker ="Aviso";
        CharSequence contentTitle = remoteMessage.getNotification().getTitle();
        CharSequence contentText = remoteMessage.getNotification().getBody();
        Notification noti = new NotificationCompat.Builder(this)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setVibrate(new long[] {100, 250, 100, 500})
                .build();
        nm.notify(++notificationID, noti);
//        Log.e("FIREBASE", remoteMessage.getNotification().getBody());
    }
}
