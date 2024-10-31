import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TooManyListenersException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.IOException;

public class WordCounter {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a file or text to process");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose option (1 for file, 2 for text): ");
        int option = scanner.nextInt();

        try {
            if (option == 1) {
                StringBuffer fileText = processFile(args[0]);
                String stopWord = args.length > 1 ? args[1] : null;
                int count = processText(fileText, stopWord);
                System.out.println("Found " + count + " words.");
            } else if (option == 2) {
                StringBuffer text = new StringBuffer(args[0]);
                String stopWord = args.length > 1 ? args[1] : null;
                int count = processText(text, stopWord);
                System.out.println("Found " + count + " words.");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
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
            throw new TooSmallText("Only found " + wordCount + " words.");
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