package com.momentum.reminders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Short, attributed quotations curated separately from the app's original Arsenal prompts. */
public final class QuoteLibrary {
    public static final String DAILY_QUOTE = "__DAILY_QUOTE__";
    public static final class Quote {
        public final String words, author, source;
        Quote(String words, String author, String source) { this.words = words; this.author = author; this.source = source; }
    }
    private static final Quote[] QUOTES = {
            new Quote("There is nothing impossible to him who will try.", "Alexander the Great", "Plutarch, Life of Alexander"),
            new Quote("First say to yourself what you would be; and then do what you have to do.", "Epictetus", "Discourses"),
            new Quote("If there is no struggle, there is no progress.", "Frederick Douglass", "West India Emancipation speech, 1857"),
            new Quote("Always bear in mind that your own resolution to succeed is more important than any other.", "Abraham Lincoln", "Letter to Isham Reavis, 1855"),
            new Quote("The present is theirs; the future, for which I really worked, is mine.", "Nikola Tesla", "My Inventions"),
            new Quote("Life is like riding a bicycle. To keep your balance, you must keep moving.", "Albert Einstein", "Letter to Eduard Einstein, 1930"),
            new Quote("Nothing in life is to be feared, it is only to be understood.", "Marie Curie", "Public lecture collection"),
            new Quote("Dream, dream, dream. Dreams transform into thoughts and thoughts result in action.", "A. P. J. Abdul Kalam", "Wings of Fire"),
            new Quote("Don’t count the days; make the days count.", "Muhammad Ali", "The Greatest: My Own Story"),
            new Quote("Stay hungry. Stay foolish.", "Steve Jobs", "Stanford commencement address, 2005; quoting Whole Earth Catalog"),
            new Quote("Take up one idea. Make that one idea your life.", "Swami Vivekananda", "Complete Works, Vol. 1"),
            new Quote("You cannot escape the responsibility of tomorrow by evading it today.", "Abraham Lincoln", "Annual message to Congress, 1862"),
            new Quote("The man who dies thus rich dies disgraced.", "Andrew Carnegie", "The Gospel of Wealth"),
            new Quote("Genius is one percent inspiration and ninety-nine percent perspiration.", "Thomas Edison", "Harper’s Monthly, 1932"),
            new Quote("Risk comes from not knowing what you’re doing.", "Warren Buffett", "Berkshire Hathaway annual meeting, 1993"),
            new Quote("Invert, always invert.", "Charlie Munger", "Poor Charlie’s Almanack"),
            new Quote("Never give in—never, never, never, never.", "Winston Churchill", "Harrow School speech, 1941"),
            new Quote("My life is my message.", "Mahatma Gandhi", "Interview, 1947"),
            new Quote("Courage was not the absence of fear, but the triumph over it.", "Nelson Mandela", "Long Walk to Freedom"),
            new Quote("Injustice anywhere is a threat to justice everywhere.", "Martin Luther King Jr.", "Letter from Birmingham Jail, 1963"),
            new Quote("Educate, agitate, organize.", "B. R. Ambedkar", "Movement slogan, attributed to Ambedkar"),
            new Quote("Give me blood, and I shall give you freedom.", "Subhas Chandra Bose", "Singapore address, 1944"),
            new Quote("Victorious warriors win first and then go to war.", "Sun Tzu", "The Art of War, Chapter 4"),
            new Quote("If I have seen further it is by standing on the shoulders of giants.", "Isaac Newton", "Letter to Robert Hooke, 1676"),
            new Quote("However difficult life may seem, there is always something you can do and succeed at.", "Stephen Hawking", "Speech, 2011"),
            new Quote("I’ve failed over and over and over again in my life. And that is why I succeed.", "Michael Jordan", "Nike campaign, 1997")
    };
    private QuoteLibrary() { }
    public static Quote forToday() {
        String day = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        return QUOTES[(day.hashCode() & 0x7fffffff) % QUOTES.length];
    }
}
