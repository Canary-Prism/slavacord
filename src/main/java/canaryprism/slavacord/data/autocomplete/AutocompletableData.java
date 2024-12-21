package canaryprism.slavacord.data.autocomplete;

import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.data.autocomplete.filter.AutocompleteFilter;

import java.lang.reflect.Method;

public record AutocompletableData(Method method, Object instance, SlashCommandOptionType type, Param[] params, AutocompleteFilter filter) {

    public enum Param {
        INTERACTION, IMPLEMENTATION_INTERACTION, VALUE
    }
}
