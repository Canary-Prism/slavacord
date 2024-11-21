package canaryprism.slavacord.util;

import java.util.regex.Pattern;

public final class Patterns {
    /**
     * no touchy
     */
    private Patterns() {}

    public static final Pattern WHITESPACE = Pattern.compile("\\s");

    public static final Pattern PUNCTUATION = Pattern.compile("[,./?!_\\-+:`]");
}
