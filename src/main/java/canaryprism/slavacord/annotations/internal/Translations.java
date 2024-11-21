package canaryprism.slavacord.annotations.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import canaryprism.slavacord.annotations.Trans;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Translations {
    Trans[] value();
}
