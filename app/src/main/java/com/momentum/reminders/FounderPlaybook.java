package com.momentum.reminders;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Small, offline founder curriculum: practical teaching rather than attributed quotations. */
public final class FounderPlaybook {
    public static final class Lesson {
        public final String category, teaching;
        Lesson(String category, String teaching) { this.category = category; this.teaching = teaching; }
    }
    private static final Lesson[] LESSONS = {
            new Lesson("SALES", "You are not selling a product; you are selling the change it enables. Ask what becomes easier, safer, faster, richer, or more meaningful after the customer buys."),
            new Lesson("CUSTOMER", "Start with one painful problem for one specific customer. A narrow promise that solves a real pain beats a broad promise that means nothing."),
            new Lesson("PRODUCT", "Build the smallest version that proves the customer wants the outcome. Features are costs until a customer uses them repeatedly."),
            new Lesson("VISION", "A vision gives direction; a weekly plan creates movement. Describe the future clearly, then reduce it to the next measurable test."),
            new Lesson("DISTRIBUTION", "A great product without a path to customers is a hidden product. Decide how people will discover, trust, try, and recommend you."),
            new Lesson("PRICING", "Price against the value created, not the effort you spent. If you save a client time, risk, or money, make the result visible."),
            new Lesson("CASH", "Revenue is applause; cash flow is oxygen. Know your runway, your gross margin, and the date when money enters and leaves."),
            new Lesson("FOCUS", "Every new idea carries an opportunity cost. Protect the one problem your company must solve before adding the next ambition."),
            new Lesson("EXECUTION", "Turn plans into owners, dates, and evidence. If a task has no owner or deadline, it is a wish, not an operating decision."),
            new Lesson("NEGOTIATION", "Prepare before you persuade. Know the other side’s incentives, alternatives, constraints, and the outcome that creates value for both sides."),
            new Lesson("BRAND", "A brand is a repeated promise kept. Make the experience after purchase as intentional as the message before purchase."),
            new Lesson("LEADERSHIP", "Hire for values, judgment, and learning speed. Give people context and standards, then let capable people own meaningful work."),
            new Lesson("LEARNING", "Talk to customers before trusting your assumptions. Ten honest conversations can prevent months of building the wrong thing."),
            new Lesson("METRICS", "Choose one leading signal that shows whether customers receive value. Track it weekly and let the number challenge your story."),
            new Lesson("RESILIENCE", "Separate the result from the lesson. A rejected pitch, lost client, or bad launch becomes useful only after you identify what to test next."),
            new Lesson("SYSTEMS", "Do not become the bottleneck in the company you want to scale. Document repeatable work, automate the obvious, and teach the standard."),
            new Lesson("STORYTELLING", "Customers remember a clear before-and-after. Explain who they are today, what is holding them back, and the better result you enable."),
            new Lesson("ETHICS", "Shortcuts create hidden liabilities. Build trust through clear terms, honest claims, and products that genuinely help the customer."),
            new Lesson("MARKET", "Follow evidence, not excitement. A growing market, urgent pain, and a reachable buyer matter more than a fashionable idea."),
            new Lesson("FOUNDER MINDSET", "Your job is to turn uncertainty into learning. Make a small bet, measure the response, update your belief, and act again.")
    };
    private FounderPlaybook() { }
    public static Lesson forToday(Context context) {
        int offset = context.getSharedPreferences("momentum_prefs", Context.MODE_PRIVATE).getInt("playbook_offset", 0);
        String day = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        return LESSONS[((day + offset).hashCode() & 0x7fffffff) % LESSONS.length];
    }
    public static Lesson next(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences("momentum_prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("playbook_offset", prefs.getInt("playbook_offset", 0) + 1).apply();
        return forToday(context);
    }
}
