package canaryprism.slavacord.annotations;

import canaryprism.discordbridge.api.server.permission.PermissionType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>sets the default required permissions of a user to run a command</p>
 * 
 * <p><strong>NOTE: because of how Discord works, this can only be applied to a root command or root command group! (directly in your Commands class)</strong></p>
 * 
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
public @interface RequiresPermissions {

    /**
     * <p>
     * the permissions that any given user must have to invoke this command or command group
     * </p>
     * 
     * <p>
     * <b><i>if you specify an empty array, it will use the default required
     * permissions of Discord, which currently is equivalent to
     * </i>{@code @RequiresPermissions(PermissionType.ADMINISTRATOR)}</b>
     * </p>
     * 
     * @return an array of required permissions
     */
    PermissionType[] value();
}
