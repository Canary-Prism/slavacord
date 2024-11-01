package canaryprism.slavacord.exceptions;

/**
 * <p>Exception thrown when parsing fails for the {@link canaryprism.slavacord.CommandHandler}</p>
 */
public class ParsingException extends RuntimeException {

    private String parse_trace = "";

    /**
     * <p>constructs a new ParsingException with the specified detail message and parse trace</p>
     * @param message the detail message
     * @param parse_trace the parse trace, in other words what was being parsed when the exception was thrown
     */
    public ParsingException(String message, String parse_trace) {
        super(message);
        addParseTrace(parse_trace);
    }

    /**
     * <p>constructs a new ParsingException with the specified detail message and cause</p>
     * @param message the detail message
     * @param parse_trace the parse trace, in other words what was being parsed when the exception was thrown
     * @param cause the cause
     */
    public ParsingException(String message, String parse_trace, Throwable cause) {
        super(message, cause);
        addParseTrace(parse_trace);
    }

    /**
     * <p>appends the given parse trace to the exception</p>
     * <p>please don't call this yourself</p>
     * @param parse_trace the parse trace to append
     * @return this
     */
    public ParsingException addParseTrace(String parse_trace) {
        this.parse_trace += "\n    " + parse_trace;
        return this;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + parse_trace;
    }
}
