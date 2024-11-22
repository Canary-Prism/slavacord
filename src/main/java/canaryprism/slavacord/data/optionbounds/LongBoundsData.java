package canaryprism.slavacord.data.optionbounds;

import org.javacord.api.interaction.SlashCommandOptionBuilder;

public record LongBoundsData(long min, long max) implements OptionBoundsData {

    public LongBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");
    }

    @Override
    public void apply(SlashCommandOptionBuilder builder) {
        if (min != Long.MIN_VALUE)
            builder.setLongMinValue(min);

        if (max != Long.MAX_VALUE)
            builder.setLongMaxValue(max);
    }
    
}
