package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * a slash command option for command methods.
 * this is to provide more information about the option for Discord
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface Option {

    /**
     * The name of the option
     * @return The name of the option
     */
    String name();

    /**
     * A brief description of what the option is for
     * @return A brief description of what the option is for
     */
    String description() default "";

    /**
     * <p>the option choices for this option</p>
     * 
     * <p><b>NOTE: if this is not empty this Option must be annotated on type {@code long} or {@code java.lang.Long}</b></p>
     * 
     * <p>
     * it is also recommended that you don't use this method and instead make an enum for the choices,
     * then you can simply change your option to take the enum type and the command handler will automatically
     * convert them for discord and back for you
     * </p>
     * 
     * @return an array of choices for the option
     */
    OptionChoiceLong[] longChoices() default {};

    /**
     * <p>the option choices for this option</p>
     *
     * <p><b>NOTE: if this is not empty this Option must be annotated on type {@code double} or {@code java.lang.Double}</b></p>
     *
     * <p>
     * it is also recommended that you don't use this method and instead make an enum for the choices,
     * then you can simply change your option to take the enum type and the command handler will automatically
     * convert them for discord and back for you
     * </p>
     *
     * @return an array of choices for the option
     */
    OptionChoiceDouble[] doubleChoices() default {};

    /**
     * <p>the option choices for this option</p>
     * 
     * <p><b>NOTE: if this is not empty this Option must be annotated on type {@code java.lang.String}</b></p>
     * 
     * <p>
     * it is also recommended that you don't use this method and instead make an enum for the choices,
     * then you can simply change your option to take the enum type and the command handler will automatically
     * convert them for discord and back for you
     * </p>
     * 
     * @return an array of choices for the option
     */
    OptionChoiceString[] stringChoices() default {};
}
