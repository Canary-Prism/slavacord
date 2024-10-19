package canaryprism.slavacord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.PARAMETER })
public @interface Option {
    String name();
    String description() default "";

    OptionChoiceLong[] longChoices() default {};
    OptionChoiceString[] stringChoices() default {};
}
