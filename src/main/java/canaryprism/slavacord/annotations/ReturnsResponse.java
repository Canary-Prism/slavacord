package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to mark a command method's return value as the response to be sent back to the user</p>
 * <p><b>NOTE: if used the method must have a return type of {@code String} or {@code Optional<String>}</b></p>
 * <p>
 * this uses an ImmediateResponder unless {@link Async @Async} is also
 * present on a method <em>and</em> {@link Async#respondLater()} is true
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface ReturnsResponse {

    /**
     * incompatible with {@link #silent()}
     * @return whether the response should be sent as an ephemeral message
     */
    boolean ephemeral() default false;

    /**
     * incompatible with {@link #ephemeral()}
     * @return whether the response should be sent as a silent message
     */
    boolean silent() default false;

    /**
     * whether the response should be split into multiple messages on the limit of 2000 characters
     * @return whether the response should be split into multiple messages on the limit of 2000 characters
     */
    boolean splitOnLimit() default true;
}
