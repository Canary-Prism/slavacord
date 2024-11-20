package canaryprism.slavacord.autocomplete;

import java.util.Map;
import java.util.Objects;

import org.javacord.api.interaction.DiscordLocale;

public record AutocompleteSuggestion<T>(String name, T value, Map<DiscordLocale, String> localizations) {
    public AutocompleteSuggestion {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(localizations, "localizations must not be null");
    }

    public AutocompleteSuggestion(String name, T value) {
        this(name, value, Map.of());
    }

    public static <T> AutocompleteSuggestion<T> of(String name, T value) {
        return new AutocompleteSuggestion<>(name, value);
    }

    public static AutocompleteSuggestion<String> of(String name_and_value) {
        return of(name_and_value, name_and_value);
    }
}
