/*
 * A Simple CLI Program that does text file manipulation. It supports the
 * following commands: add <string> | delete <string> | clear | display | exit
 */

package ce2.textbuddy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Rodson Chue Le Sheng [A0110787A]
 */
public class TextBuddy {

    // List of commands accepted by the program's main driver
    public enum Command {
        ADD, CLEAR, DEFAULT, DELETE, DISPLAY, EXIT
    }

    private static final int ADD_COMMAND_LENGTH = 3;
    private static final int ADD_COMMAND_WITH_SPACE = 4;
    private static final String CLEAR_MESSAGE = "all content deleted from ";
    private static final int DELETE_COMMAND_LENGTH = 6;
    private static final int DELETE_COMMAND_LENGTH_WITH_SPACE = 7;
    private static final String DELETED_FROM = "deleted from ";
    private static final String DOES_NOT_CONTAIN_LINE = " does not contain line ";
    private static final String ERROR_FILE_DOES_NOT_EXIST = "Error: File does not exist!";
    private static final String EXIT_MESSAGE = "Thank you for using TextBuddy!";
    private static final String FILE_IO_EXCEPTION = "Error: Unexpected IOException!\n Please restart application.";
    private static final String INTEGERS_ONLY = "Error: Integers only!\nUsage: delete <line to delete>\n";
    private static final String IS_EMPTY = " is empty";
    private static final String NO_FILE_FOUND = "Error: No file specified!";
    private static final String NO_TEXT_MESSAGE = "No text specified.\nUsage: ADD <text_to_add>";
    private static final String READY_TO_USE = " is ready to use.";
    private static final String SPACE = " ";
    private static final String UNEXPECTED_EXCEPTION = "Error: UnexpectedException!\n Please restart application.";
    private static final String UNKNOWN_COMMAND_MESSAGE = "Unknown Command!\n\nAvailable Commands:\nADD | CLEAR | DEFAULT | DELETE | DISPLAY | EXIT\n";
    private static final String WELCOME_INTRODUCTION = "Welcome to TextBuddy. ";

    private static final int ZERO = 0;;

    // Tests whether a string is an Integer
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * @param args
     *            First parameter is taken as the File Location, any extra
     *            parameters are ignored. Program exits if first parameter is
     *            not properly specified
     */
    public static void main(String[] args) {
        // parse the program arguements
        String fileLocation = readParams(args);

        // Check arguement correctness
        if (fileLocation == null) {
            errorNoFileSpecified();
        } else if (isFileDoesNotExist(fileLocation)) {
            errorFileDoesNotExist();
        } else {
            driverIOLoop(fileLocation);
        }
    }

    /**
     * @param userString
     *            The full String that the user inputs into the command line.
     *            Includes the command as well.
     * @return Command enum corresponding to the user input.
     */
    public static Command parseCommand(String userString) {
        String[] userWords = userString.split(SPACE);
        String userCommand = userWords[0].toUpperCase();

        switch (userCommand) {
            case "ADD" :
                return Command.ADD;

            case "CLEAR" :
                return Command.CLEAR;

            case "DELETE" :
                return Command.DELETE;

            case "DISPLAY" :
                return Command.DISPLAY;

            case "EXIT" :
                return Command.EXIT;

            default :
                return Command.DEFAULT;

        }
    }

    /**
     * @param fl
     *            The file that the clear command is instructed to clear
     */
    private static void displayClearMessage(File fl) {
        System.out.println(CLEAR_MESSAGE + fl.getName());

    }

    /**
     * @param fl
     *            The file that is empty
     */
    private static void displayEmptyFile(File fl) {
        System.out.println(fl.getName() + IS_EMPTY);
    }

    /**
     * Called when the command "exit" is invoked. Provides feedback to the user
     * that the program is terminating
     */
    private static void displayExitMessageNormal() {
        System.out.println(EXIT_MESSAGE);
        System.exit(0);

    }

