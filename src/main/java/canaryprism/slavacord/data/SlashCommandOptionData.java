package canaryprism.slavacord.data;

import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.data.autocomplete.AutocompletableData;
import canaryprism.slavacord.data.optionbounds.OptionBoundsData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public record SlashCommandOptionData<T>(
    String name,
    String description,
    LocalizationData localizations,
    boolean required,
    SlashCommandOptionType type,
    List<SlashCommandOptionData<?>> options,
    List<SlashCommandOptionChoiceData<T>> choices,
    AutocompletableData autocompletable_data,
    Method method,
    Object instance,
    boolean requires_interaction,
    boolean stores_enum,
    OptionBoundsData bounds
) implements Data {
    @Override
    public String toString() {
        return toString(0);
    }

    @SuppressWarnings({"java:S1643", "StringConcatenationInLoop"}) // toString(int) isn't meant to be performant anyway
    public String toString(int indent) {
        String indent_str = "    ".repeat(indent);

        String value = "";

        value += indent_str + "SlashCommandOptionData [name = " + name + ", description = " + description
                + ", required = " + required + ", type = " + type + ", requires_interaction = "
                + requires_interaction + ", method = " + method + ", options = [";
        

        if (options != null && !options.isEmpty()) {
            for (SlashCommandOptionData<?> option : options) {
                value += "\n" + option.toString(indent + 1) + ",";
            }
            value += "\n" + indent_str + "]";
        } else {
            value += "]";
        }

        value += indent_str + ", choices = [";

        if (choices != null && !choices.isEmpty()) {
            for (SlashCommandOptionChoiceData<T> choice : choices) {
                value += "\n" + choice.toString(indent + 1) + ",";
            }
            value += "\n" + indent_str + "]";
        } else {
            value += "]";
        }

        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SlashCommandOptionData<?> other) {

            return name.equals(other.name) && description.equals(other.description)
                    && required == other.required && type == other.type
                    && (((options == null || options.isEmpty()) && (other.options() == null || other.options().isEmpty())) || Objects.equals(options, other.options))
                    && (((choices == null || choices.isEmpty()) && (other.choices() == null || other.choices().isEmpty())) || Objects.equals(choices, other.choices));
        } else {
            return false;
        }
    }

    public canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData toSlashCommandOptionBuilder() {
        var builder = new canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData(name, description, type);

        builder.setRequired(required);

        builder.setAutocompletable(autocompletable_data != null);

        if (bounds != null) {
            bounds.apply(builder);
        }

        builder.setNameLocalizations(localizations.names());
        builder.setDescriptionLocalizations(localizations.descriptions());

        if (options != null && !options.isEmpty()) {
            builder.setOptions(options.stream()
                    .map(SlashCommandOptionData::toSlashCommandOptionBuilder)
                    .toList());
        }

        if (choices != null && !choices.isEmpty()) {
            builder.setChoices(choices.stream()
                    .map(SlashCommandOptionChoiceData::toSlashCommandOptionChoiceBuilder)
                    .toList());
        }

        return builder;
    }
}
