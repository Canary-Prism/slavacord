package canaryprism.slavacord.data;

import org.javacord.api.interaction.SlashCommandOptionChoiceBuilder;

public record SlashCommandOptionChoiceData<T>(
    String name,
    T value
) implements Data {
    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        return "    ".repeat(indent) + "SlashCommandOptionChoiceData [name = " + name + ", value = " + value + "]";
    }

    public SlashCommandOptionChoiceBuilder toSlashCommandOptionChoiceBuilder() {
        if (value instanceof String) {
            return new SlashCommandOptionChoiceBuilder().setName(name).setValue((String) value);
        } else if (value instanceof Long) {
            return new SlashCommandOptionChoiceBuilder().setName(name).setValue((long) value);
        } else if (value.getClass().isEnum()) {
            return new SlashCommandOptionChoiceBuilder().setName(name).setValue(((Enum<?>)value).ordinal());
        } else {
            throw new RuntimeException("Invalid type for SlashCommandOptionChoiceData.value: " + value.getClass());
        }
    }
}
