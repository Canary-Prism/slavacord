package canaryprism.slavacord.data.optionbounds;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;

public record StringLengthBoundsData(long min, long max) implements OptionBoundsData {

    public StringLengthBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");

        if (min < 0)
            throw new IllegalArgumentException("min can't be less than 0");
    }

    @Override
    public void apply(SlashCommandOptionData builder) {
        builder.setStringLengthBoundsMin(min);

        if (max != Long.MAX_VALUE)
            builder.setStringLengthBoundsMax(max);
    }
}
