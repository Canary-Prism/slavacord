package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface ReturnsResponse {
    boolean ephemeral() default false;
    boolean silent() default false;
    boolean splitOnLimit() default true;
}
