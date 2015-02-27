package ce2.textbuddy;

public class TextBuddyMessage {
    public static final int ADD_COMMAND_LENGTH = 3;
    public static final int ADD_COMMAND_WITH_SPACE = 4;
    public static final String CLEAR_MESSAGE = "all content deleted from ";
    public static final int DELETE_COMMAND_LENGTH = 6;
    public static final int DELETE_COMMAND_LENGTH_WITH_SPACE = 7;
    public static final String DELETED_FROM = "deleted from ";
    public static final String DOES_NOT_CONTAIN_LINE = " does not contain line ";
    public static final String ERROR_FILE_DOES_NOT_EXIST = "Error: File does not exist!";
    public static final String EXIT_MESSAGE = "Thank you for using TextBuddy!";
    public static final String FILE_IO_EXCEPTION = "Error: Unexpected IOException!\n Please restart application.";
    public static final int FIRST_LINE = 1;
    public static final String INTEGERS_ONLY = "Error: Integers only!\nUsage: delete <line to delete>\n";
    public static final String IS_EMPTY = " is empty";
    public static final String NO_FILE_FOUND = "Error: No file specified!";
    public static final String NO_TEXT_MESSAGE = "No text specified.\nUsage: ADD <text_to_add>";
    public static final String READY_TO_USE = " is ready to use.";
    public static final String SEARCH_MISSING_QUERY = "Error: No search term specified!\nUsage: search <query>";
    public static final String SEARCH_NO_RESULT = "No hits found for query: \"%s\"!\n";
    public static final String SEARCH_RESULT_HEADER = "Search Results for \"%s\": \n";
    public static final int SEARCH_WORD_LENGTH = 7; // "S E A R C H _"
    public static final String SORT_SUCCESS = "File sorted alphabetically!\n";
    public static final String SPACE = " ";
    public static final String UNEXPECTED_EXCEPTION = "Error: UnexpectedException!\n Please restart application.";
    public static final String UNKNOWN_COMMAND_MESSAGE = "Unknown Command!\n\nAvailable Commands:\nADD | CLEAR | DELETE | DISPLAY | SEARCH | SORT | EXIT\n";
    public static final String WELCOME_INTRODUCTION = "Welcome to TextBuddy. ";
}
