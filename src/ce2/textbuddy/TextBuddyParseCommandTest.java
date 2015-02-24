package ce2.textbuddy;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ce2.textbuddy.TextBuddy.Command;

/**
 * Tests parsing of the basic variant of Commands
 */
@RunWith(value = Parameterized.class)
public class TextBuddyParseCommandTest {

    private Command expected;
    private String input;

    /**
     * Creates a Command Test with the input and expected Command enum.
     *
     * @param input
     *            string to be parsed.
     * @param expected
     *            Command enum to be expected.
     */
    public TextBuddyParseCommandTest(String input, Command expected) {
        this.input = input;
        this.expected = expected;
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to enum({1})")
    // @formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
        /**
         * Normal tests
         */
        { "add", TextBuddy.Command.ADD }, { "clear", TextBuddy.Command.CLEAR },
                { "rubbishtext", TextBuddy.Command.DEFAULT },
                { "delete", TextBuddy.Command.DELETE },
                { "display", TextBuddy.Command.DISPLAY },
                { "exit", TextBuddy.Command.EXIT } });
    }

    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        assertEquals(this.expected, TextBuddy.parseCommand(this.input));
    }

}
