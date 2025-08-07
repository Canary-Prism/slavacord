package canaryprism.slavacord;

import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CreateGlobal;
import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.annotations.OptionChoiceLong;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

@CreateGlobal(contexts = {})
public class ProcessingTest implements Commands {

    @Command(name = "mrrp", description = "nya")
    private void mrrp(@Autocompletes(autocompleter = "") @Option(name = "mrrp", longChoices = { @OptionChoiceLong(name = "3", value = 3) })SlashCommandOptionType e) {

    }

    @org.junit.jupiter.api.Test
    public void mewo() {

    }
}
