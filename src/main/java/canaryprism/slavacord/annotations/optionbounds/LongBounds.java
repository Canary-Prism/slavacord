package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface LongBounds {
    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;
}
