package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a method as a command group that hold more subcommands.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface CommandGroup {

    /**
     * The name of the command.
     * @return The name of the command.
     */
    String name();

    /**
     * A brief description of what the command does.
     * <p><strong>NOTE: this will only take effect on root command groups (not nested)</strong></p>
     * @return A brief description of what the command does.
     */
    String description() default "";

    /**
     * Whether the command can be executed in a direct message with the bot.
     * @return Whether the command can be executed in a direct message with the bot.
     */
    boolean enabledInDMs() default true;

    /**
     * Whether the command is NSFW or not
     * <p>
     * <strong>NOTE: this will only take effect on root commands (not
     * nested)</strong>
     * </p>
     * 
     * @return Whether the command is NSFW or not
     */
    boolean nsfw() default false;
}