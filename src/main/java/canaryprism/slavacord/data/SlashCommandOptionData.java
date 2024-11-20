package canaryprism.slavacord.data;

import java.lang.reflect.Method;
import java.util.List;

import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionType;

import canaryprism.slavacord.data.optionbounds.OptionBoundsData;

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

    @SuppressWarnings("java:S1643") // toString(int) isn't meant to be performant anyway
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
        if (obj instanceof SlashCommandOptionData) {
            SlashCommandOptionData<?> other = (SlashCommandOptionData<?>) obj;

            return name.equals(other.name) && description.equals(other.description)
                    && required == other.required && type == other.type
                    && (((options == null || options.isEmpty()) && (other.options() == null || other.options().isEmpty())) || options.equals(other.options))
                    && (((choices == null || choices.isEmpty()) && (other.choices() == null || other.choices().isEmpty())) || choices.equals(other.choices));
        } else {
            return false;
        }
    }

    public SlashCommandOptionBuilder toSlashCommandOptionBuilder() {
        SlashCommandOptionBuilder builder = new SlashCommandOptionBuilder();

        builder.setName(name);
        builder.setDescription(description);
        builder.setRequired(required);
        builder.setType(type);

        builder.setAutocompletable(autocompletable_data != null);

        if (bounds != null) {
            bounds.apply(builder);
        }

        localizations.names().forEach(builder::addNameLocalization);
        localizations.descriptions().forEach(builder::addDescriptionLocalization);

        if (options != null && !options.isEmpty()) {
            for (SlashCommandOptionData<?> option : options) {
                builder.addOption(option.toSlashCommandOptionBuilder().build());
            }
        }

        if (choices != null && !choices.isEmpty()) {
            for (SlashCommandOptionChoiceData<T> choice : choices) {
                builder.addChoice(choice.toSlashCommandOptionChoiceBuilder().build());
            }
        }

        return builder;
    }
}
