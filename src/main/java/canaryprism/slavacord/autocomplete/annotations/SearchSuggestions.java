package canaryprism.slavacord.autocomplete.annotations;

import canaryprism.slavacord.autocomplete.filteroptions.MatchIn;
import canaryprism.slavacord.autocomplete.filteroptions.MatchStart;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>when used on an Autocompleter method this makes CommandHandler filter and the returned suggestions using the user's input</p>
 * <p>normally it is the Autocompleter method's responsibility to ensure the returned AutocompleteSuggestions are tailored for the given user input, but if this annotation is applied the CommandHandler attempts to do some filtering itself as specified by the annotation</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SearchSuggestions {

    /**
     * whether the user input has to match the beggining of the suggestion or anywhere
     * @return MatchStart enum indicating where in a suggestion must the user input start matching
     */
    MatchStart at() default MatchStart.BEGGINING;

    /**
     * whether the user input can be present in a suggestion's name or value or both
     * @return MatchIn enum indicating whether the user input can be present in a suggestion's name or value or both
     */
    MatchIn containedIn() default MatchIn.BOTH;

    /**
     * whether the user input is matched ignoring case
     * @return whether the user input is matched ignoring case
     */
    boolean ignoreCase() default true;

    /**
     * whether the user input is matched ignoring any regex whitespace ({@code \s}) characters
     * @return whether the user input is matched ignoring whitespace
     */
    boolean ignoreWhitespace() default true;

    /**
     * whether the user input is matched ignoring punctuation characters defined in {@link canaryprism.slavacord.util.Patterns#PUNCTUATION}
     * @return whether the user input is matched ignoring punctuation
     */
    boolean ignorePunctuation() default false;

    /**
     * whether the returned suggestions list is automatically pruned to no more than 25 items to fit within discord's limit
     * @return whether the returned suggestions list is pruned
     */
    boolean pruneToLimit() default true;

    /**
     * whether the returned suggestions list automatically gets duplicates removed.
     * suggestions are only removed as duplicates if their name AND value both match
     * @return whether the returned suggestions list gets duplicates removed
     */
    boolean removeDuplicates() default true;

    /**
     * <p>whether the user input has to be a <i>contiguous</i> substring of a suggestion</p>
     *
     * <p>if false it means that only every character in the input has to be in a suggestion</p>
     *
     * <p>for example, if false and a suggestion item is "macarons", a user input of "mars" will still count</p>
     *
     * @return whether the match has to be contiguous
     */
    boolean contiguous() default true;
}
