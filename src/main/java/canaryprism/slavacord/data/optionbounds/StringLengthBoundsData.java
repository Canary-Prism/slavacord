package canaryprism.slavacord.data.optionbounds;

import org.javacord.api.interaction.SlashCommandOptionBuilder;

public record StringLengthBoundsData(long min, long max) implements OptionBoundsData {

    public StringLengthBoundsData {
        if (min > max)
            throw new IllegalArgumentException("min value must not be greater than max value");

        if (min < 0)
            throw new IllegalArgumentException("min can't be less than 0");
    }

    @Override
    public void apply(SlashCommandOptionBuilder builder) {
        builder.setMinLength(min);
        builder.setMaxLength(max);
    }
}
