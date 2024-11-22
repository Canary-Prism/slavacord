package canaryprism.slavacord.data.autocompletefilter;

import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.filteroptions.MatchIn;
import canaryprism.slavacord.autocomplete.filteroptions.MatchStart;

import java.util.List;
import java.util.Objects;

import static canaryprism.slavacord.util.Patterns.PUNCTUATION;
import static canaryprism.slavacord.util.Patterns.WHITESPACE;

public record AutocompleteFilterData(
    MatchStart match_start,
    MatchIn contained_in,
    boolean ignore_case,
    boolean ignore_whitespace,
    boolean ignore_punctuation,
    boolean prune_to_limit,
    boolean remove_duplicates,
    boolean contiguous
) implements AutocompleteFilter {

    @Override
    public <T> List<? extends AutocompleteSuggestion<? extends T>> filter(List<? extends AutocompleteSuggestion<? extends T>> suggestion_list, T user_input) {

        var user_input_string = Objects.toString(user_input);

        var stream = suggestion_list.stream();

        if (remove_duplicates)
            stream = stream.distinct();

        stream = stream.filter((e) -> matches(e, user_input_string));

        if (prune_to_limit)
            stream = stream.limit(25);

        return stream.toList();
    }

    private boolean matches(AutocompleteSuggestion<?> suggestion, String input) {
        return switch (contained_in) {
            case NAME -> matches(suggestion.name(), input);
            case VALUE -> matches(Objects.toString(suggestion.value()), input);
            case BOTH -> matches(suggestion.name(), input) || matches(Objects.toString(suggestion.value()), input);
        };
    }

    private boolean matches(String suggestion, String input) {
        if (ignore_whitespace) {
            suggestion = WHITESPACE.matcher(suggestion).replaceAll("");
        }

        if (ignore_punctuation) {
            suggestion = PUNCTUATION.matcher(suggestion).replaceAll("");
        }

        if (ignore_case) {
            suggestion = suggestion.toUpperCase();
            input = input.toUpperCase();
        }

        if (contiguous) {
            return switch (match_start) {
                case BEGGINING -> suggestion.startsWith(input);
                case ANYWHERE -> suggestion.contains(input);
            };
        } else {
            var suggestion_chars = suggestion.toCharArray();
            var input_chars = input.toCharArray();
            int suggestion_pointer = 0;

            match_test: {
                if (match_start == MatchStart.BEGGINING) {
                    if (input_chars[0] != suggestion_chars[0])
                        break match_test;
                }
                for (char input_char : input_chars) {
                    if (suggestion_pointer >= suggestion_chars.length)
                        break match_test;

                    while (input_char != suggestion_chars[suggestion_pointer]) {
                        suggestion_pointer++;

                        if (suggestion_pointer >= suggestion_chars.length)
                            break match_test;
                    }
                    suggestion_pointer++;
                }

                return true;
            }

            return false;
        }
    }
}
