package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import canaryprism.slavacord.ThreadingMode;



/**
 * <p>This annotation is used to mark a method as asynchronous.</p>
 * <p>when a method is marked as asynchronous, it will be executed in a separate thread by the command handler.</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
public @interface Async {
    /**
     * has no effect if the method does not have {@link canaryprism.slavacord.annotations.ReturnsResponse @ReturnsResponse} annotation
     */
    boolean respondLater() default true;


    ThreadingMode threadingMode() default ThreadingMode.none;
}
