package canaryprism.slavacord.data;

import canaryprism.slavacord.data.autocompletefilter.AutocompleteFilter;

import java.lang.reflect.Method;

public record AutocompletableData(Method method, Object instance, Type type, boolean requires_interaction, AutocompleteFilter filter) {
    public enum Type {
        STRING,
        LONG,
    }
}
