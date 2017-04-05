package com.cobrodigital.com.cobrodigital2.Services.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.cobrodigital.com.cobrodigital2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notificador extends FirebaseMessagingService {
    int notificationID=0;
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getData().get("titulo"),remoteMessage.getData().get("cuerpo"),remoteMessage.getData().get("activity"));
    }

    private void sendNotification(String messageTitle,String messageBody,String clase) {
        Intent intent = null;
        try {
            //ejemplo Boletas.Boleta
            Class modulo=Class.forName("com.cobrodigital.com.cobrodigital2.Modulos."+clase);
            intent = new Intent(this,modulo);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */,
                    intent,PendingIntent.FLAG_UPDATE_CURRENT);

            long[] pattern = {500,500,500,500,500};

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.RED,1,1)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID /* ID of notification */, notificationBuilder.build());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
