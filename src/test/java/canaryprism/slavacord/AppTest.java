package canaryprism.slavacord;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.javacord.api.interaction.DiscordLocale;
import org.junit.jupiter.api.Test;

import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CreateGlobal;
import canaryprism.slavacord.annotations.ReturnsResponse;
import canaryprism.slavacord.annotations.Trans;
import canaryprism.slavacord.exceptions.ParsingException;
import canaryprism.slavacord.mock.MockDiscordApi;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    
    @Test
    public void cantMakeCommandHandlerWithNullApi() {
        try {
            new CommandHandler(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (NullPointerException e) {}
    }

    static int[] a = new int[1];
    @Test
    public void mewo() {
        synchronized (a) {
            a[0] = 1;
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
}
