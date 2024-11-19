package canaryprism.slavacord.data;

import java.lang.reflect.Method;

public record AutocompletableData(Method method, Object instance, Type type, boolean requires_interaction) {
    public enum Type {
        STRING,
        LONG,
    }
}
