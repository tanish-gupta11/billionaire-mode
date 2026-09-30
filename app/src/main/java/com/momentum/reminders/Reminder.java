package com.momentum.reminders;

import org.json.JSONException;
import org.json.JSONObject;

public class Reminder {
    public int id;
    public String text;
    public int hour;
    public int minute;
    public boolean enabled;

    public Reminder(int id, String text, int hour, int minute, boolean enabled) {
        this.id = id; this.text = text; this.hour = hour; this.minute = minute; this.enabled = enabled;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", id); object.put("text", text); object.put("hour", hour);
        object.put("minute", minute); object.put("enabled", enabled);
        return object;
    }

    public static Reminder fromJson(JSONObject object) throws JSONException {
        return new Reminder(object.getInt("id"), object.getString("text"), object.getInt("hour"),
                object.getInt("minute"), object.optBoolean("enabled", true));
    }
}
