package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * <p>This annotation is used to mark a Commandable as containing server commands.</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface CreateServer {

    /**
     * ID of server to create command in
     * @return ID of server to create command in
     */
    long value();
}
