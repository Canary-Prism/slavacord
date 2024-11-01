package canaryprism.slavacord;

/**
 * <p>
 * the threading mode to use for command dispatching, only effective when a command has been annotated with
 * {@link canaryprism.slavacord.annotations.Async Async}
 * </p>
 * 
 * <p>
 * you can set the threading mode for a command in its {@link canaryprism.slavacord.annotations.Async Async} annotation. 
 * if the threading mode is not set, the default mode of the command handler will be used.
 * </p>
 */
public enum ThreadingMode {
    /**
     * the command will be executed with platform threads
     */
    platform, 

    /**
     * similar to {@link #platform platform}, but the command will be executed in a daemon thread
     */
    daemon,

    /**
     * <i><b>Warning:</b> this mode is only available on Java 21 and above</i>
     * <p><b>i *will* yell at you if you use it before Java 21 :3</b></p>
     * <p>the command will be executed with virtual threads</p>
     */
    virtual, 

    /**
     * <p>the command will be executed with virtual threads if available, otherwise it will be executed with OS threads</p>
     * <p>this is the default mode</p>
     */
    prefervirtual,
    
    /**
     * shh, don't tell anyone
     */
    none;
}
