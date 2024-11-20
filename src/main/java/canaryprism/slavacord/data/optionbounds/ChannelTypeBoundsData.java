package canaryprism.slavacord.data.optionbounds;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.UnknownRegularServerChannel;
import org.javacord.api.interaction.SlashCommandOptionBuilder;

public record ChannelTypeBoundsData(Set<ChannelType> allowed_types) implements OptionBoundsData {

    public ChannelTypeBoundsData {
        if (allowed_types.containsAll(every_channel_type)) {
            // as an optimisation just remove the bound
            allowed_types = null;
        }
    }

    @Override
    public void apply(SlashCommandOptionBuilder builder) {
        builder.setChannelTypes(allowed_types);
    }
    

    private static final Set<ChannelType> every_channel_type = Stream.of(ChannelType.values()).collect(Collectors.toSet());

    public static Class<? extends Channel> getChannelClass(ChannelType type) {
        return switch (type) {
            case SERVER_TEXT_CHANNEL -> ServerTextChannel.class;
            case CHANNEL_CATEGORY -> ChannelCategory.class;
            case SERVER_FORUM_CHANNEL -> ServerForumChannel.class;
            
            case SERVER_NEWS_THREAD -> ServerThreadChannel.class; // there's no specific class for this
            case SERVER_PRIVATE_THREAD -> ServerThreadChannel.class;
            case SERVER_PUBLIC_THREAD -> ServerThreadChannel.class;
            
            case SERVER_VOICE_CHANNEL -> ServerVoiceChannel.class;
            case SERVER_STAGE_VOICE_CHANNEL -> ServerStageVoiceChannel.class;

            case SERVER_DIRECTORY_CHANNEL -> UnknownRegularServerChannel.class; // Javacord doesn't have a class for this
            case SERVER_NEWS_CHANNEL -> UnknownRegularServerChannel.class; // Javacord doesn't have a class for this
            case SERVER_STORE_CHANNEL -> UnknownRegularServerChannel.class; // Javacord doesn't have a class for this


            case UNKNOWN -> Channel.class;
            case GROUP_CHANNEL -> Channel.class;
            case PRIVATE_CHANNEL -> PrivateChannel.class;
        };
    }
}
