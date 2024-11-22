package canaryprism.slavacord.autocomplete.filteroptions;

/**
 * enum specifying which part of a suggestion must the user input match
 */
public enum MatchStart {

    /**
     * <p>the <strong>beginning</strong> of a suggestion must match with the user input</p>
     *
     * <p>this also works for non-contiguous matching and simply additionally requires that the <strong>first character</strong> of a suggestion matches the user input's first character</p>
     */
    BEGGINING,

    /**
     * <p>a suggestion can match with the user input <strong>anywhere</strong></p>
     */
    ANYWHERE
}
