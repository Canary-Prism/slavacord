package canaryprism.slavacord.data;

import java.util.Map;

import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandOptionChoiceBuilder;

public record SlashCommandOptionChoiceData<T>(
    String name,
    T value,
    Map<DiscordLocale, String> localizations
) implements Data {
    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        return "    ".repeat(indent) + "SlashCommandOptionChoiceData [name = " + name + ", value = " + value + "]";
    }

    public SlashCommandOptionChoiceBuilder toSlashCommandOptionChoiceBuilder() {
        SlashCommandOptionChoiceBuilder builder;
        if (value instanceof String) {
            builder = new SlashCommandOptionChoiceBuilder().setValue((String) value);
        } else if (value instanceof Long) {
            builder = new SlashCommandOptionChoiceBuilder().setValue((long) value);
        } else if (value.getClass().isEnum()) {
            builder = new SlashCommandOptionChoiceBuilder().setValue(((Enum<?>)value).ordinal());
        } else {
            throw new IllegalArgumentException("Invalid type for SlashCommandOptionChoiceData.value: " + value.getClass());
        }

        builder.setName(name);

        localizations.forEach(builder::addNameLocalization);

        return builder;
    }
}
