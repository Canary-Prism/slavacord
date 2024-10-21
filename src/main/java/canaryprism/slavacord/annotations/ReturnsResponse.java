package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface ReturnsResponse {
    boolean ephemeral() default false;
    boolean silent() default false;

    /**
     * whether the response should be split into multiple messages on the limit of 2000 characters
     * @return boolean
     */
    boolean splitOnLimit() default true;
}
