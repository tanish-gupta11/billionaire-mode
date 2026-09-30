package com.momentum.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class ReminderScheduler {
    public static void schedule(Context context, Reminder reminder) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class).putExtra("id", reminder.id).putExtra("text", reminder.text);
        PendingIntent pending = PendingIntent.getBroadcast(context, reminder.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar next = Calendar.getInstance(); next.set(Calendar.HOUR_OF_DAY, reminder.hour); next.set(Calendar.MINUTE, reminder.minute); next.set(Calendar.SECOND, 0); next.set(Calendar.MILLISECOND, 0);
        if (next.getTimeInMillis() <= System.currentTimeMillis()) next.add(Calendar.DAY_OF_YEAR, 1);
        if (manager.canScheduleExactAlarms()) manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pending);
        else manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pending);
    }
    public static void cancel(Context context, int id) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(pending);
    }
    public static void scheduleAll(Context context) {
        for (Reminder r : ReminderStore.load(context)) {
            if (r.enabled) schedule(context, r); else cancel(context, r.id);
        }
    }
}
