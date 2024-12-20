package canaryprism.slavacord.data.optionbounds;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;

public interface OptionBoundsData {
    void apply(SlashCommandOptionData builder);
}
