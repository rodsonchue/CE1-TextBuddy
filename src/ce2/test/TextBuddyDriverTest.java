package ce2.test;

import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import ce2.textbuddy.TextBuddy;

public class TextBuddyDriverTest {
    FileInputStream fis = null;
    File inputFile = null;
    File outputFile = null;

    @Before
    public void setup() {
        try {
            this.inputFile = new File("inputText.txt");
            this.outputFile = new File("outputDisplay.txt");

            this.fis = new FileInputStream(this.inputFile);
            System.setIn(this.fis);
            System.setOut(new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(this.outputFile))));

        } catch (Exception e) {
            fail("File preparation failed");
        }

        // Run the driverloop
        TextBuddy.driverIOLoop("outputTextFile.txt");
    }

    @Test
    public void test() {
        try {
            // Open outputTextFile.txt

            // Open outputDisplay.txt

        } catch (Exception e) {
            fail("File read failed");
        }
        fail("Not yet implemented");
    }

}
