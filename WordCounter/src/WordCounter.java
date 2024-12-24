import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Simple HelloWorld program (clear of Checkstyle and SpotBugs warnings).
 *
 * @author Kierra Smith
 */
public final class WordCounter {
    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     * text[position, position + |nextWordOrSeparator|) and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     * entries(nextWordOrSeparator) intersection separators = {} and
     * (position + |nextWordOrSeparator| = |text| or
     * entries(text[position, position + |nextWordOrSeparator| + 1))
     * intersection separators /= {})
     * else
     * entries(nextWordOrSeparator) is subset of separators and
     * (position + |nextWordOrSeparator| = |text| or
     * entries(text[position, position + |nextWordOrSeparator| + 1))
     * is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";
        int end = position;
        //runs if character is a separator in the set
        if (separators.contains((text.charAt(position)))) {
            //runs to increment end
            while (text.length() != end
                    && separators.contains(text.charAt(end))) {
                end++;
            }
            // runs if character is not in separator set
        } else {
            //runs to increment end
            while (text.length() != end
                    && !separators.contains(text.charAt(end))) {
                end++;
            }
        }
        //return text
        return text.substring(position, end);
    }

    /**
     * Comparator to enable the usage of sort for following methods.
     *
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            //turns first letter to lower case
            String o1LowerCase = o1.toLowerCase();
            String o2LowerCase = o2.toLowerCase();
            //compare the strings
            return o1LowerCase.compareTo(o2LowerCase);
        }
    }

    /**
     * Makes a queue of keys and sorts the keys alphabetically.
     *
     * @param wordAndCount
     *            map used to store word paired with respective definition
     * @return keys are sorted alphabetically
     */

    private static Queue<String> sort(Map<String, Integer> wordAndCount) {
        //create new Queue
        Queue<String> order = new Queue1L<>();
        Comparator<String> aToZ = new StringLT();
        //for each wordPair in wordAndCount add the key to order
        for (Map.Pair<String, Integer> wordPair : wordAndCount) {
            order.enqueue(wordPair.key());
        }
        //sort the order Queue
        order.sort(aToZ);
        return order;
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     *
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "violation of: charSet is not null";
        for (int i = 0; i < str.length(); i++) {
            if (!charSet.contains(str.charAt(i))) {
                charSet.add(str.charAt(i));
            }
        }
    }

    /**
     * prints the ordered list in the html file.
     *
     * @param keys
     *            queue that stores the words in wordsAndCount alphabetically
     * @param wordAndCount
     *            map used to store word paired with respective definition
     * @param fileName
     *            file name input by user
     * @param html
     *            html file that everything prints to
     * @ensures that each term has an html file
     */
    private static void printOrderedList(Queue<String> keys, String fileName,
            Map<String, Integer> wordAndCount, SimpleWriter html) {
        //while loop print each word in alphabetical order into a table
        while (keys.length() > 0) {
            String word = keys.dequeue();
            html.println("<tr>");
            html.println("<td>" + word + "</td>");
            html.println("<td>" + wordAndCount.value(word) + "</td>");
            html.println("</tr>");
        }
    }

    /**
     * prints the header in the html file.
     *
     * @param fileName
     *            file name that in input by user
     *
     * @param html
     *            html file that everything prints to
     * @ensures that the header and file name is printed at the top of the html
     *          file
     */
    private static void header(String fileName, SimpleWriter html) {
        //clean up the main function code with this function
        html.println("<html><head><title> Words Counted in " + fileName
                + "</title>");
        html.println("</head><body>");
        html.println("<h1> Words Counted in " + fileName + "</h1>");

        html.println("<hr/>");
        html.println("<table border=\"1\"");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        //ask user for a txt file input
        out.println("input a txt file: ");
        String file = in.nextLine();
        //create a SimpleReader to read the file input
        SimpleReader readFile = new SimpleReader1L(file);
        //name your output file
        out.println("input a name for your output file: ");
        String fileName = in.nextLine();
        SimpleWriter html = new SimpleWriter1L(fileName);
        //create your Queue of keys
        Queue<String> keys = new Queue1L<>();
        //create your Map to store the word and its count
        Map<String, Integer> wordAndCount = new Map1L<>();
        //make a set for characters are not being counted
        Set<Character> charSet = new Set1L<Character>();
        generateElements(" \t, .-", charSet);
        //while loop to read through the whole file
        while (!readFile.atEOS()) {
            //read through each line in txt
            String line = readFile.nextLine();
            //change whole line to lower case
            line = line.toLowerCase();
            int position = 0;
            //while loop to get each individual word not including punctuation or spaces
            while (position < line.length()) {
                String word = nextWordOrSeparator(line, position, charSet);
                position += word.length();
                if (!charSet.contains((word.charAt(0)))) {
                    //update count value each time word appears
                    if (wordAndCount.hasKey(word)) {
                        int count = wordAndCount.value(word);
                        wordAndCount.replaceValue(word, count + 1);
                    } else {
                        wordAndCount.add(word, 1);
                    }
                }
            }
        }
        //sort the word in alphabetical order with the sort function
        keys = sort(wordAndCount);
        //html header
        header(fileName, html);
        //print the list
        printOrderedList(keys, fileName, wordAndCount, html);
        html.println("</body>");
        html.println("</html>");
        html.close();
        readFile.close();
        in.close();
        out.close();

    }
}
