package com.example.samsungschoolproject.notificator;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlannedWorkoutNotificator {
    public static final String CHANNEL_ID = "ASAFADF"; // TODO: я не знаю, зачем это нужно

    public static void scheduleNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        // Устанавливается время рассылки: 12:00 каждого дня
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Установка дневного интервала между рассылками
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void createNotificationChannel(Context context){
        // TODO: я не знаю, на что влияют название и описание канала
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

    @SuppressLint({"NotificationPermission", "MissingPermission"})
    public static void sendNotification(Context context){
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Планировщик тренировок")
                .setContentText("У вас еще запланировано несколько тренировок на сегодня!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public static class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            PlannedWorkoutNotificator.scheduleNotification(context);

            // Необходимо проверить, что на сегодня тренировки запланированы - тогда отправить уведомление
            WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(context);

            CompletableFuture<List<PlannedWorkout>> future = CompletableFuture.supplyAsync(() -> database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(LocalDate.now().toString()));
            List<PlannedWorkout> plannedWorkouts = null;
            try {
                plannedWorkouts = future.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Проверка о том, что уведомление отправляется в нужное время (т.к. работает с багами)
            if (!plannedWorkouts.isEmpty() && 11 <= LocalDateTime.now().getHour() && LocalDateTime.now().getHour() <= 13){
                sendNotification(context);
            }
        }
    }
}
