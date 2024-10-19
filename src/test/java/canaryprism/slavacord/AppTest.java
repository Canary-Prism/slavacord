package canaryprism.slavacord;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
}
