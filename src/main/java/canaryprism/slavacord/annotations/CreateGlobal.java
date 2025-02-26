package canaryprism.slavacord.annotations;

import canaryprism.discordbridge.api.interaction.ContextType;
import canaryprism.discordbridge.api.interaction.InstallationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to mark a Command class as containing global commands.</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface CreateGlobal {

    /**
     * <p>Context in which the commands declared can be used</p>
     *
     * <p>If the value contains {@link ContextType#OTHER_DM}, {@link #install()} must contain {@link InstallationType#USER_INSTALL}</p>
     *
     * <p>If not specified default is empty array which is interpreted as all of them</p>
     *
     * @return The contexts in which the commands can be used
     */
    ContextType[] contexts() default {};

    /**
     * <p>Where the commands declared should be installed</p>
     *
     * <p>
     * If a command is only installed on {@link InstallationType#SERVER_INSTALL} then you may only use the command
     * when you're interacting with the bot as joined into a server, not installed on you as a user
     * </p>
     *
     * <p>If not specified default is empty array which is interpreted as all of them</p>
     *
     * @return Where the commands should be installed
     */
    InstallationType[] install() default {};
}
