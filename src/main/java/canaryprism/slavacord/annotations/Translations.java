package canaryprism.slavacord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * container annotation for {@link Trans} annotations
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Translations {

    /**
     * array of {@link Trans} translation values. prefer repeating the annotation if possible
     * @return an array of Trans translations
     */
    Trans[] value();
}
