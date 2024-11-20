package canaryprism.slavacord.annotations.optionbounds;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface DoubleBounds {
    double min() default Double.MIN_NORMAL;

    double max() default Double.MAX_VALUE;
}
