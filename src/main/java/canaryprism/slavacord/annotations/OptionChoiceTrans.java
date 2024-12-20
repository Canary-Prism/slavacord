package canaryprism.slavacord.annotations;

import canaryprism.discordbridge.api.misc.DiscordLocale;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Trans(lated) name for an OptionChoice
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({})
public @interface OptionChoiceTrans {
    /**
     * The locale of the translation
     * @return The locale of the translation
     */
    DiscordLocale locale();

    /**
     * The localized name of the option choice
     * @return The name of the option choice
     */
    String value();
}
