package canaryprism.slavacord.data;

import canaryprism.discordbridge.api.misc.DiscordLocale;

import java.util.Map;

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

    public canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionChoiceData toSlashCommandOptionChoiceBuilder() {
        var builder = new canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionChoiceData(name, value);

        builder.setNameLocalizations(localizations);

        return builder;
    }
}
