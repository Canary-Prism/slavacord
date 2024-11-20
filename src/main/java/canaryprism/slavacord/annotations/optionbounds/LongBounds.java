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
     * the minimum value that the user may enter, less than or equal to {@link #max}
     * @return the minimum value that the user may enter
     */
    long min() default Long.MIN_VALUE;

    /**
     * the maximum value that the user may enter, greater than or equal to {@link #min}
     * @return the maximum value that the user may enter
     */
    long max() default Long.MAX_VALUE;
}
