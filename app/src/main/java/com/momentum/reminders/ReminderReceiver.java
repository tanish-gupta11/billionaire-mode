package com.momentum.reminders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String PERSONAL_CHANNEL = "personal_reminders";
    private static final String DAILY_CHANNEL = "daily_motivation";
    @Override public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text"); int id = intent.getIntExtra("id", 0);
        if (QuoteLibrary.DAILY_QUOTE.equals(text)) {
            QuoteLibrary.Quote quote = QuoteLibrary.forToday();
            text = quote.words + " — " + quote.author;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel personal = new NotificationChannel(PERSONAL_CHANNEL, "Personal reminders", NotificationManager.IMPORTANCE_HIGH);
        personal.setDescription("Your personal commitments and scheduled reminders"); manager.createNotificationChannel(personal);
        NotificationChannel daily = new NotificationChannel(DAILY_CHANNEL, "Daily motivation", NotificationManager.IMPORTANCE_DEFAULT);
        daily.setDescription("Daily verified quotes and mindset prompts"); manager.createNotificationChannel(daily);
        String channelId = id == 1003 ? DAILY_CHANNEL : PERSONAL_CHANNEL;
        PendingIntent open = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Billionaire Mode")
                .setContentText(text)
                .setStyle(new android.app.Notification.BigTextStyle().bigText(text))
                .setContentIntent(open)
                .setAutoCancel(true)
                .setPriority(android.app.Notification.PRIORITY_HIGH);
        manager.notify(id, builder.build());
        ReminderScheduler.scheduleAll(context);
    }
}