    /**
     * @param fl
     *            The file whose contents are to be displayed
     */
    private static void displayFileContents(File fl) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fl));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            int counter = 1;

            while (line != null) {
                sb.append(counter + ". ");
                sb.append(line);
                sb.append(System.lineSeparator());
                counter++;
                line = br.readLine();
            }
            String everythingFormatted = sb.toString();

            System.out.println(everythingFormatted);
        } catch (Exception e) {
            errorFileIOException();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }

            } catch (Exception e) {
                errorFileIOException();
            }
        }
    }

    /**
     * @param insertText
     *            Text that the user input through the CLI to be inserted into
     *            the file
     * @param fl
     *            The file that the insertText is to be inserted into
     */
    private static void displayInsertedText(String insertText, File fl) {
        System.out.println("added to " + fl.getName() + ": \"" + insertText
                + "\"");

    }

    /**
     * @param line
     *            The particular line in the text file that is to be deleted
     * @param fl
     *            The file that contains the line to be deleted
     */
    private static void displayLineDeleted(String line, File fl) {
        System.out.println(DELETED_FROM + fl.getName() + ": \"" + line + "\"");

    }

    /**
     * @param lineNumber
     *            The lineNumber that the user has specified
     * @param fl
     *            File for which the line does not exist
     */
    private static void displayLineNotFound(int lineNumber, File fl) {
        System.out.println(fl.getName() + DOES_NOT_CONTAIN_LINE + lineNumber);

    }

    /**
     * Invoked when user invokes the 'add' command but does not include any text
     * to add to the text file.
     */
    private static void displayNoTextMessage() {
        System.out.println(NO_TEXT_MESSAGE);

    }

    /**
     * Invoked when the user inputs an unknown command. Feedback to the user on
     * the list of available commands.
     */
    private static void displayUnknownCommandMessage() {
        System.out.println(UNKNOWN_COMMAND_MESSAGE);

    }

    /**
     * @param fl
     *            File specified in the parameters when running the program
     */
    private static void displayWelcomeMessage(File fl) {
        System.out.println(WELCOME_INTRODUCTION + fl.getName() + READY_TO_USE);

    }

    /**
     * This is the main driver loop of the program.
     *
     * @param fileLocation
     *            Location of the file as per specified by the user.
     */
    private static void driverIOLoop(String fileLocation) {
        File fl = null;
        BufferedReader br = null;
        try {
            fl = getFile(fileLocation);
            br = new BufferedReader(new InputStreamReader(System.in));

            displayWelcomeMessage(fl);

            boolean isNotExitCommand = true;
            do {
                String userInput = br.readLine();
                Command userCommand = parseCommand(userInput);
                switch (userCommand) {
                    case ADD :
                        executeAdd(userInput, fl);
                        break;

                    case CLEAR :
                        executeClear(fl);
                        break;

                    case DEFAULT :
                        executeDefault();
                        break;

                    case DELETE :
                        executeDelete(userInput, fl);
                        break;

                    case DISPLAY :
                        executeDisplay(fl);
                        break;

                    case EXIT :
                        executeExit();
                        isNotExitCommand = false;
                        break;

                    default :
                        errorUnexpectedException();
                        isNotExitCommand = false;
                        break;
                }
            } while (isNotExitCommand);
        } catch (Exception e) {
            errorFileIOException();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                errorFileIOException();
            }
        }
    }

    /**
     * Invoked when user calls a 'delete' command but the parameter is not an
     * integer
     */
    private static void errorDeleteNotNumber() {
        System.out.println(INTEGERS_ONLY);

    }

    /**
     * Invoked whenever a file is to be referenced but cannot be found / does
     * not exist
     */
    private static void errorFileDoesNotExist() {
        System.out.println(ERROR_FILE_DOES_NOT_EXIST);

    }

    /**
     * FileIO Problems which do not currently have a solution to them will
     * invoke this.
     */
    private static void errorFileIOException() {
        System.out.print(FILE_IO_EXCEPTION);

    }

    /**
     * Invoked when the program parameters does not point to a file, such as a
     * directory or bad pathname
     */
    private static void errorNoFileSpecified() {
        System.out.println(NO_FILE_FOUND);
    }

    /**
     * Invoked when users input the 'delete' command but do not provide an
     * integer number as the line number to delete
     */
    private static void errorNoNumberGivenToDelete() {
        errorDeleteNotNumber();

    }

    /**
     * All other unexpected exceptions that are not File-IO related will invoke
     * this.
     */
    private static void errorUnexpectedException() {
        System.out.println(UNEXPECTED_EXCEPTION);

    }

    /**
     * @param userInput
     *            The full String that the user inputs into the command line.
     *            Includes the command as well.
     * @param fl
     *            File to append the parameter's text to
     */
    private static void executeAdd(String userInput, File fl) {
        if (userInput.length() > ADD_COMMAND_WITH_SPACE) {
            // Remove the first 4 characters
            // literally removing "ADD " from String
            // then appending a newline behind
            String insertText = userInput.substring(4);
            BufferedWriter bw = null;

            try {
                // true = append mode
                bw = new BufferedWriter(new FileWriter(fl, true));
                displayInsertedText(insertText, fl);
                bw.write(insertText.concat("\n"));
                bw.close();

            } catch (Exception e) {
                errorFileIOException();
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (Exception e) {
                        errorFileIOException();
                    }
                }
            }
        } else if ((userInput.length() == ADD_COMMAND_LENGTH)
                || (userInput.length() == ADD_COMMAND_WITH_SPACE)) {
            // When the userInput is exactly "ADD" or "ADD "
            displayNoTextMessage();
            // We do not do anything
        } else {
            errorUnexpectedException();
        }

    }

    /**
     * @param fl
     *            File to 'clear'
     */
    private static void executeClear(File fl) {
        PrintWriter pw = null;
        try {
            fl.delete();
            fl.createNewFile();

            displayClearMessage(fl);
        } catch (Exception e) {
            errorFileIOException();
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    errorFileIOException();
                }
            }
        }

    }

    /**
     * Invoked when user inputs an unknown command. Gives feedback to user that
     * command is not understood by the program
     */
    private static void executeDefault() {
        displayUnknownCommandMessage();

    }

    /**
     * @param userInput
     *            The full String that the user inputs into the command line.
     *            Includes the command as well.
     * @param fl
     *            The file to delete a line from
     */
    private static void executeDelete(String userInput, File fl) {
        // "D E L E T E _"
        if (userInput.length() > DELETE_COMMAND_LENGTH_WITH_SPACE) {
            String numberString = getNumberString(userInput);
            if (isInteger(numberString)) {
                int lineNumber = getLineNumber(numberString);
                removeLineFromFile(fl, lineNumber);
            } else {
                errorDeleteNotNumber();
            }

        } else if ((userInput.length() == DELETE_COMMAND_LENGTH)
                || (userInput.length() == DELETE_COMMAND_LENGTH_WITH_SPACE)) {
            errorNoNumberGivenToDelete();
        } else {
            errorUnexpectedException();
        }

    }

    /**
     * @param fl
     *            File to display the contents of
     */
    private static void executeDisplay(File fl) {
        if (isEmptyFile(fl)) {
            displayEmptyFile(fl);
        } else {
            displayFileContents(fl);
        }

    }

    /**
     * Invoked when user inputs the 'exit' command
     */
    private static void executeExit() {
        displayExitMessageNormal();

    }

    /**
     * @param fileLocation
     *            Location of the file
     * @return A file object
     */
    private static File getFile(String fileLocation) {
        return new File(fileLocation);
    }

    /**
     * @param numberInString
     *            an Integer that is stored as a string
     * @return the integer value of the String
     */
    private static int getLineNumber(String numberInString) {
        // Assumption: String is already checked by isNumber()
        return Integer.parseInt(numberInString);
    }

    /**
     * @param userInput
     *            The full String that the user inputs into the command line.
     *            Includes the command as well.
     * @return The first "word" preceeding the command
     */
    private static String getNumberString(String userInput) {
        return userInput.substring(DELETE_COMMAND_LENGTH_WITH_SPACE).trim()
                .split(SPACE)[0];
    }

    /**
     * @param fl
     *            The File to check
     * @return true if file is empty (0 byte), false otherwise
     */
    private static boolean isEmptyFile(File fl) {
        FileInputStream fis = null;
        boolean isEmpty = false;
        try {
            fis = new FileInputStream(fl);
            if (fis.read() == -1) {
                isEmpty = true;
            }
        } catch (Exception e) {
            errorFileIOException();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                errorFileIOException();
            }
        }
        return isEmpty;
    }

    /**
     * @param fileLocation
     *            File to test for existence
     * @return true if the file does not exist, false otherwise
     */
    private static boolean isFileDoesNotExist(String fileLocation) {
        try {
            File fl = new File(fileLocation);
            return !fl.exists();
        } catch (Exception e) {
            errorFileIOException();
        }
        return false;
    }

    /**
     * @param args
     *            Program arguements
     * @return Location of the file to edit
     */
    private static String readParams(String[] args) {
        if (args.length <= ZERO) {
            return null;
        } else {
            return args[0];
        }
    }

    /**
     * @param fl
     *            File to remove line from
     * @param lineNumber
     *            Line number to remove
     */
    private static void removeLineFromFile(File fl, int lineNumber) {
        File tempFile = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            tempFile = new File(fl.getAbsolutePath() + ".tmp");
            br = new BufferedReader(new FileReader(fl));
            pw = new PrintWriter(new FileWriter(tempFile));

            int counter = 1;
            boolean isLineRemoved = false;
            String line = null;
            while ((line = br.readLine()) != null) {

                if (counter != lineNumber) {
                    pw.println(line);
                } else {
                    isLineRemoved = true;
                    displayLineDeleted(line, fl);

                }
                counter++;
            }
            pw.close();
            br.close();

            if (!isLineRemoved) {
                // No lines have been removed
                displayLineNotFound(lineNumber, fl);
            }

            fl.delete();
            tempFile.renameTo(fl);
            fl = tempFile;

        } catch (Exception e) {
            errorFileIOException();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                errorFileIOException();
            }
        }

    }
}
