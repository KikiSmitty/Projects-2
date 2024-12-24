import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Katie Beatty, Kierra Smith, and Maggie Tiernan
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer.isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF")
                : "" + "Violation of: <\"IF\"> is proper prefix of tokens";

        tokens.dequeue(); // Remove "IF"

        //Check if the condition following If is valid
        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                "Not a valid condition.");

        //Convert the condition and continue
        Condition c = parseCondition(tokens.dequeue());

        //Expect "THEN" to follow the condition
        Reporter.assertElseFatalError(tokens.dequeue().equals("THEN"),
                "THEN needs to follow the condition.");

        //Parse the main block for IF
        Statement bodyM = s.newInstance();
        bodyM.parseBlock(tokens);

        //Check if there is an ELSE block
        if (tokens.front().equals("ELSE")) {
            tokens.dequeue();
            Statement sElse = s.newInstance();
            sElse.parseBlock(tokens);
            s.assembleIfElse(c, bodyM, sElse);
        } else if (tokens.front().equals("END")) {
            s.assembleIf(c, bodyM);
        } else {
            Reporter.assertElseFatalError(false, "END or ELSE expected.");
        }

        //Final END and IF tokens to close the statment
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "End needs to follow your block.");
        Reporter.assertElseFatalError(tokens.dequeue().equals("IF"),
                "IF needs to follow END.");

    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE")
                : "" + "Violation of: <\"WHILE\"> is proper prefix of tokens";
        // Remove "WHILE"
        tokens.dequeue();

        //Check if the condition following the WHILE is vaild
        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                "Not a valid condition.");

        //Parse the condition and expect "DO" after
        Condition c = parseCondition(tokens.dequeue());
        Reporter.assertElseFatalError(tokens.dequeue().equals("DO"),
                "DO needs to follow the condition.");

        //Parse the body of WHILE loop
        Statement bodyM = s.newInstance();
        bodyM.parseBlock(tokens);

        //Assemble WHILE statement
        s.assembleWhile(c, bodyM);

        //Final END and WHILE tokens to close loop
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "End needs to follow your block.");
        Reporter.assertElseFatalError(tokens.dequeue().equals("WHILE"),
                "WHILE needs to follow END.");
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && Tokenizer.isIdentifier(tokens.front())
                : "" + "Violation of: identifier string is proper prefix of tokens";

        Reporter.assertElseFatalError(Tokenizer.isIdentifier(tokens.front()),
                "Expected identifier for CALL statement.");
        s.assembleCall(tokens.dequeue());

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0
                : "" + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        String keyword = tokens.front();
        boolean isValidCall = Tokenizer.isIdentifier(keyword);

        //Decide based on the keyword which parse method to call and use
        if (keyword.equals("IF")) {
            parseIf(tokens, this);

        } else if (keyword.equals("WHILE")) {
            parseWhile(tokens, this);

        } else if (isValidCall) {
            parseCall(tokens, this);

        } else {
            Reporter.assertElseFatalError(false,
                    "The block should contain valid instructions.");
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0
                : "" + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        this.clear();
        Statement s = this.newInstance();

        //Loop through the tokens in the block
        while (tokens.front().equals("IF") || tokens.front().equals("WHILE")
                || Tokenizer.isIdentifier(tokens.front())) {
            s.parse(tokens);
            this.addToBlock(this.lengthOfBlock(), s);
            s.clear();

        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
