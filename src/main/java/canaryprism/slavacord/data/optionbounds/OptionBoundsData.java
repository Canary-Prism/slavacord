package canaryprism.slavacord.data.optionbounds;

import org.javacord.api.interaction.SlashCommandOptionBuilder;

public interface OptionBoundsData {
    void apply(SlashCommandOptionBuilder builder);
}
