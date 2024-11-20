package canaryprism.slavacord.autocomplete.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface Autocompletes {
    Class<?> autocompleterClass() default Void.class; // default to Void.class which actually parses to implicitly the class this annotation is used in

    String autocompleter();
}
