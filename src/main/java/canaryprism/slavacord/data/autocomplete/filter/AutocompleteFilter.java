package canaryprism.slavacord.data.autocomplete.filter;

import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;

import java.util.List;

public sealed interface AutocompleteFilter permits AutocompleteFilterData {
    <T> List<? extends AutocompleteSuggestion<? extends T>> filter(List<? extends AutocompleteSuggestion<? extends T>> suggestion_list, T user_input);
}
