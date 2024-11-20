package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.javacord.api.entity.channel.ChannelType;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface ChannelTypeBounds {
    ChannelType[] value();
}
