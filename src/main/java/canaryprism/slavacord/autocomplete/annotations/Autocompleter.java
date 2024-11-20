package canaryprism.slavacord.autocomplete.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a method as an autocompleter. This method will be called to provide suggestions for an autocomplete command. {@link canaryprism.slavacord.annotations.Async @Async} may be annotated on this method.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface Autocompleter {
}
