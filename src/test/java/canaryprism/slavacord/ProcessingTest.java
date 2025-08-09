package canaryprism.slavacord;

import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CreateGlobal;
import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

import java.util.List;

@CreateGlobal(contexts = {})
public class ProcessingTest implements Commands {

    @Command(name = "mrrp", description = "nya")
    private void mrrp(@Autocompletes(autocompleter = "mrr") @Option(name = "mrrp") String e) {

    }

    @Autocompleter
    private List<AutocompleteSuggestion<Long>> mrr(long mrr) {
        return List.of();
    }

    @org.junit.jupiter.api.Test
    public void mewo() {

    }
}
