import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Project: TextBuddy CE1
 * Name: Leong Wei Ming
 * Matric No: A0110674M
 * Tutorial Group: W09
 * 
 * TextBuddy is a program written to manipulate text from a user-specified file.
 * 
 * It can do the following:
 * 
 * 1) Add a line of text into the file
 * 2) Delete a line of text from the file
 * 3) Clear all lines of text from the file
 * 4) Print out all lines of text from the file
 * 
 * Assumptions
 * 
 * 1) The user expects the line added to be appended at the end of the text file
 * 2) The user does not want indexing to be done in the file
 * 3) The user wants to load in any previous text of the file specified into the program
 * 4) All edits done to the text file will only be saved after the "exit" command
 * 
 * @author Kyle
 *
 */
public class TextBuddy {
	
	//Commonly used variables
	private static String fileName;
	private static File textFile;
	private static Scanner s = new Scanner(System.in);
	private static ArrayList<String> textList = new ArrayList<String>();
	
	//List of messages
	private static final String MESSAGE_NOT_ENOUGH_ARGUMENTS = "Please enter a filename\n";
	private static final String MESSAGE_TOO_MUCH_ARGUMENTS = "Please enter only one filename\n";
	private static final String MESSAGE_FILE_IS_DIRECTORY = "Filename is a directory, please enter a valid filename\n";
	private static final String MESSAGE_INVALID_INDEX = "Index to delete is invalid, please try again\n";
	private static final String MESSAGE_EMPTY_DISPLAY = "%s is empty\n";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MESSAGE_EXIT = "Thank you for using textbuddy\n";
	private static final String MESSAGE_ADD_SUCCESS = "added to %s: \"%s\"\n";
	private static final String MESSAGE_INCORRECT_COMMAND = "You have entered an incorrect command, please try again\n";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted from %s: \"%s\"\n";
	private static final String MESSAGE_CLEAR_SUCCESS = "all content deleted from %s\n";
	private static final String MESSAGE_SAVE_SUCCESS = "%s saved\n";
	private static final String MESSAGE_PROMPT = "Command: ";
	
	//List of constants
	private static final int EMPTY = 0;
	private static final int EXCESS = 1;
	private static final int FIRST = 0;
	private static final int USER_COMMAND = 0;
	private static final int USER_TEXT = 1;
	private static final int USER_COMMAND_AND_TEXT = 2;
	
	//List of commands
	enum COMMANDS {
		ADD, DELETE, DISPLAY, CLEAR, EXIT
	};
	
	public static void main(String[] args) throws Exception {
		checkArguments(args);
		checkFile();
		runTextEditor();
		cleanUp();
	}

	/**
	 * This method checks the number of arguments that the user entered. If user
	 * entered an incorrect number of argument, print error message and exit.
	 * Else set the filename and continue
	 */
	private static void checkArguments(String[] args) {

		// No filename entered
		if (args.length == EMPTY) {
			printMessage(MESSAGE_NOT_ENOUGH_ARGUMENTS);
			System.exit(0);
		} else if (args.length > EXCESS) { // Too many filename entered
			printMessage(MESSAGE_TOO_MUCH_ARGUMENTS);
			System.exit(0);
		} else { // Only 1 filename entered
			fileName = args[FIRST]; // Set fileName to user input
			return;
		}
	}

	/**
	 * This method checks if the file exists and loads it. Else it creates the
	 * file.
	 */
	private static void checkFile() throws Exception {
		File tempFile = new File(fileName);

		if (tempFile.exists() && !tempFile.isDirectory()) { // If the file exists and is not a directory then load the file.
			textFile = tempFile;
		} else if (tempFile.exists() && tempFile.isDirectory()) { // If the file exists but is a directory, then inform the user and exit the program.
			printMessage(MESSAGE_FILE_IS_DIRECTORY);
			System.exit(0);
		} else if (!tempFile.exists()) { // If the file does not exist, create it and assign it to textFile variable
			tempFile.createNewFile();
			textFile = tempFile;
		}
	}

	/**
	 * This method does the following 
	 * 1) Initialize the text editor 
	 * 2) Accept a user input 
	 * 3) Partition it into the command and the user's string 
	 * 4)Executes the command if command is entered correctly, else it rejects
	 */
	private static void runTextEditor() throws Exception {

		String input = "";
		String[] processedInput;

		initializeTextEditor();
		
		printMessage(String.format(MESSAGE_WELCOME, fileName));
		
		while (!input.equals("exit")) {

			input = acceptInput();
			processedInput = cleanUpInput(input);

			if (isValidInput(processedInput)) {
				executeInput(processedInput);
			} else if (!isValidInput(processedInput)) {
				printMessage(MESSAGE_INCORRECT_COMMAND);

			}
		}

	}

	/**
	 * This method initialize the text editor by loading the contents of the
	 * textfile into an arraylist of strings
	 */
	private static void initializeTextEditor() throws Exception {

		Scanner textFileParser = new Scanner(textFile);

		// Use a scanner to load the textFile
		while (textFileParser.hasNextLine()) {
			textList.add(textFileParser.nextLine());
		}

		textFileParser.close();

	}

	/**
	 * This method prompts the user for an input and accepts a string as an
	 * input from the user and returns it to the caller
	 */
	private static String acceptInput() {

		String input;
		printMessage(MESSAGE_PROMPT);
		input = s.nextLine();
		return input;
	}

