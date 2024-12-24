import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Program to make an output file that consists of a table of words and counts
 * in alphabetical order from a given input file for the most frequent words in
 * a file using Java Collections Framework.
 *
 * @author Katie Beatty, Kierra Smith, and Maggie Tiernan
 *
 *
 */
public final class TagCloudGenerator {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private TagCloudGenerator() {
    }

    /**
     * Comparator to sort by frequency in descending order.
     */
    private static class ByFrequency implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> p1, Map.Entry<String, Integer> p2) {
            //Descending order
            return Integer.compare(p2.getValue(), p1.getValue());
        }
    }

    /**
     * Comparator to sort by alphabetical order.
     */
    private static class ByAlphabet implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> p1, Map.Entry<String, Integer> p2) {
            //Alphabetical order
            return p1.getKey().compareToIgnoreCase(p2.getKey());
        }
    }

    /**
     * Processes the input file and populates the word count map.
     *
     * @param inputFile
     *            the SimpleReader for the input file
     * @param separators
     *            the set of separator characters
     * @param wordCounts
     *            the map to store word counts
     */
    public static void processInputFile(String inputFile, Set<Character> separators,
            Map<String, Integer> wordCounts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            // Read each line from the input file until there are no more lines
            while ((line = reader.readLine()) != null) {

                // Convert the line to lowercase to ensure case-insensitivity
                line = line.toLowerCase();
                int position = 0;
                while (position < line.length()) {
                    // Extract the next word or separator from the line
                    String token = nextWordOrSeparator(line, position, separators);
                    if (!separators.contains(token.charAt(0))) {

                        //Increment the word's count in the map, or add it with 1
                        wordCounts.put(token, wordCounts.getOrDefault(token, 0) + 1);
                    }

                    // Move position forward by the length of the token
                    position += token.length();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Generates the HTML for the tag cloud.
     *
     * @param htmlOut
     *            the SimpleWriter for the output file
     * @param inputFile
     *            the name of the input file
     * @param sortedWords
     *            the sorted words and their counts
     * @param minCount
     *            the minimum count of the words
     * @param maxCount
     *            the maximum count of the words
     *
     */
    public static void generateTagCloudHtml(PrintWriter htmlOut, String inputFile,
            List<Map.Entry<String, Integer>> sortedWords, int minCount, int maxCount) {
        // Write the HTML header
        htmlOut.println("<html>");
        htmlOut.println("<head>");
        htmlOut.println("<title>Top " + sortedWords.size() + " words in " + inputFile
                + "</title>");
        htmlOut.println(
                "<link href=\"https://cse22x1.engineering.osu.edu/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        htmlOut.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        htmlOut.println("</head>");
        htmlOut.println("<body>");
        htmlOut.println(
                "<h2>Top " + sortedWords.size() + " words in " + inputFile + "</h2>");
        htmlOut.println("<hr>");
        htmlOut.println("<div class=\"cdiv\">");
        htmlOut.println("<p class=\"cbox\">");

        // Write each word with appropriate font size
        for (Map.Entry<String, Integer> pair : sortedWords) {
            int fontSize;
            if (minCount == maxCount) {
                // Default font size
                fontSize = 11;
            } else {
                //Font size based on word frequency
                fontSize = 11
                        + (37 * (pair.getValue() - minCount) / (maxCount - minCount));
            }
            htmlOut.println("<span style=\"cursor:default\" class=\"f" + fontSize
                    + "\" title=\"count: " + pair.getValue() + "\">" + pair.getKey()
                    + "</span>");
        }

        // Write the HTML footer
        htmlOut.println("</p>");
        htmlOut.println("</div>");
        htmlOut.println("</body>");
        htmlOut.println("</html>");
    }

    /**
     * Finds the next word or separator string in the text.
     *
     * @param text
     *            the text to process
     * @param position
     *            the starting position
     * @param separators
     *            the set of separator characters
     * @return the next word or separator string
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        int end = position;
        boolean isSeparator = separators.contains(text.charAt(end));

        while (end < text.length()
                && separators.contains(text.charAt(end)) == isSeparator) {
            end++;
        }
        return text.substring(position, end);
    }

    /**
     * Main method to run the Tag Cloud Generator.
     *
     * @param args
     *            command-line arguments
     */
    public static void main(String[] args) {
        // Initialize input/output streams
        Scanner console = new Scanner(System.in);

        // User input for file paths and word count (N)
        System.out.print("Enter input file path: ");
        String inputFile = console.nextLine();
        System.out.print("Enter output file path: ");
        String outputFile = console.nextLine();
        System.out.print("Enter number of words for tag cloud: ");
        int n = Integer.parseInt(console.nextLine());

        /// Initialize components
        Map<String, Integer> wordCounts = new HashMap<>();

        //Define Separators
        Set<Character> separators = new HashSet<>(
                Arrays.asList('-', '.', ',', ';', ':', '!', '?', '\"', '(', ')', '`', '[',
                        ']', '{', '}', '<', '>', ' ', '\t', '\n', '\r'));

        try (PrintWriter htmlOut = new PrintWriter(new FileWriter(outputFile))) {
            // Process input file
            processInputFile(inputFile, separators, wordCounts);

            // Sort words by frequency
            List<Map.Entry<String, Integer>> byFrequency = new ArrayList<>(
                    wordCounts.entrySet());
            Collections.sort(byFrequency, new ByFrequency());

            // Extract top N words and sort them alphabetically
            List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>();
            for (int i = 0; i < Math.min(n, byFrequency.size()); i++) {
                sortedWords.add(byFrequency.get(i));
            }
            Collections.sort(sortedWords, new ByAlphabet());

            // Calculate minCount and maxCount
            int minCount = Integer.MAX_VALUE;
            int maxCount = Integer.MIN_VALUE;
            for (Map.Entry<String, Integer> pair : sortedWords) {
                int frequency = pair.getValue();
                if (frequency < minCount) {
                    minCount = frequency;
                }
                if (frequency > maxCount) {
                    maxCount = frequency;
                }
            }

            // Generate HTML
            generateTagCloudHtml(htmlOut, inputFile, sortedWords, minCount, maxCount);

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        } finally {
            console.close();
        }

    }
}
