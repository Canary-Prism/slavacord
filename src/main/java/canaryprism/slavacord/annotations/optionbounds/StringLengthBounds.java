package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>the length bounds of a STRING option.</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface StringLengthBounds {

    /**
     * the minimum length that the user may enter, greater than or equal to 0, less than or equal to {@link #max}
     * @return the minimum length that the user may enter
     */
    long min() default 0;

    /**
     * the maximum length that the user may enter, greater than or equal to {@link #min}
     * @return the maximum length that the user may enter
     */
    long max() default Long.MAX_VALUE;
}
