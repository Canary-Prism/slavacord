package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.javacord.api.entity.channel.ChannelType;

/**
 * <p>the bounds of a CHANNEL type option.</p>
 * <p>when used the user may only select a channel from one of these types.</p>
 * <p>the declared parameter's type must be able to hold all of these channel types</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface ChannelTypeBounds {

    /**
     * the channel types that the user may select from
     * @return an array of channel types that the user may select from
     */
    ChannelType[] value();
}
