package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to mark a method parameter as requesting the underlying SlashCommandInteraction that triggered the execution of this method</p>
 * <p>When a method parameter is marked as an Interaction, the command handler will attempt to inject the SlashCommandInteraction that triggered the execution of the command into the parameter</p>
 * <p>must be used as the first parameter of a method annotated with {@Link Command}</p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface Interaction {
    
}
