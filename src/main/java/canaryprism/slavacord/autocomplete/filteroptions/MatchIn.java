package canaryprism.slavacord.autocomplete.filteroptions;

/**
 * enum specifying what part of a suggestion must match with the user input
 */
public enum MatchIn {

    /**
     * <p>the <strong>name</strong> of a suggestion must match with user input</p>
     *
     * <p>keep in mind that for {@code long} options only long values will be sent to the bot with failed coercions resulting in {@code 0}, the bot cannot recover the user's original input string and so searching suggestion names with non-number strings is impossible</p>
     */
    NAME,

    /**
     * <p>the <strong>value</strong> of a suggestion must match with user input</p>
     *
     * <p>
     * keep in mind that the value of a suggestion is only presented to the bot and is never shown to the user.
     * it can be confusing with string suggestions if the name shown does not resemble the value that got matched
     * </p>
     *
     * <p>
     * it is also recommended you <strong>NEVER</strong> give String suggestions with different name and value strings (such as a human-readable string for the name and an internal identifier for the value)
     * because suggestions aren't forced (they're suggestions) and the user is allowed to enter whatever string they like
     * which could coincide with the name or value of some of your suggestions
     * </p>
     */
    VALUE,

    /**
     * <p>BOTH the <strong>name and value</strong> are used to match the user's input, and either one matching will cause the suggestion to be kept</p>
     *
     * <p>this tries to strike a balance that's good enough for a default value when matching either Strings or longs</p>
     */
    BOTH
}
