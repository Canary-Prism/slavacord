package canaryprism.slavacord.autocomplete;

import java.util.Map;
import java.util.Objects;

import org.javacord.api.interaction.DiscordLocale;

/**
 * A suggestion for an autocomplete command.
 */
public record AutocompleteSuggestion<T>(String name, T value, Map<DiscordLocale, String> localizations) {
    /**
     * Constructs a new autocomplete suggestion. none of the parameters may be null.
     * @param name the name of the suggestion shown to the user
     * @param value the value of the suggestion
     * @param localizations the localizations of the suggestion
     */
    public AutocompleteSuggestion {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(localizations, "localizations must not be null");
    }

    /**
     * Constructs a new autocomplete suggestion with no localizations. none of the parameters may be null.
     * @param name the name of the suggestion shown to the user
     * @param value the value of the suggestion
     */
    public AutocompleteSuggestion(String name, T value) {
        this(name, value, Map.of());
    }

    /**
     * Creates a new autocomplete suggestion with no localizations. none of the parameters may be null.
     * 
     * @param <T> the type of the value
     * @param name the name of the suggestion shown to the user
     * @param value the value of the suggestion
     */
    public static <T> AutocompleteSuggestion<T> of(String name, T value) {
        return new AutocompleteSuggestion<>(name, value);
    }

    /**
     * Creates a new String autocomplete suggestion with no localizations. none of the parameters may be null. this is a convenience method for {@link #of(String, Object) of(String, String)}.
     * @param name_and_value the name and value of the suggestion
     */
    public static AutocompleteSuggestion<String> of(String name_and_value) {
        return of(name_and_value, name_and_value);
    }
}
