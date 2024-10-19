package canaryprism.slavacord.exceptions;

public class ParsingException extends RuntimeException {

    private String parse_trace = "";

    public ParsingException(String message, String parse_trace) {
        super(message);
        addParseTrace(parse_trace);
    }

    public ParsingException(String message, String parse_trace, Throwable cause) {
        super(message, cause);
        addParseTrace(parse_trace);
    }

    public ParsingException addParseTrace(String parse_trace) {
        this.parse_trace += "\n    " + parse_trace;
        return this;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + parse_trace;
    }
}
