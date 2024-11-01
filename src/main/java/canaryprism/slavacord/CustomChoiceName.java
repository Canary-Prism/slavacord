package canaryprism.slavacord;

/**
 * <p>
 * this interface marks an enum that has custom option choice names for when it's used as a parameter in a command method.
 * </p>
 */
public interface CustomChoiceName {

    /**
     * <p>
     * gets the custom name of the enum value. 
     * this method is called by the {@link CommandHandler} when the enum is used as a parameter in a command method.
     * </p>
     * 
     * <p>
     * <strong>the string returned must not exceed 25 characters in length</strong>
     * </p>
     * 
     * @return the option choice name to use
     */
    String getCustomName();
}
