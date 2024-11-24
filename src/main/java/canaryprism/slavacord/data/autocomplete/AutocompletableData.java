package canaryprism.slavacord.data.autocomplete;

import canaryprism.slavacord.data.autocomplete.filter.AutocompleteFilter;

import java.lang.reflect.Method;

public record AutocompletableData(Method method, Object instance, Type type, Param[] params, AutocompleteFilter filter) {
    public enum Type {
        STRING,
        LONG,
    }

    public enum Param {
        INTERACTION, VALUE
    }
}
