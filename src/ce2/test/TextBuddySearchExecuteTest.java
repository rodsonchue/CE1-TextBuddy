package ce2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import ce2.textbuddy.TextBuddy;

/**
 * This test attempts to run the search functionality by passing a typical
 * search query command. Instead of displaying to System.out, the output is set
 * to an external file so that comparison can be done. Because the stream adds
 * additional format "\r" to every line, the expected string includes that as
 * well. Test assert is done by extracting file contents into a StringBuilder
 * object to compare.
 *
 * @author Rodson Chue Le Sheng [A0110787A]
 */
public class TextBuddySearchExecuteTest {
    FileOutputStream fos = null;
    PrintWriter pw = null;
    StringBuilder sbExpected;
    StringBuilder sbOutput;
    String testCase = "search a";
    File testOutputStreamFile = null;
    File testSearchFile = null;

    /**
     * Prepares all the files used in the test
     */
    @Before
    public void setup() {
        // Setup the files for execution and to pipe output stream
        this.setupAllFiles();
    }

    @Test
    public void test() {
        // Prepare output stream
        this.setupOutputStream();

        // Dump test scenario into test file
        this.setupScenario();

        // Generate expected result
        this.generateExpectedResult();

        // Conduct Test
        TextBuddy.executeGenericCommand(this.testCase, this.testSearchFile);
        this.sbOutput = this.FileTextToStringBuilder(this.testOutputStreamFile);
        assertEquals(this.sbExpected.toString(), this.sbOutput.toString());

    }

    /**
     * Creates the file by attempting to instantiate a PrintWriter object
     *
     * @param fl
     */
    private void createEmptyFile(File fl) {
        try {
            new PrintWriter(fl);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

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

    /**
     * Generates the expected result for the test case
     */
    private void generateExpectedResult() {
        this.sbExpected = new StringBuilder();
        this.sbExpected.append(this.TextExpectedString());

    }

    /**
     * Initialisation of all test Files
     */
    private void setupAllFiles() {
        this.testSearchFile = this.setupFile("testSearchFile.txt");
        this.testOutputStreamFile = this.setupFile("testOutputStreamFile.txt");

        // Try to create the files
        this.createEmptyFile(this.testOutputStreamFile);
        this.createEmptyFile(this.testSearchFile);
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
     * Setup an output stream to divert System.out outputs to for testing
     * purposes.
     */
    private void setupOutputStream() {
        try {
            this.fos = new FileOutputStream(this.testOutputStreamFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(this.fos));

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
        this.pw = this.setupPrintWriter(this.testSearchFile);
        this.pw.print(this.testInputString());
        this.pw.close();

    }

    /**
     * Contains the String that is used as input
     *
     * @return
     */
    private String testInputString() {
        return "lots of spaces in this line       \n"
                + "           begins with spaces\n" + "only hasOneSpace\n"
                + " \n" + "     \n" + "thisLinehasNoSpacing\n"
                + "MoreLinesWithNoSpacing\n" + "AAAAAAAAAAAA\n"
                + "%%%%%%%%%%\n";
    }

    /**
     * Contains the string that we expect the test to produce. Has additional
     * format \r because of stream choice
     *
     * @return
     */
    private Object TextExpectedString() {
        return "Search Results for \"a\": \n"
                + "Line 1: lots of spaces in this line       \r\n"
                + "Line 2:            begins with spaces\r\n"
                + "Line 3: only hasOneSpace\r\n"
                + "Line 6: thisLinehasNoSpacing\r\n"
                + "Line 7: MoreLinesWithNoSpacing\r\n" + "\r\n";
        // Extra newlines behind
    }
}
