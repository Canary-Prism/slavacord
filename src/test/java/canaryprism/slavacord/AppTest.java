package canaryprism.slavacord;

import canaryprism.discordbridge.api.channel.ChannelType;
import canaryprism.discordbridge.api.misc.DiscordLocale;
import canaryprism.slavacord.annotations.*;
import canaryprism.slavacord.annotations.optionbounds.ChannelTypeBounds;
import canaryprism.slavacord.annotations.optionbounds.DoubleBounds;
import canaryprism.slavacord.annotations.optionbounds.LongBounds;
import canaryprism.slavacord.annotations.optionbounds.StringLengthBounds;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;
import canaryprism.slavacord.exceptions.ParsingException;
import canaryprism.slavacord.mock.MockDiscordApi;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
    }
    
    @Test
    public void cantMakeCommandHandlerWithNullApi() {
        try {
            new CommandHandler(null);
            fail("Should have thrown NPE");
        } catch (NullPointerException e) {
            // do nothing
        }
    }

 
    @Test
    public void fakeDiscordApiTeehee() {
        CommandHandler handler = new CommandHandler(new MockDiscordApi());
        assertNotNull(handler);

        @CreateGlobal
        class Mewo implements Commands {
            @Trans(locale = DiscordLocale.CHINESE_TAIWAN, name = "mewo", description = "mewo")
            @Trans(locale = DiscordLocale.JAPANESE, name = "mewo", description = "mewo")
            @ReturnsResponse
            @Command(name = "mewo", description = "mewo")
            public String mewo(String nya) {
                return nya;
            }
        }

        try {
            handler.register(new Mewo(), false);
            fail("should not be able to parse");
        } catch (ParsingException e) {
            assertEquals("All parameters must have either @Interaction or @Option\n" + //
                        "    with parameter canaryprism.slavacord.AppTest$1Mewo.mewo(String arg0)\n" + //
                        "    in method canaryprism.slavacord.AppTest$1Mewo.mewo\n" + //
                        "    at class canaryprism.slavacord.AppTest$1Mewo", e.getMessage());
        }
    }

    @Test
    public void returnsResponseAllowsOptionalOfString() {
        CommandHandler handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @ReturnsResponse
            @Command(name = "mewo", description = "mewo")
            public Optional<String> mewo() {
                return Optional.of("mewo");
            }
        }


        handler.register(new Mewo(), false);
    }

    @Test
    public void returnsResponseDisallowsOptionalOfAnythingElse() {
        CommandHandler handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @ReturnsResponse
            @Command(name = "mewo", description = "mewo")
            public Optional<String> mewo() {
                return Optional.of("mewo");
            }

            @ReturnsResponse
            @Command(name = "mrrp", description = "mrrp")
            public Optional<Object> mrrp() {
                return Optional.of("mewo");
            }
        }

        try {
            handler.register(new Mewo(), false);
            fail("should not be able to parse");
        } catch (ParsingException e) {
            // do nothing
        }
    }

    @Test
    public void autoCompleteParsingTest() {
        var handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @Command(name = "mewo", description = "mewo")
            public void mewo(@Autocompletes(autocompleter = "autocompleter") @Option(name = "nya", description = "nya") String nya) {
                // do nothing
            }

            @Autocompleter
            public List<AutocompleteSuggestion<String>> autocompleter(String nya) {
                return null;
            }
        }

        handler.register(new Mewo(), false);
    }

    @Test
    public void optionBoundsParsingTest() {
        var handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @Command(name = "mewo", description = "mewo")
            static void mewo(
                @StringLengthBounds @Option(name = "one") String str,
                @DoubleBounds @Option(name = "two") double d,
                @LongBounds @Option(name = "three") long l,
                @ChannelTypeBounds({ ChannelType.SERVER_TEXT }) @Option(name = "four") RegularServerChannel channel
            ) {}
        }

        handler.register(Mewo.class, false);
    }

    @Test
    public void testsToMakeIntellijHappy() {
        var handler = new CommandHandler(new MockDiscordApi());

        handler.setDefaultThreadingMode(ThreadingMode.PLATFORM);

        handler.stop();
    }

    @Test
    public void boundedWildcardAutocompleteProviders() {
//        Configurator.setRootLevel(Level.TRACE)
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");


        var handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @Command(name = "mewo", description = "mewo")
            public void mewo(@Autocompletes(autocompleter = "autocompleter") @Option(name = "nya", description = "nya") String nya) {
                // do nothing
            }

            @Autocompleter
            public List<? extends AutocompleteSuggestion<? extends String>> autocompleter(String nya) {
                return null;
            }
        }

        handler.register(new Mewo(), true);
    }

    @Test
    public void mixingBoxingTypesAutocompleter() {
//        Configurator.setRootLevel(Level.TRACE);
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");

        var handler = new CommandHandler(new MockDiscordApi());

        @CreateGlobal
        class Mewo implements Commands {
            @Command(name = "mewo", description = "mewo")
            public void mewo(@Autocompletes(autocompleter = "autocompleter") @Option(name = "nya", description = "nya") long nya) {
                // do nothing
            }

            @Autocompleter
            public List<? extends AutocompleteSuggestion<? extends Long>> autocompleter() {
                return null;
            }
        }

        handler.register(new Mewo(), true);
    }

    @Test
    public void enumChoices() {
        var handler = new CommandHandler(new MockDiscordApi());

        enum Meow {
            mrrp, nya
        }
        @CreateGlobal
        class Mewo implements Commands {
            @Command(name = "mewo", description = "mewo")
            public void mewo(@Option(name = "nya", description = "nya") Meow nya) {
                // do nothing
            }

        }

        handler.register(new Mewo(), true);
    }
}
