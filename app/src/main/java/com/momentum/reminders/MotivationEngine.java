package com.momentum.reminders;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Lightweight offline prompt engine: 20 × 20 × 20 × 20 = 160,000 original combinations. */
public final class MotivationEngine {
    public static final String DAILY_ARSENAL = "__DAILY_ARSENAL__";
    private static final String[] FOCUS = {
            "Your edge is the work nobody can see", "A clear mind outperforms a loud one", "Courage begins before confidence arrives", "Your calendar reveals your real ambition", "Pressure is a signal to become more precise",
            "Great businesses start with useful decisions", "A disciplined body teaches a disciplined mind", "Your attention is your first capital", "Patience is an advantage when others rush", "The hard conversation is often the profitable one",
            "A setback is data when you study it", "Skill is built in repetitions, not declarations", "Your standards choose your future", "Long horizons make small moves powerful", "The market rewards preparation more than excitement",
            "Resilience is a habit you can train", "The person you become is built by ordinary mornings", "Focus turns limited hours into uncommon output", "Ownership is the beginning of freedom", "Vision becomes real only through execution"
    };
    private static final String[] ACTION = {
            "Use this hour to improve one asset", "Finish the task you have been avoiding", "Make one decision your future self will respect", "Study before you risk capital", "Practice the skill that raises your value",
            "Build something useful before seeking applause", "Turn your plan into the next measurable action", "Choose depth over distraction", "Keep a promise when nobody checks", "Ask the question that reveals the real problem",
            "Protect your energy for meaningful work", "Do the difficult rep before the easy comfort", "Write down the next three moves", "Learn the numbers behind the opportunity", "Make your health support your ambition",
            "Replace urgency with preparation", "Invest your time where it can compound", "Take responsibility for the outcome", "Make the call, send the proposal, start the work", "Leave the day stronger than you entered it"
    };
    private static final String[] STANDARD = {
            "Discipline compounds when it is kept private", "Consistency makes motivation optional", "Integrity is doing the work after the feeling fades", "A strong life is built on repeated small wins", "Preparation creates calm under pressure",
            "Your values become visible through your habits", "Real confidence is earned evidence", "The best strategy still needs action", "You do not need permission to improve", "A focused day is a vote for a focused life",
            "Learning protects ambition from ego", "The long game favors people who keep showing up", "Meaningful wealth follows meaningful value", "Progress respects people who measure it", "Strength is quiet before it is visible",
            "A promise to yourself is a contract", "Your future needs your effort more than your excuses", "Compounding starts with what you repeat", "Every standard you hold shapes your identity", "Clarity removes the need for drama"
    };
    private static final String[] OUTCOME = {
            "Momentum turns deliberate days into uncommon years", "The result is a life you can trust yourself to build", "That is how a vision gains weight", "That is how you become difficult to stop", "The next level is built before anyone notices",
            "Your future capital is created in these choices", "That is the difference between wishing and building", "Your discipline becomes your reputation", "The work will eventually speak for you", "Small proof becomes powerful self-belief",
            "That is how preparation becomes opportunity", "Every honest rep expands your capacity", "Your business instincts sharpen through practice", "You earn calm by becoming capable", "The person you want to be is waiting in the next action",
            "This is how big goals become practical", "The world changes when capable people follow through", "A stronger identity is built one choice at a time", "Your standard becomes your advantage", "Build today so tomorrow has more options"
    };

    private MotivationEngine() { }

    public static String forToday(Context context) {
        int offset = context.getSharedPreferences("momentum_prefs", Context.MODE_PRIVATE).getInt("arsenal_offset", 0);
        return thoughtFor(today() + ":" + offset);
    }

    public static String next(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences("momentum_prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("arsenal_offset", prefs.getInt("arsenal_offset", 0) + 1).apply();
        return forToday(context);
    }

    private static String thoughtFor(String seed) {
        long value = seed.hashCode() & 0x7fffffffL;
        int a = (int)(value % 20); value /= 20;
        int b = (int)(value % 20); value /= 20;
        int c = (int)(value % 20); value /= 20;
        int d = (int)(value % 20);
        return FOCUS[a] + ". " + ACTION[b] + ". " + STANDARD[c] + ". " + OUTCOME[d] + ".";
    }

    private static String today() { return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()); }
}
