package canaryprism.slavacord.data.optionbounds;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;

public record DoubleBoundsData(double min, double max) implements OptionBoundsData {

    public DoubleBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");
    }

    @Override
    public void apply(SlashCommandOptionData builder) {
        if (min != Double.NEGATIVE_INFINITY)
            builder.setNumberBoundsMin(min);

        if (max != Double.POSITIVE_INFINITY)
            builder.setNumberBoundsMax(max);
    }
    
}