	/**
	 * This method splits the user-inputed string into two parts, the first word
	 * will be treated as the command and the remaining as a string
	 */
	private static String[] cleanUpInput(String input) {

		Scanner stringParser = new Scanner(input);
		String[] commandAndString = new String[USER_COMMAND_AND_TEXT];
		String tempString = "";

		if (stringParser.hasNext()) { // Puts the first word into the first slot of the string array as the command
			commandAndString[USER_COMMAND] = stringParser.next();
		} else { // If user did not specify anything, default it to an empty string
			commandAndString[USER_COMMAND] = "";
		}

		// Determine if the next input is trivial or not
		// Put the remaining input into tempString if any
		if (stringParser.hasNext()) {
			tempString = stringParser.nextLine();
		}

		if (tempString.equals("\n") || tempString.equals("")) { // Trivial 2nd argument when tempString is just a new line or is empty
			commandAndString[USER_TEXT] = "";
		} else { // Not trivial when it is anything else
			tempString = tempString.substring(1, tempString.length());
			commandAndString[USER_TEXT] = tempString;
		}

		stringParser.close();

		return commandAndString;
	}

	/**
	 * This method verifies the command and the number of arguments entered
	 */
	private static boolean isValidInput(String[] processedInput) {

		// Initialize the boolean to indicate if 2nd argument is trivial
		boolean secondEntryIsTrivial = true;

		// If 2nd argument is only an empty string.
		if (processedInput[USER_TEXT].equals("")) {
			secondEntryIsTrivial = true;
		} else if (!processedInput[USER_TEXT].equals("")) { // Else if 2nd argument is
			// not just an empty string.
			secondEntryIsTrivial = false;
		}

		if (isCommandAndArgumentsCorrect(processedInput, secondEntryIsTrivial)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method determine if the command and number of arguments are correct
	 * Below is a summary of commands and number of arguments it requires.
	 * "display", "clear", "save" - 0 argument required 
	 * "add", "delete" - 1 argument required 
	 * Thus this method check the input based on the requirement 
	 * and returns a boolean to indicate if the input meets the specifications.
	 */
	private static boolean isCommandAndArgumentsCorrect(String[] processedInput, boolean secondEntryIsTrivial) {
		if (((processedInput[0].equals("add") || processedInput[0].equals("delete")) && !secondEntryIsTrivial)
				|| ((processedInput[0].equals("display") || processedInput[0].equals("clear")
						|| processedInput[0].equals("exit")) && secondEntryIsTrivial)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method takes in the processed and VERIFIED input then executes the
	 * corresponding commands
	 */
	private static void executeInput(String[] verifiedInput) throws Exception {

		if (verifiedInput[USER_COMMAND].equals("add")) {
			addToTextList(verifiedInput[USER_TEXT]);
		} else if (verifiedInput[USER_COMMAND].equals("delete")) {
			deleteFromTextList(verifiedInput[USER_TEXT]);
		} else if (verifiedInput[USER_COMMAND].equals("display")) {
			displayTextList();
		} else if (verifiedInput[USER_COMMAND].equals("clear")) {
			clearTextList();
		} else if (verifiedInput[USER_COMMAND].equals("exit")) {
			exitTextBuddy();
		}
	}

	/**
	 * This method appends the following line to the end of the arraylist
	 * textList
	 */
	private static void addToTextList(String input) throws Exception {

		textList.add(input);

		// Print success statement
		printMessage(String.format(MESSAGE_ADD_SUCCESS, fileName, input));
	}

	/**
	 * This method deletes the specified line of text from the arraylist
	 */
	private static void deleteFromTextList(String index) throws Exception {

		String deletedString = "";

		if (isValidToDelete(index)) {
			// Index is adjusted for arraylist
			deletedString = textList.remove(Integer.parseInt(index) - 1);

			// Print success statement
			printMessage(String.format(MESSAGE_DELETE_SUCCESS, fileName, deletedString));
		}
	}

	/**
	 * This method checks if the index the user specified is within a valid
	 * range of index to be deleted from the arraylist
	 */
	private static boolean isValidToDelete(String indexToDelete) {

		int indexInInt = 0;

		try {
			indexInInt = Integer.parseInt(indexToDelete);
		} catch (NumberFormatException a) {
			printMessage(MESSAGE_INVALID_INDEX);
			return false;
		}

		// Negative or index 0 is invalid since index starts with 1
		if (indexInInt <= 0 || indexInInt > textList.size()) {
			printMessage(MESSAGE_INVALID_INDEX);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method iterate through the arraylist called textList and prints out
	 * all the entries
	 */
	private static void displayTextList() throws Exception {

		// If the textlist is not empty then print everything
		if (textList.size() > EMPTY) {
			for (int i = 0; i < textList.size(); i++) {
				printMessage(i+1+"."+textList.get(i)+"\n");
			}
		} else { // Else print error message
			printMessage(String.format(MESSAGE_EMPTY_DISPLAY, fileName));
		}
	}

	private static PrintWriter makeAPrintWriter(File toBeWrittenOn,
			boolean append) throws Exception {

		FileWriter fw = new FileWriter(toBeWrittenOn, append);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		return pw;
	}

	/**
	 * This method deletes all entries from the arraylist textList
	 */
	private static void clearTextList() throws Exception {
		textList = new ArrayList<String>();

		// Print success statement
		printMessage(String.format(MESSAGE_CLEAR_SUCCESS, fileName));
	}

	/**
	 * This method copies the contents of textList over to the textFile
	 */
	private static void saveToTextFile() throws Exception {

		PrintWriter pw = makeAPrintWriter(textFile, false);

		for (String i : textList) {
			pw.println(i);
		}

		pw.close();

		// Print success statement
		printMessage(String.format(MESSAGE_SAVE_SUCCESS, fileName));
	}

	/**
	 * This method saves the changes to textFile and exits textBuddy
	 */
	private static void exitTextBuddy() throws Exception {

		saveToTextFile();
		printMessage(MESSAGE_EXIT);
	}

	private static void cleanUp() {
		s.close();
	}
	
	private static void printMessage(String message) {
		System.out.print(message);
	}
}












