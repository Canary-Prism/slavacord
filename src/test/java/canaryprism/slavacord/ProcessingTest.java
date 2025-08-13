package canaryprism.slavacord;

import canaryprism.discordbridge.api.misc.DiscordLocale;
import canaryprism.slavacord.annotations.*;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;

import java.util.List;

@CreateGlobal
public class ProcessingTest implements Commands {

    @Trans(locale = DiscordLocale.JAPANESE)
    @Trans(locale = DiscordLocale.JAPANESE)
    @Command(name = "mrrp", description = "nya")
    private void mrrp(@Option(name = "mrr") Mewo e) {
        @CommandGroup(name = "mrrr")
        class Mrrr {

        }
    }

    enum Mewo {

    }

    @CommandGroup(name = "mrow")
    class Meep {

    }
    @CommandGroup(name = "e")
    class Mrow {

        @CommandGroup(name = "r")
        class Mrrp {
            @Command(name = "mrr", description = "nya")
            private void mrrp(Mrrp this) {
                System.out.println(this);
            }
        }
    }

    @Autocompleter
    private List<AutocompleteSuggestion<Long>> mrr(long mrr) {
        return List.of();
    }

    @org.junit.jupiter.api.Test
    public void mewo() {

    }
}
