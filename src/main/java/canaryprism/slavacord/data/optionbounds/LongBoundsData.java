package canaryprism.slavacord.data.optionbounds;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;

public record LongBoundsData(long min, long max) implements OptionBoundsData {

    public LongBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");
    }

    @Override
    public void apply(SlashCommandOptionData builder) {
        if (min != Long.MIN_VALUE)
            builder.setIntegerBoundsMin(min);

        if (max != Long.MAX_VALUE)
            builder.setIntegerBoundsMax(max);
    }
    
}
