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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ce2.textbuddy.pojo.TextBuddySearchStruct;

/**
 * @author Rodson Chue Le Sheng [A0110787A]
 */
public class TextBuddy {

    // List of commands accepted by the program's main driver
    public enum Command {
        ADD,
        /*
         * Adds the parameter specified as the last entry in the file
         */
        CLEAR,
        /*
         * Empties the file
         */
        DEFAULT,
        /*
         * All other inputs that do not match other commands go here. Displays
         * error to user
         */
        DELETE,
        /*
         * Deletes a particular entry number in the file as specified by user
         */
        DISPLAY,
        /*
         * Show what the file currently contains
         */
        EXIT,
        /*
         * Exit the program
         */
        SEARCH
    }

    // Magical Numbers
    private static final int ZERO = 0;

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
        String[] userWords = userString.split(TextBuddyMessage.SPACE);
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

            case "SEARCH" :
                return Command.SEARCH;

            default :
                return Command.DEFAULT;

        }
    }

    /**
     * Function closes a Reader, usually because its operation has completed
     *
     * @param br
     */
    private static void closeReader(Reader br) {
        if (br != null) {
            try {
                br.close();
            } catch (Exception e) {
                errorFileIOException();
            }
        } else {
            // Nothing needed to be done about null object
        }

    }

    /**
     * Function closes a Writer, usually because its operation has completed.
     *
     * @param bw
     *            BufferedWriter to close
     */
    private static void closeWriter(Writer bw) {
        if (bw != null) {
            try {
                bw.close();
            } catch (Exception e) {
                errorFileIOException();
            }
        } else {
            // Nothing needed to be done about null object
        }

    }

    /**
     * @param fl
     *            The file that the clear command is instructed to clear
     */
    private static void displayClearMessage(File fl) {
        System.out.println(TextBuddyMessage.CLEAR_MESSAGE + fl.getName());

    }

    /**
     * @param fl
     *            The file that is empty
     */
    private static void displayEmptyFile(File fl) {
        System.out.println(fl.getName() + TextBuddyMessage.IS_EMPTY);
    }

    /**
     * Called when the command "exit" is invoked. Provides feedback to the user
     * that the program is terminating
     */
    private static void displayExitMessageNormal() {
        System.out.println(TextBuddyMessage.EXIT_MESSAGE);
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
            closeReader(br);
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
        System.out.println(TextBuddyMessage.DELETED_FROM + fl.getName()
                + ": \"" + line + "\"");

    }

    /**
     * @param lineNumber
     *            The lineNumber that the user has specified
     * @param fl
     *            File for which the line does not exist
     */
    private static void displayLineNotFound(int lineNumber, File fl) {
        System.out.println(fl.getName()
                + TextBuddyMessage.DOES_NOT_CONTAIN_LINE + lineNumber);

    }

    private static void displayNoQuery() {
        System.out.println(TextBuddyMessage.SEARCH_MISSING_QUERY);
    }

    private static void displayNoResultsFound(String match) {
        System.out.printf(TextBuddyMessage.SEARCH_NO_RESULT, match);

    }

    /**
     * Invoked when user invokes the 'add' command but does not include any text
     * to add to the text file.
     */
    private static void displayNoTextMessage() {
        System.out.println(TextBuddyMessage.NO_TEXT_MESSAGE);

    }

    private static void displayResultList(
            LinkedList<TextBuddySearchStruct> resultList) {
        StringBuilder sb = new StringBuilder();
        TextBuddySearchStruct entry;

        while (!resultList.isEmpty()) {
            entry = resultList.removeFirst();
            sb.append(entry.lineNumber + ". ");
            sb.append(entry.line);
            sb.append(System.lineSeparator());
        }
        String everythingFormatted = sb.toString();

        System.out.println(everythingFormatted);

    }

    private static void displayResultsFoundHeader(String match) {
        System.out.printf(TextBuddyMessage.SEARCH_RESULT_HEADER, match);

    }

    /**
     * Invoked when the user inputs an unknown command. Feedback to the user on
     * the list of available commands.
     */
    private static void displayUnknownCommandMessage() {
        System.out.println(TextBuddyMessage.UNKNOWN_COMMAND_MESSAGE);

    }

    /**
     * @param fl
     *            File specified in the parameters when running the program
     */
    private static void displayWelcomeMessage(File fl) {
        System.out.println(TextBuddyMessage.WELCOME_INTRODUCTION + fl.getName()
                + TextBuddyMessage.READY_TO_USE);

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

                    case SEARCH :
                        executeSearch(fl, userInput);
                        break;

                    default :
                        displayUnknownCommandMessage();
                        break;
                }
            } while (isNotExitCommand);
        } catch (Exception e) {
            errorFileIOException();
        } finally {
            closeReader(br);
        }
    }

    /**
     * Invoked when user calls a 'delete' command but the parameter is not an
     * integer
     */
    private static void errorDeleteNotNumber() {
        System.out.println(TextBuddyMessage.INTEGERS_ONLY);

    }

    /**
     * Invoked whenever a file is to be referenced but cannot be found / does
     * not exist
     */
    private static void errorFileDoesNotExist() {
        System.out.println(TextBuddyMessage.ERROR_FILE_DOES_NOT_EXIST);

    }

    /**
     * FileIO Problems which do not currently have a solution to them will
     * invoke this.
     */
    private static void errorFileIOException() {
        System.out.print(TextBuddyMessage.FILE_IO_EXCEPTION);

    }

    /**
     * Invoked when the program parameters does not point to a file, such as a
     * directory or bad pathname
     */
    private static void errorNoFileSpecified() {
        System.out.println(TextBuddyMessage.NO_FILE_FOUND);
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
        System.out.println(TextBuddyMessage.UNEXPECTED_EXCEPTION);

    }

    /**
     * @param userInput
     *            The full String that the user inputs into the command line.
     *            Includes the command as well.
     * @param fl
     *            File to append the parameter's text to
     */
    private static void executeAdd(String userInput, File fl) {
        if (userInput.length() > TextBuddyMessage.ADD_COMMAND_WITH_SPACE) {
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
                closeWriter(bw);
            }
        } else if ((userInput.length() == TextBuddyMessage.ADD_COMMAND_LENGTH)
                || (userInput.length() == TextBuddyMessage.ADD_COMMAND_WITH_SPACE)) {
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
            closeWriter(pw);
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
        if (userInput.length() > TextBuddyMessage.DELETE_COMMAND_LENGTH_WITH_SPACE) {
            String numberString = getNumberString(userInput);
            if (isInteger(numberString)) {
                int lineNumber = getLineNumber(numberString);
                removeLineFromFile(fl, lineNumber);
            } else {
                errorDeleteNotNumber();
            }

        } else if ((userInput.length() == TextBuddyMessage.DELETE_COMMAND_LENGTH)
                || (userInput.length() == TextBuddyMessage.DELETE_COMMAND_LENGTH_WITH_SPACE)) {
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
     * Executes the command to search the file using a String to match lines
     * with.
     *
     * @param fl
     *            file to search
     * @param userInput
     *            userInput including the command
     */
    private static void executeSearch(File fl, String userInput) {
        if (userInput.length() < TextBuddyMessage.SEARCH_WORD_LENGTH) {
            displayNoQuery();
        } else {
            String match = userInput
                    .substring(TextBuddyMessage.SEARCH_WORD_LENGTH);

            LinkedList<TextBuddySearchStruct> resultList = searchFile(fl, match);
            if (resultList.isEmpty()) {
                displayNoResultsFound(match);
            } else {
                displayResultsFoundHeader(match);
                displayResultList(resultList);
            }
        }
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
        return userInput
                .substring(TextBuddyMessage.DELETE_COMMAND_LENGTH_WITH_SPACE)
                .trim().split(TextBuddyMessage.SPACE)[0];
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
     * Reads the program arguements. Only accept the first parameter even if
     * there exist more than one.
     *
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
            closeReader(br);
            closeWriter(pw);
        }

    }

    /**
     * Searches a given file with a String to match each line with. Returns a
     * LinkedList of lines that contain that String as a substring
     *
     * @param fl
     *            file to search
     * @param match
     *            String to match line with
     * @return LinkedList<TextBuddySearchStruct> list of lines containing match
     *         with their line number
     */
    private static LinkedList<TextBuddySearchStruct> searchFile(File fl,
            String match) {
        LinkedList<TextBuddySearchStruct> lineList = new LinkedList<TextBuddySearchStruct>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fl));
            String line;

            // Begin with the first line
            int lineNumber = TextBuddyMessage.FIRST_LINE;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(match);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    lineList.addLast(new TextBuddySearchStruct(line, lineNumber));
                }
                lineNumber++; // Move onto the next line
            }

        } catch (Exception e) {
            errorFileIOException();
        } finally {
            closeReader(br);
        }

        return lineList;
    }
}
