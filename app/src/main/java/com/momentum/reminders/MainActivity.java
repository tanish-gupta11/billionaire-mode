package com.momentum.reminders;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int GOLD = Color.rgb(255, 184, 77);
    private static final int INK = Color.rgb(11, 16, 32);
    private static final int SURFACE = Color.rgb(18, 26, 49);
    private LinearLayout reminderList;
    private TextView countdown;
    private TextView goalDate;
    private TextView streak;
    private Button promiseButton;
    private LinearLayout targetList;
    private TextView targetStatus;
    private TextView arsenalThought;
    private TextView quoteText;
    private TextView quoteAttribution;
    private TextView strategyText;
    private TextView strategyAttribution;
    private TextView playbookCategory;
    private TextView playbookTeaching;
    private TextView habitSummary;
    private TextView businessSummary;
    private TextView weeklySummary;
    private List<Reminder> reminders = new ArrayList<>();

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        ReminderStore.ensureStarterReminders(this);
        ReminderScheduler.scheduleAll(this);
        requestNotificationPermission();
        requestExactAlarmPermission();
        buildUi();
        refresh();
    }

    @Override public void onResume() { super.onResume(); if (reminderList != null) refresh(); }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int sidePadding = getResources().getConfiguration().screenWidthDp >= 600 ? dp(56) : dp(20);
        root.setPadding(sidePadding, dp(24), sidePadding, dp(28));
        root.setBackgroundColor(INK);
        scroll.addView(root);

        TextView eyebrow = label("YOUR DAILY COMPASS", 12, GOLD);
        root.addView(eyebrow);
        TextView title = label("Billionaire Mode", getResources().getConfiguration().screenWidthDp >= 600 ? 38 : 34, Color.WHITE);
        title.setPadding(0, dp(4), 0, 0); root.addView(title);
        TextView subtitle = label("Remember who you are. Then act on it.", 16, Color.rgb(170,180,206));
        subtitle.setPadding(0, 0, 0, dp(22)); root.addView(subtitle);

        CollapsibleSection arsenal = collapsible("DAILY ARSENAL", "160K original prompts");
        arsenalThought = label("", 16, Color.WHITE); arsenalThought.setLineSpacing(dp(3), 1f); arsenalThought.setPadding(0, dp(12), 0, dp(12)); arsenal.body.addView(arsenalThought);
        Button nextThought = button("New perspective", false); nextThought.setOnClickListener(v -> { arsenalThought.setText(MotivationEngine.next(this)); }); arsenal.body.addView(nextThought);

        CollapsibleSection quote = collapsible("VERIFIED VOICE", "Attributed historical quote");
        quoteText = label("", 17, Color.WHITE); quoteText.setPadding(0, dp(12), 0, dp(8)); quote.body.addView(quoteText);
        quoteAttribution = label("", 13, Color.rgb(170,180,206)); quote.body.addView(quoteAttribution);

        CollapsibleSection strategy = collapsible("STRATEGY NOTE", "Chanakya, leadership and execution");
        strategyText = label("", 16, Color.WHITE); strategyText.setPadding(0, dp(12), 0, dp(8)); strategy.body.addView(strategyText);
        strategyAttribution = label("", 13, Color.rgb(170,180,206)); strategy.body.addView(strategyAttribution);

        CollapsibleSection playbook = collapsible("FOUNDER’S PLAYBOOK", "Sales, product and business lessons");
        playbookCategory = label("", 13, Color.rgb(170,180,206)); playbookCategory.setPadding(0, dp(12), 0, dp(4)); playbook.body.addView(playbookCategory);
        playbookTeaching = label("", 16, Color.WHITE); playbookTeaching.setLineSpacing(dp(3), 1f); playbook.body.addView(playbookTeaching);
        Button nextLesson = button("Next business lesson", false); nextLesson.setPadding(dp(12), dp(6), dp(12), dp(6)); nextLesson.setOnClickListener(v -> showPlaybookLesson(FounderPlaybook.next(this))); playbook.body.addView(nextLesson);

        LinearLayout goalCard = card();
        TextView goalLabel = label("TIME LEFT TO BUILD YOUR EMPIRE", 11, GOLD);
        goalCard.addView(goalLabel);
        countdown = label("", 30, Color.WHITE); countdown.setPadding(0, dp(5), 0, 0); goalCard.addView(countdown);
        goalDate = label("", 14, Color.rgb(170,180,206)); goalDate.setPadding(0, dp(4), 0, dp(10)); goalCard.addView(goalDate);
        Button changeGoal = button("Set target date", false);
        changeGoal.setOnClickListener(v -> chooseGoalDate()); goalCard.addView(changeGoal);
        root.addView(goalCard, matchWidth());

        LinearLayout focusCard = card();
        focusCard.setPadding(dp(16), dp(16), dp(16), dp(14));
        TextView focusLabel = label("DAILY EXECUTION", 11, GOLD); focusCard.addView(focusLabel);
        LinearLayout focusRow = new LinearLayout(this); focusRow.setGravity(Gravity.CENTER_VERTICAL); focusRow.setPadding(0, dp(5), 0, dp(10));
        streak = label("0", 30, Color.WHITE); focusRow.addView(streak);
        TextView streakCaption = label("  day promise streak\nKeep a promise. Build self-trust.", 14, Color.rgb(170,180,206)); focusRow.addView(streakCaption);
        focusCard.addView(focusRow);
        habitSummary = label("", 13, Color.rgb(170,180,206)); habitSummary.setPadding(0, 0, 0, dp(10)); focusCard.addView(habitSummary);
        promiseButton = button("I kept my promise today", true); promiseButton.setOnClickListener(v -> markPromiseKept()); focusCard.addView(promiseButton);
        root.addView(focusCard, spacedCard());

        LinearLayout targetsCard = card();
        LinearLayout targetsHead = new LinearLayout(this); targetsHead.setGravity(Gravity.CENTER_VERTICAL);
        TextView targetsTitle = label("TODAY'S TOP 3", 11, GOLD); targetsHead.addView(targetsTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button editTargets = button("Plan day", false); editTargets.setOnClickListener(v -> editTargets()); targetsHead.addView(editTargets);
        targetsCard.addView(targetsHead);
        targetStatus = label("", 14, Color.rgb(170,180,206)); targetStatus.setPadding(0, dp(8), 0, dp(5)); targetsCard.addView(targetStatus);
        targetList = new LinearLayout(this); targetList.setOrientation(LinearLayout.VERTICAL); targetsCard.addView(targetList);
        root.addView(targetsCard, spacedCard());

        LinearLayout businessCard = card();
        LinearLayout businessHead = new LinearLayout(this); businessHead.setGravity(Gravity.CENTER_VERTICAL);
        TextView businessTitle = label("BUSINESS DASHBOARD", 11, GOLD); businessHead.addView(businessTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button updateBusiness = button("Update", false); updateBusiness.setOnClickListener(v -> editBusinessDashboard()); businessHead.addView(updateBusiness);
        businessCard.addView(businessHead);
        businessSummary = label("", 15, Color.WHITE); businessSummary.setLineSpacing(dp(3), 1f); businessSummary.setPadding(0, dp(10), 0, 0); businessCard.addView(businessSummary);
        root.addView(businessCard, spacedCard());

        LinearLayout reviewCard = card();
        LinearLayout reviewHead = new LinearLayout(this); reviewHead.setGravity(Gravity.CENTER_VERTICAL);
        TextView reviewTitle = label("WEEKLY REVIEW", 11, GOLD); reviewHead.addView(reviewTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button writeReview = button("Write", false); writeReview.setOnClickListener(v -> editWeeklyReview()); reviewHead.addView(writeReview);
        reviewCard.addView(reviewHead);
        weeklySummary = label("", 14, Color.rgb(170,180,206)); weeklySummary.setPadding(0, dp(9), 0, 0); reviewCard.addView(weeklySummary);
        root.addView(reviewCard, spacedCard());

        TextView libraryHeading = label("MINDSET LIBRARY", 14, Color.WHITE); libraryHeading.setPadding(0, dp(28), 0, dp(10)); root.addView(libraryHeading);
        root.addView(arsenal.card, matchWidth());
        root.addView(quote.card, spacedCard());
        root.addView(strategy.card, spacedCard());
        root.addView(playbook.card, spacedCard());

        LinearLayout heading = new LinearLayout(this); heading.setGravity(Gravity.CENTER_VERTICAL); heading.setPadding(0, dp(26), 0, dp(10));
        TextView remindersTitle = label("YOUR REMINDERS", 14, Color.WHITE);
        heading.addView(remindersTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button add = button("+ Add", true); add.setOnClickListener(v -> editReminder(null)); heading.addView(add);
        root.addView(heading);
        reminderList = new LinearLayout(this); reminderList.setOrientation(LinearLayout.VERTICAL); root.addView(reminderList);

        TextView footer = label("Small actions, repeated without excuse, become your life.", 13, Color.rgb(170,180,206));
        footer.setGravity(Gravity.CENTER); footer.setPadding(dp(12), dp(28), dp(12), 0); root.addView(footer);
        setContentView(scroll);
    }

    private void refresh() {
        reminders = ReminderStore.load(this);
        updateCountdown();
        if (arsenalThought != null) arsenalThought.setText(MotivationEngine.forToday(this));
        if (quoteText != null) {
            QuoteLibrary.Quote quote = QuoteLibrary.forToday();
            quoteText.setText("“" + quote.words + "”");
            quoteAttribution.setText("— " + quote.author + " • " + quote.source);
        }
        if (strategyText != null) {
            StrategyLibrary.Note note = StrategyLibrary.forToday();
            strategyText.setText(note.text);
            strategyAttribution.setText(note.lens + " • original interpretation, not a quotation");
        }
        if (playbookTeaching != null) showPlaybookLesson(FounderPlaybook.forToday(this));
        updateDailyFocus();
        updateTargets();
        updateBusinessDashboard();
        updateWeeklyReview();
        reminderList.removeAllViews();
        if (reminders.isEmpty()) {
            TextView empty = label("No reminders yet. Add the message you need to hear every day.", 15, Color.rgb(170,180,206));
            empty.setPadding(0, dp(14), 0, dp(14)); reminderList.addView(empty);
        }
        for (Reminder reminder : reminders) addReminderCard(reminder);
    }

    private void addReminderCard(Reminder r) {
        LinearLayout card = card(); card.setPadding(dp(16), dp(14), dp(12), dp(14));
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout words = new LinearLayout(this); words.setOrientation(LinearLayout.VERTICAL);
        String visibleText = QuoteLibrary.DAILY_QUOTE.equals(r.text) ? "Daily Quote — verified classic" : r.text;
        TextView text = label(visibleText, 16, Color.WHITE); words.addView(text);
        TextView time = label(formatTime(r.hour, r.minute) + "  •  Every day", 13, Color.rgb(170,180,206)); time.setPadding(0, dp(7), 0, 0); words.addView(time);
        row.addView(words, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Switch enabled = new Switch(this); enabled.setChecked(r.enabled); enabled.setContentDescription("Enable reminder");
        enabled.setOnCheckedChangeListener((b, on) -> { r.enabled = on; saveAndSchedule(); }); row.addView(enabled);
        card.addView(row);
        LinearLayout actions = new LinearLayout(this); actions.setGravity(Gravity.RIGHT); actions.setPadding(0, dp(8), 0, 0);
        Button edit = button("Edit", false); edit.setOnClickListener(v -> editReminder(r)); actions.addView(edit);
        Button delete = button("Remove", false); delete.setTextColor(Color.rgb(255,142,142)); delete.setOnClickListener(v -> { reminders.remove(r); ReminderScheduler.cancel(this, r.id); saveAndSchedule(); }); actions.addView(delete);
        card.addView(actions); reminderList.addView(card, spacedCard());
    }

    private void editReminder(Reminder existing) {
        LinearLayout form = new LinearLayout(this); form.setOrientation(LinearLayout.VERTICAL); int p = dp(22); form.setPadding(p, p, p, 0);
        EditText message = new EditText(this); message.setHint("Your message to future you"); message.setTextColor(Color.WHITE); message.setHintTextColor(Color.GRAY); message.setText(existing == null ? "" : existing.text); message.setMinLines(3); form.addView(message);
        TextView selected = label(existing == null ? "Reminder time: 08:00" : "Reminder time: " + formatTime(existing.hour, existing.minute), 15, GOLD);
        selected.setPadding(0, dp(16), 0, dp(12)); form.addView(selected);
        final int[] clock = {existing == null ? 8 : existing.hour, existing == null ? 0 : existing.minute};
        Button timeButton = button("Choose time", false); timeButton.setOnClickListener(v -> new TimePickerDialog(this, (view, h, m) -> { clock[0]=h; clock[1]=m; selected.setText("Reminder time: " + formatTime(h,m)); }, clock[0], clock[1], false).show()); form.addView(timeButton);
        new android.app.AlertDialog.Builder(this).setTitle(existing == null ? "New reminder" : "Edit reminder").setView(form)
                .setNegativeButton("Cancel", null).setPositiveButton("Save", (d, w) -> {
                    String value = message.getText().toString().trim(); if (value.isEmpty()) { Toast.makeText(this, "Write a reminder first", Toast.LENGTH_SHORT).show(); return; }
                    if (existing == null) reminders.add(new Reminder(nextId(), value, clock[0], clock[1], true));
                    else { existing.text=value; existing.hour=clock[0]; existing.minute=clock[1]; }
                    saveAndSchedule();
                }).show();
    }

    private void chooseGoalDate() {
        Calendar c = Calendar.getInstance();
        try { c.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(ReminderStore.goalDate(this))); } catch (Exception ignored) { }
        new DatePickerDialog(this, (v,y,m,d) -> { String value=String.format(Locale.US, "%04d-%02d-%02d",y,m+1,d); ReminderStore.goalDate(this,value); updateCountdown(); }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateCountdown() {
        try {
            Date target = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(ReminderStore.goalDate(this));
            long days = Math.max(0, (target.getTime() - System.currentTimeMillis()) / 86400000L);
            countdown.setText(days + " days remaining"); goalDate.setText("Target date: " + ReminderStore.goalDate(this));
        } catch (Exception e) { countdown.setText("Set your target"); }
    }

    private void updateDailyFocus() {
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        int count = prefs.getInt("promise_streak", 0);
        boolean done = today().equals(prefs.getString("promise_last_day", ""));
        if (done && !prefs.getBoolean("promise_done_" + today(), false)) {
            prefs.edit().putBoolean("promise_done_" + today(), true).apply();
        }
        streak.setText(count + " day" + (count == 1 ? "" : "s"));
        promiseButton.setText(done ? "Promise kept — come back tomorrow" : "I kept my promise today");
        promiseButton.setEnabled(!done);
        promiseButton.setAlpha(done ? .62f : 1f);
        if (habitSummary != null) habitSummary.setText("This week: " + completedDaysThisWeek(prefs) + " of 7 promises kept");
    }

    private void markPromiseKept() {
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        String now = today();
        if (now.equals(prefs.getString("promise_last_day", ""))) return;
        Calendar yesterday = Calendar.getInstance(); yesterday.add(Calendar.DAY_OF_YEAR, -1);
        String previous = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(yesterday.getTime());
        int next = previous.equals(prefs.getString("promise_last_day", "")) ? prefs.getInt("promise_streak", 0) + 1 : 1;
        prefs.edit().putString("promise_last_day", now).putInt("promise_streak", next).apply();
        prefs.edit().putBoolean("promise_done_" + now, true).apply();
        Toast.makeText(this, "Promise recorded. Keep moving.", Toast.LENGTH_SHORT).show();
        updateDailyFocus();
    }

    private String today() { return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()); }

    private int completedDaysThisWeek(android.content.SharedPreferences prefs) {
        Calendar day = Calendar.getInstance();
        int count = 0;
        for (int i = 0; i < 7; i++) {
            String key = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(day.getTime());
            if (prefs.getBoolean("promise_done_" + key, false)) count++;
            day.add(Calendar.DAY_OF_YEAR, -1);
        }
        return count;
    }

    private void updateBusinessDashboard() {
        if (businessSummary == null) return;
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        String revenue = prefs.getString("business_revenue", "Not set");
        String leads = prefs.getString("business_leads", "0");
        String calls = prefs.getString("business_calls", "0");
        businessSummary.setText("Revenue: " + revenue + "\nLeads: " + leads + "   •   Sales calls: " + calls);
    }

    private void editBusinessDashboard() {
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        LinearLayout form = new LinearLayout(this); form.setOrientation(LinearLayout.VERTICAL); int p = dp(22); form.setPadding(p, p, p, 0);
        EditText revenue = metricInput("Revenue this month (for example ₹50,000)", prefs.getString("business_revenue", "")); form.addView(revenue);
        EditText leads = metricInput("Qualified leads this week", prefs.getString("business_leads", "")); form.addView(leads);
        EditText calls = metricInput("Sales calls this week", prefs.getString("business_calls", "")); form.addView(calls);
        new android.app.AlertDialog.Builder(this).setTitle("Business dashboard").setMessage("Track operating signals, not investment advice.").setView(form)
                .setNegativeButton("Cancel", null).setPositiveButton("Save", (d, w) -> {
                    prefs.edit().putString("business_revenue", emptyAs(revenue, "Not set")).putString("business_leads", emptyAs(leads, "0")).putString("business_calls", emptyAs(calls, "0")).apply();
                    updateBusinessDashboard();
                }).show();
    }

    private void updateWeeklyReview() {
        if (weeklySummary == null) return;
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        String key = reviewWeek();
        String win = prefs.getString("review_win_" + key, "");
        String lesson = prefs.getString("review_lesson_" + key, "");
        weeklySummary.setText(win.isEmpty() && lesson.isEmpty() ? "Capture your win and the lesson that improves next week." : "Win: " + win + "\nNext lesson: " + lesson);
    }

    private void editWeeklyReview() {
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE); String key = reviewWeek();
        LinearLayout form = new LinearLayout(this); form.setOrientation(LinearLayout.VERTICAL); int p = dp(22); form.setPadding(p, p, p, 0);
        EditText win = metricInput("What was your biggest win?", prefs.getString("review_win_" + key, "")); win.setMinLines(2); form.addView(win);
        EditText lesson = metricInput("What will you improve next week?", prefs.getString("review_lesson_" + key, "")); lesson.setMinLines(2); form.addView(lesson);
        new android.app.AlertDialog.Builder(this).setTitle("Weekly review").setMessage("Turn effort into learning.").setView(form)
                .setNegativeButton("Cancel", null).setPositiveButton("Save review", (d, w) -> { prefs.edit().putString("review_win_" + key, win.getText().toString().trim()).putString("review_lesson_" + key, lesson.getText().toString().trim()).apply(); updateWeeklyReview(); }).show();
    }

    private EditText metricInput(String hint, String value) { EditText input = new EditText(this); input.setHint(hint); input.setText(value); input.setTextColor(Color.WHITE); input.setHintTextColor(Color.GRAY); input.setContentDescription(hint); return input; }
    private String emptyAs(EditText input, String fallback) { String value = input.getText().toString().trim(); return value.isEmpty() ? fallback : value; }
    private String reviewWeek() { return new SimpleDateFormat("YYYY-'W'ww", Locale.US).format(new Date()); }

    private void showPlaybookLesson(FounderPlaybook.Lesson lesson) {
        playbookCategory.setText(lesson.category);
        playbookTeaching.setText(lesson.teaching);
    }

    private static final class CollapsibleSection {
        final LinearLayout card;
        final LinearLayout body;
        final Button toggle;
        CollapsibleSection(LinearLayout card, LinearLayout body, Button toggle) { this.card = card; this.body = body; this.toggle = toggle; }
    }

    private CollapsibleSection collapsible(String title, String summary) {
        LinearLayout sectionCard = card(); sectionCard.setPadding(dp(16), dp(13), dp(12), dp(13));
        LinearLayout header = new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout labels = new LinearLayout(this); labels.setOrientation(LinearLayout.VERTICAL);
        labels.addView(label(title, 11, GOLD));
        TextView detail = label(summary, 13, Color.rgb(170,180,206)); detail.setPadding(0, dp(3), 0, 0); labels.addView(detail);
        header.addView(labels, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        Button toggle = button("Open", false); header.addView(toggle);
        sectionCard.addView(header);
        LinearLayout body = new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL); body.setVisibility(View.GONE); sectionCard.addView(body);
        toggle.setOnClickListener(v -> {
            boolean opening = body.getVisibility() != View.VISIBLE;
            body.setVisibility(opening ? View.VISIBLE : View.GONE);
            toggle.setText(opening ? "Close" : "Open");
        });
        return new CollapsibleSection(sectionCard, body, toggle);
    }

    private void updateTargets() {
        if (targetList == null) return;
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        targetList.removeAllViews();
        if (!today().equals(prefs.getString("targets_day", ""))) {
            targetStatus.setText("Plan the three actions that move your life forward.");
            return;
        }
        int total = 0, complete = 0;
        for (int i = 1; i <= 3; i++) {
            String task = prefs.getString("target_" + i, "").trim();
            if (task.isEmpty()) continue;
            total++;
            boolean done = prefs.getBoolean("target_done_" + i, false);
            if (done) complete++;
            CheckBox box = new CheckBox(this); box.setText(task); box.setTextSize(15); box.setTextColor(Color.WHITE); box.setChecked(done); box.setPadding(0, dp(2), 0, dp(2));
            final int targetId = i; box.setOnCheckedChangeListener((button, checked) -> { prefs.edit().putBoolean("target_done_" + targetId, checked).apply(); updateTargets(); });
            targetList.addView(box);
        }
        targetStatus.setText(total == 0 ? "Plan the three actions that move your life forward." : complete + " of " + total + " targets complete today");
    }

    private void editTargets() {
        android.content.SharedPreferences prefs = getSharedPreferences("momentum_prefs", MODE_PRIVATE);
        LinearLayout form = new LinearLayout(this); form.setOrientation(LinearLayout.VERTICAL); int p = dp(22); form.setPadding(p, p, p, 0);
        EditText[] inputs = new EditText[3];
        for (int i = 0; i < 3; i++) {
            EditText input = new EditText(this); input.setHint("Target " + (i + 1)); input.setTextColor(Color.WHITE); input.setHintTextColor(Color.GRAY);
            input.setText(today().equals(prefs.getString("targets_day", "")) ? prefs.getString("target_" + (i + 1), "") : "");
            form.addView(input); inputs[i] = input;
        }
        new android.app.AlertDialog.Builder(this).setTitle("Plan your top 3").setMessage("Choose actions that make today count.").setView(form)
                .setNegativeButton("Cancel", null).setPositiveButton("Save plan", (d, w) -> {
                    android.content.SharedPreferences.Editor editor = prefs.edit().putString("targets_day", today());
                    for (int i = 0; i < 3; i++) { editor.putString("target_" + (i + 1), inputs[i].getText().toString().trim()); editor.putBoolean("target_done_" + (i + 1), false); }
                    editor.apply(); updateTargets();
                }).show();
    }

    private void saveAndSchedule() { ReminderStore.save(this, reminders); ReminderScheduler.scheduleAll(this); refresh(); }
    private int nextId() { return (int)(System.currentTimeMillis() & 0x7fffffff); }
    private void requestNotificationPermission() { if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7); }
    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            boolean prompted = getSharedPreferences("momentum_prefs", MODE_PRIVATE).getBoolean("exact_alarm_prompted", false);
            if (!alarms.canScheduleExactAlarms() && !prompted) {
                getSharedPreferences("momentum_prefs", MODE_PRIVATE).edit().putBoolean("exact_alarm_prompted", true).apply();
                try { startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName()))); } catch (Exception ignored) { }
            }
        }
    }
    private String formatTime(int h, int m) { return String.format(Locale.US, "%d:%02d %s", h % 12 == 0 ? 12 : h % 12, m, h < 12 ? "AM" : "PM"); }
    private TextView label(String text, int size, int color) { TextView v = new TextView(this); v.setText(text); v.setTextSize(size); v.setTextColor(color); return v; }
    private Button button(String text, boolean filled) { Button b = new Button(this); b.setText(text); b.setContentDescription(text); b.setTextSize(13); b.setAllCaps(false); b.setTextColor(filled ? INK : GOLD); b.setBackground(round(filled ? GOLD : SURFACE, dp(10))); b.setMinHeight(dp(44)); b.setMinimumHeight(dp(44)); b.setPadding(dp(12), 0, dp(12), 0); return b; }
    private LinearLayout card() { LinearLayout c = new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(16), dp(16), dp(16), dp(16)); c.setBackground(round(SURFACE, dp(18))); return c; }
    private GradientDrawable round(int color, int radius) { GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius); return d; }
    private LinearLayout.LayoutParams matchWidth() { return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); }
    private LinearLayout.LayoutParams spacedCard() { LinearLayout.LayoutParams p = matchWidth(); p.topMargin=dp(8); return p; }
    private int dp(int value) { return (int)(value * getResources().getDisplayMetrics().density + .5f); }
}
