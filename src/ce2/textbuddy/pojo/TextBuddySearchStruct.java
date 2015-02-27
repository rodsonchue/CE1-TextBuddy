package ce2.textbuddy.pojo;

/**
 * Class defines the structure used to store the result of searching a file
 */
public class TextBuddySearchStruct {
    // The String contained in the line
    public String line;

    // The lineNumber corresponding to the String
    public int lineNumber;

    public TextBuddySearchStruct() {
    }

    public TextBuddySearchStruct(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public TextBuddySearchStruct(String line) {
        this.line = line;
    }

    public TextBuddySearchStruct(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }
}
