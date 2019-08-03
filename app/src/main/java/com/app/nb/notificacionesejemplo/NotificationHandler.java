package com.app.nb.notificacionesejemplo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler extends ContextWrapper {

    private NotificationManager manager;

    public static final String CHANNEL_HIGH_ID = "1";
    private static final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";

    public static final String CHANNEL_LOW_ID = "2";
    private static final String CHANNEL_LOW_NAME = "LOW CHANNEL";

    public NotificationHandler(Context base) {
        super(base);
        createChannel();
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) { // Compatible con los canales y notificaciones de android
            //Creando el HIGH CHANNEL
            NotificationChannel highChannel = new NotificationChannel(CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);

            // Configuracion extra
            highChannel.enableLights(true);// activa leds parpadeantes
            highChannel.setLightColor(Color.YELLOW);
            highChannel.setShowBadge(true);
            highChannel.enableVibration(true);
            //highChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            highChannel.setSound(defaultSoundUri, null);

            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationChannel lowChannel = new NotificationChannel(CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);

            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            //Crea los canales
            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    public Notification.Builder createNotification(String title, String message, boolean isHighImportance) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (isHighImportance) {
                return createNotificationWithChannel(title, message, CHANNEL_HIGH_ID);
            }
            return createNotificationWithChannel(title, message, CHANNEL_LOW_ID);
        }
        return createNotificationWithoutChannel(title, message);
    }

    private Notification.Builder createNotificationWithChannel(String title, String message, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            return new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(title)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentText(message)
                    .setContentIntent(createAndGetPendingIntent(title, message))
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setAutoCancel(true);
        }
        return null;
    }

    private Notification.Builder createNotificationWithoutChannel(String title, String message) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setAutoCancel(true)
                .setContentIntent(createAndGetPendingIntent(title, message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);
    }

    private PendingIntent createAndGetPendingIntent(String title, String message) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }
}
