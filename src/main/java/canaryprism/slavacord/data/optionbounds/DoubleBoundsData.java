package canaryprism.slavacord.data.optionbounds;

import org.javacord.api.interaction.SlashCommandOptionBuilder;

public record DoubleBoundsData(double min, double max) implements OptionBoundsData {

    public DoubleBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");
    }

    @Override
    public void apply(SlashCommandOptionBuilder builder) {
        builder.setDecimalMinValue(min);
        builder.setDecimalMaxValue(max);
    }
    
}
