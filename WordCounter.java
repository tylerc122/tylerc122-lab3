import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.IOException;

public class WordCounter {
    public static void main(String[] args) {
        // Scanner stuff
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        // New valid option flag
        boolean validOption = false;

        // Loop until we have a valid option
        while (!validOption) {
            System.out.println("Choose option (1 for file, 2 for text): ");
            try {
                option = scanner.nextInt();
                if (option == 1 || option == 2) {
                    validOption = true;
                } else {
                    System.out.println("Invalid option, please enter either a 1 or a 2");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number: ");
            }
        }

        try {
            // If option one
            if (option == 1) {
                // Make new stringbuffer so we can pass it into our function
                StringBuffer fileText = processFile(args[0]);
                // If a stop word exists, it will be in args[1] so set it to that if it exists
                String stopWord = args.length > 1 ? args[1] : null;
                // Making sure we have a valid stop word
                boolean validStopWord = false;

                while (!validStopWord && (stopWord != null)) {
                    try {
                        int count = processText(fileText, stopWord);
                        System.out.println("Found " + count + " words.");
                        validStopWord = true;
                    } catch (InvalidStopwordException e) {
                        System.out.println(
                                "Invalid stop word, please enter a new stop word to continue or press enter to skip: ");
                        stopWord = scanner.nextLine();
                        if (stopWord.isEmpty()) {
                            stopWord = null;
                        }
                    }
                }
                if (stopWord == null) {
                    int count = processText(fileText, null);
                    System.out.println("Found " + count + " words.");
                }
            } else if (option == 2) {
                // Same ordeal here, just with file instead
                StringBuffer text = new StringBuffer(args[0]);
                String stopWord = args.length > 1 ? args[1] : null;
                int count = processText(text, stopWord);
                System.out.println("Found " + count + " words.");
            }
        } catch (

        Exception e) {
            System.out.println(e.toString());
        }
    }

    public static int processText(StringBuffer text, String stopWord) throws InvalidStopwordException, TooSmallText {
        // Doesn't account for stopword
        int fullCount = 0;
        // Accounts for stopword
        int wordCount = 0;
        boolean foundStopWord = false;
        // Need try catch blocks somewhere here to handle exceptions
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");

        Matcher noStopWordRegexMatcher = regex.matcher(text);

        while (noStopWordRegexMatcher.find()) {
            fullCount++;
        }

        Matcher stopWordRegexMatcher = regex.matcher(text);

        while (stopWordRegexMatcher.find()) {
            wordCount++;
            if (stopWord != null && stopWordRegexMatcher.group().equals(stopWord)) {
                foundStopWord = true;
                break;
            }
        }

        if (fullCount < 5) {
            throw new TooSmallText("Only found " + fullCount + " words.");
        }

        if (foundStopWord == false && stopWord != null) {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopWord);
        }

        return wordCount;
    }

    public static StringBuffer processFile(String path) throws EmptyFileException {
        // Define a buffer that we will return
        StringBuffer result = new StringBuffer();

        try {
            // Define a file to see if it's empty
            File file = new File(path);

            // Why is this check not working?
            if (file.length() == 0) {
                throw new EmptyFileException(path + " was empty");
            }
            // Define a file reader
            FileReader fileReader = new FileReader(path);

            // Define a bufferedReader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Reads the first line
            String currLine = bufferedReader.readLine();

            // Check if the first line is invalid
            if (currLine == null) {
                // If it is, close our readers
                bufferedReader.close();
                fileReader.close();
                // Throw empty file exception
                throw new EmptyFileException(path + " was empty");
            }
            // If it is valid, loop thru, appending each line to our string buffer result.
            while (currLine != null) {
                result.append(currLine);
                currLine = bufferedReader.readLine();
            }
            // Close our readers at the end
            bufferedReader.close();
            fileReader.close();

            // Catches IOException which includes invalid file names and such
        } catch (IOException ex) {
            // If we end up having one of these exceptions, we need a valid file name from
            // the user
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a valid file name: ");
            // Try again with the new file
            return processFile(scanner.nextLine());
        }
        return result;
    }
}