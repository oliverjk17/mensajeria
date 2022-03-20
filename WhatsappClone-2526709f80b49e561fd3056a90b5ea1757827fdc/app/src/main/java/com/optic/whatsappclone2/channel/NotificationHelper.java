package com.optic.whatsappclone2.channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.optic.whatsappclone2.R;
import com.optic.whatsappclone2.models.Message;

import java.util.Date;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.optic.whatsappclone2";
    private static final String CHANNEL_NAME = "WhatsappClone";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getNotification(String title, String body) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setColor(Color.GRAY)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    public NotificationCompat.Builder getNotificationMessage(
            Message[] messages,
            String myMessage,
            String usernameSender,
            Bitmap bitmapReceiver,
            Bitmap myBitmap,
            NotificationCompat.Action actionResponse,
            NotificationCompat.Action actionStatus,
            PendingIntent contentIntent

    ) {
        Person myPerson = null;
        Person receiverPerson = null;

        if (bitmapReceiver == null) {
            receiverPerson = new Person.Builder()
                    .setName(usernameSender)
                    .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.ic_person))
                    .build();
        }
        else {
            receiverPerson = new Person.Builder()
                    .setName(usernameSender)
                    .setIcon(IconCompat.createWithBitmap(bitmapReceiver))
                    .build();
        }


        if (myBitmap == null) {
            myPerson = new Person.Builder()
                    .setName("Tu")
                    .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.ic_person))
                    .build();
        }
        else {
            myPerson = new Person.Builder()
                    .setName("Tu")
                    .setIcon(IconCompat.createWithBitmap(myBitmap))
                    .build();
        }

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(receiverPerson);

        for (Message m: messages) {
            NotificationCompat.MessagingStyle.Message messageNotification = new NotificationCompat.MessagingStyle.Message(
                    m.getMessage(),
                    m.getTimestamp(),
                    receiverPerson
            );
            messagingStyle.addMessage(messageNotification);
        }

        if (!myMessage.equals("")) {
            NotificationCompat.MessagingStyle.Message myMessageNotification = new NotificationCompat.MessagingStyle.Message(
                    myMessage,
                    new Date().getTime(),
                    myPerson
            );
            messagingStyle.addMessage(myMessageNotification);
        }


        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(messagingStyle)
                .addAction(actionResponse)
                .setContentIntent(contentIntent);

        if (actionStatus != null) {
            notification.addAction(actionStatus);
        }

        return notification;
    }


}
