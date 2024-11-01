package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>an option choice of type {@code java.lang.String}</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface OptionChoiceString {

    /**
     * the name of the choice shown to the user, if not provided the value will be used
     * @return the name of the choice shown to the user, if not provided the value will be used
     */
    String name() default "";

    /**
     * the string value of the choice
     * @return the string value of the choice
     */
    String value();
}
