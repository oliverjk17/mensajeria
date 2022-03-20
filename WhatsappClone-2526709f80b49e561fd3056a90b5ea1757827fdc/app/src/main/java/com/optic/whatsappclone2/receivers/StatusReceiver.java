package com.optic.whatsappclone2.receivers;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.gson.Gson;
import com.optic.whatsappclone2.R;
import com.optic.whatsappclone2.channel.NotificationHelper;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.providers.MessagesProvider;
import com.optic.whatsappclone2.providers.NotificationProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.optic.whatsappclone2.services.MyFirebaseMessagingClient.NOTIFICATION_REPLY;

public class StatusReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //getMyImage(context, intent);
        updateStatus(context, intent);
    }

    private void updateStatus(Context context, Intent intent) {

        int id = intent.getExtras().getInt("idNotification");
        String messagesJSON = intent.getExtras().getString("messages");
        MessagesProvider messagesProvider = new MessagesProvider();

        Gson gson = new Gson();
        Message[] messages = gson.fromJson(messagesJSON, Message[].class);

        for(Message m: messages) {
            messagesProvider.updateStatus(m.getId(), "VISTO");
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);

    }




}
