package com.momentum.reminders;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ReminderStore {
    private static final String PREFS = "momentum_prefs";
    private static final String KEY = "reminders";
    private static final String SEEDED = "seeded";
    public static List<Reminder> load(Context context) {
        List<Reminder> items = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]"));
            for (int i = 0; i < array.length(); i++) items.add(Reminder.fromJson(array.getJSONObject(i)));
        } catch (Exception ignored) { }
        return items;
    }
    public static void save(Context context, List<Reminder> items) {
        JSONArray array = new JSONArray();
        try { for (Reminder item : items) array.put(item.toJson()); } catch (Exception ignored) { }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, array.toString()).apply();
    }
    public static void ensureStarterReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        List<Reminder> items = load(context);
        if (!prefs.getBoolean(SEEDED, false)) {
            items.add(new Reminder(1001, "Winter arc: show up today. Your future is built by this hour.", 6, 0, true));
            items.add(new Reminder(1002, "Veer bhogya vasundhara — courage earns the world.", 20, 0, true));
            prefs.edit().putBoolean(SEEDED, true).apply();
        }
        boolean hasQuote = false;
        for (Reminder item : items) {
            if (item.id == 1003) { item.text = QuoteLibrary.DAILY_QUOTE; hasQuote = true; }
        }
        if (!hasQuote) items.add(new Reminder(1003, QuoteLibrary.DAILY_QUOTE, 7, 0, true));
        save(context, items);
    }
    public static String goalDate(Context context) { return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString("goal_date", "2030-01-01"); }
    public static void goalDate(Context context, String date) { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString("goal_date", date).apply(); }
}
