import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.IOException;

public class WordCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {
            System.out.println("Enter valid option (1 or 2): ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                if (option == 1) {
                    StringBuffer fileText = processFile(args[0]);
                    String stopWord = args.length > 1 ? args[1] : null;
                    int wordCount = processText(fileText, stopWord);
                    System.out.println("Found " + wordCount + " words.");
                    break;
                } else {
                    StringBuffer text = new StringBuffer(args[0]);
                    String stopWord = args.length > 1 ? args[1] : null;
                    int wordCount = processText(text, stopWord);
                    System.out.println("Found " + wordCount + " words.");
                    break;
                }

            } catch (InvalidStopwordException e) {
                System.out.println("Please enter a valid stop word: ");
                String newStopWord = scanner.nextLine();
                if (option == 1) {
                    StringBuffer fileText = processFile(args[0]);
                    String stopWord = args.length > 1 ? args[1] : null;
                    int wordCount = processText(fileText, newStopWord);
                    System.out.println("Found " + wordCount + " words.");
                    break;
                } else {
                    StringBuffer text = new StringBuffer(args[0]);
                    String stopWord = args.length > 1 ? args[1] : null;
                    int wordCount = processText(text, stopWord);
                    System.out.println("Found " + wordCount + " words.");
                    break;
                }
            }
        }
    }

    // How do we handle when a user enters an invalid option?
    // Try catch block I think, but I don't know how to just make them choose a
    // valid option val.
    // Need to also check if there is a second arg which should be a stopword.

    // So after we verify valid option and check if it has a stopword, we call
    // either processFile or processText based on value of option?

    }

    public static int processText(StringBuffer text, String stopWord) throws InvalidStopwordException, TooSmallText {
        int wordCount = 0;
        boolean foundStopWord = false;
        // Need try catch blocks somewhere here to handle exceptions
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");

        Matcher regexMatcher = regex.matcher(text);

        while (regexMatcher.find()) {
            wordCount++;
            if (stopWord != null && regexMatcher.group().equals(stopWord)) {
                foundStopWord = true;
                break;
            }
        }

        if (wordCount < 5) {
            throw new TooSmallText("TooSmallText: Only found " + wordCount + " words.");
        }

        if (!foundStopWord && stopWord != null) {
            throw new InvalidStopwordException("InvalidStopwordException: Couldn't find stopword: " + stopWord);
        }

        return wordCount;
    }

    public static StringBuffer processFile(String path) {
        // Define a buffer that we will return
        StringBuffer result = new StringBuffer();

        try {
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
                throw new EmptyFileException("EmptyFileException: " + path + " was empty");
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