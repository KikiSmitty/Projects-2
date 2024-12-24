import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * Program to make an output file that consists of a table of words and counts
 * in alphabetical order from a given input file for the most frequent words in
 * a file.
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
    private static class ByFrequency implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> p1, Map.Pair<String, Integer> p2) {
            //Descending order
            return Integer.compare(p2.value(), p1.value());
        }
    }

    /**
     * Comparator to sort by alphabetical order.
     */
    private static class ByAlphabet implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> p1, Map.Pair<String, Integer> p2) {
            //Alphabetical order
            return p1.key().compareToIgnoreCase(p2.key());
        }
    }

    /**
     * Processes the input file and populates the word count map.
     *
     * @param in
     *            the SimpleReader for the input file
     * @param separators
     *            the set of separator characters
     * @param wordCounts
     *            the map to store word counts
     */
    public static void processInputFile(SimpleReader in, Set<Character> separators,
            Map<String, Integer> wordCounts) {
        while (!in.atEOS()) {
            String line = in.nextLine().toLowerCase();
            int position = 0;

            while (position < line.length()) {
                String token = nextWordOrSeparator(line, position, separators);
                if (!separators.contains(token.charAt(0))) {
                    if (wordCounts.hasKey(token)) {
                        int count = wordCounts.value(token);
                        wordCounts.replaceValue(token, count + 1);
                    } else {
                        wordCounts.add(token, 1);
                    }
                }
                position += token.length();
            }
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
    public static void generateTagCloudHtml(SimpleWriter htmlOut, String inputFile,
            SortingMachine<Map.Pair<String, Integer>> sortedWords, int minCount,
            int maxCount) {
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
        while (sortedWords.size() > 0) {
            Map.Pair<String, Integer> pair = sortedWords.removeFirst();
            int fontSize;
            if (minCount == maxCount) {
                //Default font size
                fontSize = 11;
            } else {
                //Font size based on word frequency
                fontSize = 11 + (37 * (pair.value() - minCount) / (maxCount - minCount));
            }
            htmlOut.println("<span style=\"cursor:default\" class=\"f" + fontSize
                    + "\" title=\"count: " + pair.value() + "\">" + pair.key()
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
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // User input for file paths and word count (N)
        out.print("Enter input file path: ");
        String inputFile = in.nextLine();
        out.print("Enter output file path: ");
        String outputFile = in.nextLine();
        out.print("Enter number of words for tag cloud: ");
        int n = Integer.parseInt(in.nextLine());

        // Initialize components
        SimpleReader fileReader = new SimpleReader1L(inputFile);
        SimpleWriter htmlOut = new SimpleWriter1L(outputFile);

        //Initialize the map for word counts and the set of separators
        Map<String, Integer> wordCounts = new Map1L<>();
        Set<Character> separators = new Set1L<>();

        // Define separators
        String separatorChars = ".,:;!?\"()[]{}<> \t\n\r";
        for (char c : separatorChars.toCharArray()) {
            separators.add(c);
        }

        // Process input file
        processInputFile(fileReader, separators, wordCounts);

        // Sort words by frequency
        SortingMachine<Map.Pair<String, Integer>> byFrequency = new SortingMachine1L<>(
                new ByFrequency());
        for (Map.Pair<String, Integer> pair : wordCounts) {
            byFrequency.add(pair);
        }
        byFrequency.changeToExtractionMode();

        // Extract top N word tracking
        SortingMachine<Map.Pair<String, Integer>> sortedWords = new SortingMachine1L<>(
                new ByAlphabet());
        int count = 0;
        while (count < n && byFrequency.size() > 0) {
            Map.Pair<String, Integer> pair = byFrequency.removeFirst();
            sortedWords.add(pair);
            count++;
        }
        sortedWords.changeToExtractionMode();

        // Calculate minCount and maxCount
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        for (Map.Pair<String, Integer> pair : sortedWords) {
            int frequency = pair.value();
            if (frequency < minCount) {
                minCount = frequency;
            }
            if (frequency > maxCount) {
                maxCount = frequency;
            }
        }

        // Generate HTML
        generateTagCloudHtml(htmlOut, inputFile, sortedWords, minCount, maxCount);

        // Close streams
        fileReader.close();
        htmlOut.close();
        in.close();
        out.close();
    }
}
