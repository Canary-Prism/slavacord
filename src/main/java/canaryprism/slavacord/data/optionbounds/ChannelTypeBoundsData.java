package canaryprism.slavacord.data.optionbounds;

import canaryprism.discordbridge.api.channel.ChannelType;
import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ChannelTypeBoundsData(Set<? extends ChannelType> allowed_types) implements OptionBoundsData {

    public ChannelTypeBoundsData {
        //noinspection SuspiciousMethodCalls
        if (allowed_types.containsAll(every_channel_type)) {
            // as an optimisation just remove the bound
            allowed_types = null;
        }
    }

    @Override
    public void apply(SlashCommandOptionData builder) {
        builder.setChannelTypeBounds(allowed_types);
    }

    private static final Set<ChannelType> every_channel_type = Stream.of(ChannelType.values()).collect(Collectors.toSet());
}
