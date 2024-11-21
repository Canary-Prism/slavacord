package canaryprism.slavacord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.javacord.api.interaction.DiscordLocale;

import canaryprism.slavacord.annotations.internal.Translations;

/**
 * Trans(lated) name and description for a Command or Option
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@Repeatable(Translations.class)
public @interface Trans {

    /**
     * The locale of the translation
     * @return The locale of the translation
     */
    DiscordLocale locale();

    /**
     * The localized name of the command or option
     * @return The name of the command or option
     */
    String name() default "";

    /**
     * The localized description of the command or option
     * @return The description of the command or option
     */
    String description() default "";
}
