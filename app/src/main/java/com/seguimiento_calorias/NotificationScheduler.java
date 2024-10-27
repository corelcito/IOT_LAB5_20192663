package com.seguimiento_calorias;

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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationScheduler {

    private static final String CHANNEL_ID = "scheduled_notifications_channel";
    private static final int NOTIFICATION_ID_BASE = 1000;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificaciones Programadas";
            String description = "Canal para notificaciones programadas";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleNotification(Context context, String message, int notificationId, long startTime, long repeatInterval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("repeatInterval", repeatInterval);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Programar la primera notificación en startTime
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);

        Toast.makeText(context, "Notificación agregada.", Toast.LENGTH_SHORT).show();
    }
    public static class NotificationReceiver extends BroadcastReceiver {
        @SuppressLint({"MissingPermission", "ScheduleExactAlarm"})
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            int notificationId = intent.getIntExtra("notificationId", 0);
            long repeatInterval = intent.getLongExtra("repeatInterval", 0);

            // Mostrar la notificación
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_notification_important_24)
                    .setContentTitle("Recordatorio")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, notification);

            // Programar repeticiones solo si el intervalo es mayor que 0
            if (repeatInterval > 0) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent repeatIntent = new Intent(context, NotificationReceiver.class);
                repeatIntent.putExtra("message", message);
                repeatIntent.putExtra("notificationId", notificationId);
                repeatIntent.putExtra("repeatInterval", repeatInterval);

                PendingIntent repeatPendingIntent = PendingIntent.getBroadcast(
                        context,
                        notificationId,
                        repeatIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                // Programar la repetición
                long nextTriggerTime = System.currentTimeMillis() + repeatInterval;
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerTime, repeatPendingIntent);
            }
        }
    }

    public static void cancelAllNotifications(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Notificaciones canceladas.", Toast.LENGTH_SHORT).show();
    }
}