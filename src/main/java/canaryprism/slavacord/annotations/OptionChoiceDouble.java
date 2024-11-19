package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>an option choice of type {@code double}</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface OptionChoiceDouble {

    /**
     * the name of the choice shown to the user
     * @return the name of the choice shown to the user
     */
    String name();

    /**
     * the double value of the choice
     * @return the double value of the choice
     */
    double value();

    /**
     * translations for the option choice names
     * @return a list of translations for the option choice name
     */
    OptionChoiceTrans[] translations() default {};
}
