package com.optic.whatsappclone2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.optic.whatsappclone2.R;
import com.optic.whatsappclone2.adapters.OptionsPagerAdapter;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.models.User;
import com.optic.whatsappclone2.providers.AuthProvider;
import com.optic.whatsappclone2.providers.ImageProvider;
import com.optic.whatsappclone2.providers.NotificationProvider;
import com.optic.whatsappclone2.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfirmImageSendActivity extends AppCompatActivity {

    ViewPager mViewPager;
    String mExtraIdChat;
    String mExtraIdReceiver;
    String mExtraIdNotification;
    ArrayList<String> data;
    ArrayList<Message> messages = new ArrayList<>();

    User mExtraMyUser;
    User mExtraReceiverUser;

    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;
    NotificationProvider mNotificationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image_send);
        setStatusBarColor();

        mViewPager = findViewById(R.id.viewPager);
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();
        mNotificationProvider = new NotificationProvider();

        data = getIntent().getStringArrayListExtra("data");
        mExtraIdChat = getIntent().getStringExtra("idChat");
        mExtraIdReceiver = getIntent().getStringExtra("idReceiver");
        mExtraIdNotification = getIntent().getStringExtra("idNotification");

        String myUser = getIntent().getStringExtra("myUser");
        String receiverUser = getIntent().getStringExtra("receiverUser");

        Gson gson = new Gson();
        mExtraMyUser = gson.fromJson(myUser, User.class);
        mExtraReceiverUser = gson.fromJson(receiverUser, User.class);


        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Message m = new Message();
                m.setIdChat(mExtraIdChat);
                m.setIdSender(mAuthProvider.getId());
                m.setIdReceiver(mExtraIdReceiver);
                m.setStatus("ENVIADO");
                m.setTimestamp(new Date().getTime());
                m.setType("imagen");
                m.setUrl(data.get(i));
                m.setMessage("\uD83D\uDCF7imagen");
                messages.add(m);
            }
        }


        OptionsPagerAdapter pagerAdapter = new OptionsPagerAdapter(
            getApplicationContext(),
            getSupportFragmentManager(),
            dpToPixels(2, this),
            data
        );
        ShadowTransformer transformer = new ShadowTransformer(mViewPager, pagerAdapter);
        transformer.enableScaling(true);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(false, transformer);

    }

    public void send() {
        mImageProvider.uploadMultiple(ConfirmImageSendActivity.this, messages);

        final Message message = new Message();
        message.setIdChat(mExtraIdChat);
        message.setIdSender(mAuthProvider.getId());
        message.setIdReceiver(mExtraIdReceiver);
        message.setMessage("\uD83D\uDCF7 Imagen");
        message.setStatus("ENVIADO");
        message.setType("texto");
        message.setTimestamp(new Date().getTime());
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);

        sendNotification(messages);
        finish();
    }

    private void sendNotification(ArrayList<Message> messages) {
        Map<String, String> data = new HashMap<>();
        data.put("title", "MENSAJE");
        data.put("body", "texto mensaje");
        data.put("idNotification", String.valueOf(mExtraIdNotification));
        data.put("usernameReceiver", mExtraReceiverUser.getUsername());
        data.put("usernameSender", mExtraMyUser.getUsername());
        data.put("imageReceiver", mExtraReceiverUser.getImage());
        data.put("imageSender", mExtraMyUser.getImage());
        data.put("idChat", mExtraIdChat);
        data.put("idSender", mAuthProvider.getId());
        data.put("idReceiver", mExtraIdReceiver);
        data.put("tokenSender", mExtraMyUser.getToken());
        data.put("tokenReceiver", mExtraReceiverUser.getToken());

        Gson gson = new Gson();
        String messagesJSON = gson.toJson(messages);
        data.put("messagesJSON", messagesJSON);
        mNotificationProvider.send(ConfirmImageSendActivity.this, mExtraReceiverUser.getToken(), data);
    }

    public void setMessage(int position, String message) {
        if (message.equals("")) {
            message = "\uD83D\uDCF7imagen";
        }
        messages.get(position).setMessage(message);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack, this.getTheme()));
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack));
        }
    }

}