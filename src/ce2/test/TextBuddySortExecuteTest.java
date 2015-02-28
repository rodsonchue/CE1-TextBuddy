package ce2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import ce2.textbuddy.TextBuddy;

/**
 * This test attempts to run the sort functionality by passing a legitimate sort
 * command. Since sort automatically sorts the lines in the file, the test is
 * done by comparing the "newly sorted" file with an expected file by extracting
 * its information out as a StringBuilder to compare.
 *
 * @author Rodson Chue Le Sheng [A0110787A]
 */
public class TextBuddySortExecuteTest {
    PrintWriter pw = null;
    StringBuilder sbExpected;
    StringBuilder sbOutput;
    File testSortFile = null;

    /**
     * Prepares all the files used in the test
     */
    @Before
    public void setup() {
        // Setup the files as for "before" and "after" function execute
        this.setupAllFiles();
    }

    @Test
    public void test() {
        // Dump test scenario into test file
        this.setupScenario();

        // Generate expected result
        this.generateExpectedResult();

        // Conduct test
        TextBuddy.executeGenericCommand("sort", this.testSortFile);
        this.sbOutput = this.FileTextToStringBuilder(this.testSortFile);
        assertEquals(this.sbExpected.toString(), this.sbOutput.toString());
    }

    /**
     * Converts a File's contents into a StringBuilder object Assumes that file
     * exists
     *
     * @param fl
     * @return StringBuilder
     */
    private StringBuilder FileTextToStringBuilder(File fl) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] encoded = Files
                    .readAllBytes(Paths.get(fl.getAbsolutePath()));
            sb.append(new String(encoded));
        } catch (IOException e) {
            fail(fl.getAbsolutePath() + " failed to convert to StringBuilder");
        }

        return sb;
    }

    private void generateExpectedResult() {
        this.sbExpected = new StringBuilder();
        this.sbExpected.append(this.testExpectedString());

    }

    /**
     * Initialisation of all test Files
     */
    private void setupAllFiles() {
        this.testSortFile = this.setupFile("testSortFile.txt");

    }

    /**
     * Setup a file to prepare it for JUnit testing. There should not already
     * exist a file, if it does then delete it.
     *
     * @param filePath
     * @return
     */
    private File setupFile(String filePath) {
        File tmp = new File(filePath);
        if (tmp.exists()) {
            tmp.delete();
        }

        return tmp;
    }

    /**
     * Setup a PrintWriter object for JUnit testing.
     *
     * @param fl
     * @return
     */
    private PrintWriter setupPrintWriter(File fl) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fl));
        } catch (IOException e) {
            fail("PrintWriter failed to be created");
        }

        return pw;
    }

    /**
     * Sets up the test scenario
     */
    private void setupScenario() {
        this.pw = this.setupPrintWriter(this.testSortFile);
        this.pw.print(this.testInputString());
        this.pw.close();

    }

    /**
     * Contains the string that we expect the test to produce
     *
     * @return
     */
    private String testExpectedString() {
        return " \n"
                + "!!!!!!!!!!!!!!\n"
                + "%%%%%%%%\n"
                + "&&&&&&&&&&&&&&&\n"
                + "''''''\n"
                + ". line begins with a non numerical nor character\n"
                + "00\n"
                + "000\n"
                + "111111111111\n"
                + "111111111111111\n"
                + "a\n"
                + "aaaaa\n"
                + "aaaaaa\n"
                + "aaaaaaaaaaaaaaaaa\n"
                + "add \n"
                + "add lots of sashimi\n"
                + "bbbbbbbb\n"
                + "bbbbbbbbbb\n"
                + "bbbbbbbbbbbbbbbbbbbbbbbbbbb\n"
                + "delete 1\n"
                + "display\n"
                + "exit\n"
                + "hello world\n"
                + "more test cases wouldnt hurt\n"
                + "numbers and characters to sort\n"
                + "some additional cases\n"
                + "sort\n"
                + "very long text\n"
                + "very very very long text which is meant to test how effective the sort is\n"
                + "zz\n" + "zzzzzzzzzzz\n";
    }

    /**
     * Contains the String that is used as input
     *
     * @return
     */
    private String testInputString() {
        return "aaaaaa\n"
                + "aaaaa\n"
                + "aaaaaaaaaaaaaaaaa\n"
                + "bbbbbbbb\n"
                + "bbbbbbbbbb\n"
                + "bbbbbbbbbbbbbbbbbbbbbbbbbbb\n"
                + "111111111111111\n"
                + "111111111111\n"
                + "very very very long text which is meant to test how effective the sort is\n"
                + ". line begins with a non numerical nor character\n"
                + "some additional cases\n" + "hello world\n" + "zzzzzzzzzzz\n"
                + "zz\n" + " \n" + "add lots of sashimi\n" + "add \n"
                + "delete 1\n" + "display\n" + "sort\n" + "exit\n"
                + "more test cases wouldnt hurt\n" + "''''''\n" + "%%%%%%%%\n"
                + "&&&&&&&&&&&&&&&\n" + "!!!!!!!!!!!!!!\n" + "000\n" + "00\n"
                + "very long text\n" + "numbers and characters to sort\n"
                + "a\n";
    }
}
