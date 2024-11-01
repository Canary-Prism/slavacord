package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>an option choice of type {@code long}</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface OptionChoiceLong {

    /**
     * the name of the choice shown to the user
     * @return the name of the choice shown to the user
     */
    String name();

    /**
     * the long value of the choice
     * @return the long value of the choice
     */
    long value();
}
