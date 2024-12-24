import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAdd1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "");
        m.add("");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAdd2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "apple",
                "orange");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "apple", "orange", "banana");
        m.add("banana");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "apple");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.removeFirst();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirst1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "1",
                "2", "3");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "2", "3");
        m.removeFirst();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirst2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "1",
                "2");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "2");
        m.removeFirst();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeTrue1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "one",
                "two");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "one", "two");
        boolean checkM = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(checkM, true);
    }

    public final void testIsInInsertionModeTrue2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        boolean checkM = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(checkM, true);
    }

    @Test
    public final void testIsInInsertionModeFalse1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "one",
                "two");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "one", "two");
        boolean checkM = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(checkM, m);
    }

    @Test
    public final void testIsInInsertionModeFalse2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        boolean checkM = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(checkM, false);
    }

    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        mExpected.changeToExtractionMode();
        m.changeToExtractionMode();
        assertEquals(mExpected, m);

    }

    @Test
    public final void testChangeToExtractionMode1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "one",
                "two");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "one", "two");
        mExpected.changeToExtractionMode();
        m.changeToExtractionMode();
        assertEquals(mExpected, m);

    }

    @Test
    public final void testSizeExtractionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "");
        assertEquals(mExpected.size(), m.size());

    }

    @Test
    public final void testSizeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        assertEquals(mExpected.size(), m.size());

    }

    @Test
    public final void testSize1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "apple");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "apple");
        assertEquals(mExpected.size(), m.size());

    }

    @Test
    public final void testSize2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "apple",
                "orange");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "apple", "orange");
        assertEquals(mExpected.size(), m.size());

    }

    @Test
    public final void testOrderEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        assertEquals(ORDER, m.order());

    }

    @Test
    public final void testOrder1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "1",
                "2", "3");
        assertEquals(ORDER, m.order());

    }

    @Test
    public final void testOrder2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "8");
        assertEquals(ORDER, m.order());

    }

    @Test
    public final void testOrder3() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "8",
                "2", "7", "6", "1");
        assertEquals(ORDER, m.order());

    }

}
