package com.momentum.reminders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Practical original interpretations, explicitly not quotations. */
public final class StrategyLibrary {
    public static final class Note {
        public final String text, lens;
        Note(String text, String lens) { this.text = text; this.lens = lens; }
    }
    private static final Note[] NOTES = {
            new Note("Before making a move, define the outcome, timing, resources, resistance, and cost of failure. Strategy starts before action.", "Chanakya Niti / Arthashastra — practical interpretation"),
            new Note("Know the terrain: your customer, your numbers, your competitors, and your own limits. Preparation turns courage into an advantage.", "Sun Tzu — practical interpretation"),
            new Note("Build trust before scale. A reputation for fair dealing becomes an asset that survives difficult cycles.", "Jamsetji Tata — business legacy lesson"),
            new Note("Let a long-term mission guide short-term decisions. Institutions outlive individual wins.", "Ratan Tata — leadership legacy lesson"),
            new Note("Treat capital as responsibility: learn the downside before you celebrate the upside.", "Value-investing practice"),
            new Note("A bold vision needs a patient operating system: study, test, measure, and improve.", "Builders’ practice"),
            new Note("Strength is most useful when it is governed by restraint, responsibility, and a clear purpose.", "Emperor Ashoka — leadership lesson"),
            new Note("Make your team capable enough to act well without you. Scale is built through people and systems.", "Shivaji Maharaj — leadership lesson"),
            new Note("Courage is not noise. It is the willingness to make the necessary decision with a clear head.", "Sam Manekshaw — leadership lesson"),
            new Note("Use learning as leverage: the person who keeps improving their judgment gains options others cannot see.", "Inventors’ practice")
    };
    private StrategyLibrary() { }
    public static Note forToday() {
        String day = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        return NOTES[(day.hashCode() & 0x7fffffff) % NOTES.length];
    }
}
