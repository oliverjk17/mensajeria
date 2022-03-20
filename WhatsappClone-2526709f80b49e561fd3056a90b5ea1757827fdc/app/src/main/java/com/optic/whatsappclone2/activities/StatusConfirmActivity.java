package com.optic.whatsappclone2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.optic.whatsappclone2.R;
import com.optic.whatsappclone2.adapters.OptionsPagerAdapter;
import com.optic.whatsappclone2.adapters.StatusPagerAdapter;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.models.Status;
import com.optic.whatsappclone2.models.User;
import com.optic.whatsappclone2.providers.AuthProvider;
import com.optic.whatsappclone2.providers.ImageProvider;
import com.optic.whatsappclone2.providers.NotificationProvider;
import com.optic.whatsappclone2.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusConfirmActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ArrayList<String> data;

    AuthProvider mAuthProvider;
    ImageProvider mImageProvider;

    ArrayList<Status> mStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_confirm);
        setStatusBarColor();

        mViewPager = findViewById(R.id.viewPager);
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImageProvider();

        data = getIntent().getStringArrayListExtra("data");

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                long now = new Date().getTime();
                long limit = now + (60 *1000 * 60 * 24);

                Status s = new Status();
                s.setIdUser(mAuthProvider.getId());
                s.setComment("");
                s.setTimestamp(now);
                s.setTimestampLimit(limit);
                s.setUrl(data.get(i));
                mStatus.add(s);
            }
        }

        StatusPagerAdapter pagerAdapter = new StatusPagerAdapter(
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
        mImageProvider.uploadMultipleStatus(StatusConfirmActivity.this, mStatus);
        finish();
    }

    public void setComment(int position, String comment) {
        mStatus.get(position).setComment(comment);
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