package canaryprism.slavacord.autocomplete.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>Marks a parameter as autocompletes. only String and long options may be autocompleted</p>
 * <p>if an option is autocompleted it may not also have static option choices</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface Autocompletes {

    /**
     * The class of the autocompleter, if not specified the class this annotation is used in will be used
     * @return The class of the autocompleter
     */
    Class<?> autocompleterClass() default Void.class; // default to Void.class which actually parses to implicitly the class this annotation is used in

    /**
     * The method name of the autocompleter
     * @return The method name of the autocompleter
     */
    String autocompleter();
}
