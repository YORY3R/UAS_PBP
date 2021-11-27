package com.yory3r.e_learning.activities;

import static com.yory3r.e_learning.notifications.App.CHANNEL_1_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivitySplashBinding;
import com.yory3r.e_learning.notifications.NotificationReceiver;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity
{
    private Intent intent;
    private NotificationManagerCompat compat;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_splash;
        binding = DataBindingUtil.setContentView(SplashActivity.this,layout);
        binding.setActivitySplashScreen(SplashActivity.this);

        compat = NotificationManagerCompat.from(this);

        pushNotification();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                intent = new Intent(SplashActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }

    private void pushNotification()
    {
        String title = null;
        String message;

        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12)
        {
            title = "Hallo, Selamat Pagi";
        }
        else if(timeOfDay >= 12 && timeOfDay < 16)
        {
            title = "Hallo, Selamat Siang";
        }
        else if(timeOfDay >= 16 && timeOfDay < 21)
        {
            title = "Hallo, Selamat Sore";
        }
        else if(timeOfDay >= 21 && timeOfDay < 24)
        {
            title = "Hallo, Selamat Malam";
        }

        message = "Jam : " + String.valueOf(timeOfDay);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_emoji_emotions_24);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText("Selamat datang dan selamat bergabung di aplikasi kami");
        bigTextStyle.setSummaryText("Welcome");

        Intent activityIntent = new Intent(SplashActivity.this, RegisterActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(SplashActivity.this, 0,activityIntent,0);

        Intent broadcastIntent = new Intent(SplashActivity.this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage","Terima Kasih :D");
        PendingIntent actionIntent = PendingIntent.getBroadcast(SplashActivity.this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,CHANNEL_1_ID);
        notification.setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setStyle(bigTextStyle)
                .addAction(R.mipmap.ic_launcher,"Suka",actionIntent);

        compat.notify(1,notification.build());
    }
}