package com.example.samsungschoolproject.noificator;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.samsungschoolproject.R;

import java.util.Calendar;

public class ExampleNotificator {
    public static final String CHANNEL_ID = "ASAFADF";

    public static void scheduleNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    60*1000, pendingIntent);
            Log.d("BACK_TASK", "ALARM_MANAGER");
        }
    }

    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @SuppressLint({"NotificationPermission", "MissingPermission"})
    public static void sendNotification(Context context){
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("УВЕДОМЛЕНИЕ")
                .setContentText("БЛА БЛА БЛА")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public static class NotificationReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            ExampleNotificator.scheduleNotification(context);
            sendNotification(context);
        }
    }
}
