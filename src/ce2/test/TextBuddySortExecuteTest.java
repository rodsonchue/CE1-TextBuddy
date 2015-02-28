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

public class TextBuddySortExecuteTest {
    PrintWriter pw = null;
    StringBuilder sbExpected;
    StringBuilder sbOutput;
    File testSortFile = null;

    @Before
    public void setup() {
        // Setup the files as for "before" and "after" function execute
        this.testSortFile = this.setupFile("testSortFile.txt");

        // Dump test scenario into test file
        String testInputString = this.TestInputString();
        this.pw = this.setupPrintWriter(this.testSortFile);
        this.pw.print(testInputString);
        this.pw.close();

        // Generate expected result
        this.sbExpected = new StringBuilder();
        this.sbExpected.append(this.TextExpectedString());
    }

    @Test
    public void test() {
        TextBuddy.executeGenericCommand("sort", this.testSortFile);
        this.sbOutput = this.FileTextToStringBuilder(this.testSortFile);
        assertEquals(this.sbExpected.toString(), this.sbOutput.toString());
    }

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

    private File setupFile(String filePath) {
        File tmp = new File(filePath);
        if (tmp.exists()) {
            tmp.delete();
        }

        return tmp;
    }

    private PrintWriter setupPrintWriter(File fl) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fl));
        } catch (IOException e) {
            fail("PrintWriter failed to be created");
        }

        return pw;
    }

    private String TestInputString() {
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

    private String TextExpectedString() {
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
}
