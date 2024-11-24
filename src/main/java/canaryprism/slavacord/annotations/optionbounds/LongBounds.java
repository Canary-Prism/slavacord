package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>the bounds of a LONG option.</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface LongBounds {

    /**
     * <p>the minimum value that the user may enter less than or equal to {@link #max}</p>
     * <p>this value must not be smaller than -2^53 as specified by {@link org.javacord.api.interaction.SlashCommandOptionType#LONG}</p>
     * <p>it can, however, be {@link Long#MIN_VALUE} as this library ignores this value</p>
     *
     * @return the minimum value that the user may enter
     */
    long min() default Long.MIN_VALUE;

    /**
     * <p>the maximum value that the user may enter greater than or equal to {@link #min}</p>
     * <p>this value must not be greater than 2^53 as specified by {@link org.javacord.api.interaction.SlashCommandOptionType#LONG}</p>
     * <p>it can, however, be {@link Long#MAX_VALUE} as this library ignores this value</p>
     *
     * @return the maximum value that the user may enter
     */
    long max() default Long.MAX_VALUE;
}
