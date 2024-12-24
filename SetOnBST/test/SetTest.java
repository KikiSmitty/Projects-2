import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Test constructor that isn't empty.
     */
    @Test
    public final void testWithArgConstructor() {
        Set<String> set = this.createFromArgsTest("Kiki", "Teng", "Rob");
        Set<String> expectedSet = this.createFromArgsTest("Kiki", "Teng",
                "Rob");
        assertEquals(expectedSet, set);
    }

    /**
     * Test constructor with no args.
     */
    @Test
    public final void testNoArgConstructor() {
        Set<String> set = this.constructorTest();
        Set<String> expectedSet = this.constructorRef();
        assertEquals(expectedSet, set);
    }

    /**
     * Tests add element to empty set
     */
    @Test
    public void testAddEmpty() {
        /*
         * Create empty tree
         */
        Set<String> n = this.createFromArgsTest();
        Set<String> nExpected = this.createFromArgsRef("a");
        /*
         * add
         */
        n.add("a");

        assertEquals(n, nExpected);
    }

    //add element smaller than root
    @Test
    public void testAddLess() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "c", "a");
        n.add("a");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
    }

    //add element larger than root
    @Test
    public void testAddLarger() {
        /*
         * Create
         */
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("a", "b", "c", "d");
        /*
         * add element larger than root
         */
        n.add("d");

        assertEquals(n, nExpected);
    }

    //empty a tree with only one element
    @Test
    public void testRemoveToEmpty() {
        //initialize a tree with only one element
        Set<String> n = this.createFromArgsTest("a");
        Set<String> nExpected = this.createFromArgsRef();
        //remove the element
        String removed = n.remove("a");

        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    //remove element larger than root
    @Test
    public void testRemoveLarger() {
        //create a tree which root is a
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("a", "c");

        //remove element larger than root
        String removed = n.remove("b");
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    //remove the root of a tree
    @Test
    public void testRemoveRoot() {
        //create a tree which root is "a"
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "c");

        //remove the root
        String removed = n.remove("a");
        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    //remove element smaller than root
    @Test
    public void testRemoveLess() {
        //create a tree which root is "b"
        Set<String> n = this.createFromArgsTest("b", "a");
        Set<String> nExpected = this.createFromArgsRef("a");

        //remove "a"
        String removed = n.remove("b");
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    //remove element smaller than root 2
    @Test
    public void testRemoveSmaller() {
        //create a tree which root is "b"
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "c");

        String removed = n.remove("a");
        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    //remove the middle one
    @Test
    public void testRemoveMiddle() {
        //initialize a tree which root is "b"
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("c", "a");
        //remove the middle one
        String removed = n.remove("b");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    //remove the one larger than root
    @Test
    public void testRemoveLarger2() {
        //initialize a tree which root is "b"
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "a");

        //remove larger one
        String removed = n.remove("c");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "c");
    }

    //test removed element
    @Test
    public void testRemoveEmpty() {
        //create a tree with only one element in it
        Set<String> n = this.createFromArgsTest("b");
        Set<String> nExpected = this.createFromArgsRef("b");

        String removed = n.removeAny();
        boolean contain = nExpected.contains(removed);
        String temp = nExpected.remove("b");

        assertEquals(true, contain);
        assertEquals(n, nExpected);
        assertEquals(removed, temp);
    }

    //Tests removeAny
    @Test
    public void testRemoveAny() {
        //create sets
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        String removed = n.removeAny();
        int eSize = n.size();
        assertEquals(2, eSize);

    }

    //Tests removeAny and Compare
    @Test
    public void testRemoveAnyTwoSets() {
        //create sets
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "a", "c");
        String any = n.removeAny();
        boolean contain = nExpected.contains(any);
        assertEquals(true, contain);
    }

    //Tests removeAny in two same sets, the elements removed should be the same
    @Test
    public void testRemoveAnyTwoSets2() {
        //create sets
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "a", "c");
        String actual = n.removeAny();
        String expected = nExpected.removeAny();
        assertEquals(actual, expected);
    }

    //A set of one element contains test
    @Test
    public void testContainsOne() {
        //create a set only contain one element
        Set<String> n = this.createFromArgsTest("a");

        boolean contain = n.contains("a");
        assertEquals(true, contain);
    }

    /**
     * Tests contains element smaller than root
     */
    @Test
    public void testContainsSmaller() {
        //create a tree root is b
        Set<String> n = this.createFromArgsTest("b", "a", "c");

        boolean contain = n.contains("a");
        assertEquals(true, contain);
    }

    //Tests contains element larger than root
    @Test
    public void testContainsLarger() {
        //create a tree root is b
        Set<String> n = this.createFromArgsTest("b", "a", "c");

        boolean contain = n.contains("c");
        assertEquals(true, contain);
    }

    //test contains root itself
    @Test
    public void testContainsRoot() {
        //create a tree root is b
        Set<String> n = this.createFromArgsTest("b", "a", "c");

        boolean contain = n.contains("b");
        assertEquals(true, contain);
    }

    //test doesn't contain an element
    @Test
    public void testNoContains() {
        //create a tree root is b
        Set<String> n = this.createFromArgsTest("a");

        boolean contain = n.contains("b");
        assertEquals(false, contain);

    }

    //test doesn't contain an element(more than one element)
    @Test
    public void testNoContains2() {
        //create a tree root is b
        Set<String> n = this.createFromArgsTest("b", "a", "c");

        boolean contain = n.contains("d");
        assertEquals(false, contain);
    }

    //test doesn't contain an element(empty)
    @Test
    public void testEmptyNoContains() {
        //create an empty set
        Set<String> n = this.createFromArgsTest();

        boolean contain = n.contains("d");
        assertEquals(false, contain);
    }

    //test size of an empty element
    @Test
    public void testSizeEmpty() {
        //create an empty set
        Set<String> n = this.createFromArgsTest();

        int eSize = 0;
        int nSize = n.size();
        assertEquals(eSize, nSize);
    }

    //test the size of set with only one element
    @Test
    public void testOneSize() {
        //create an element with only one element
        Set<String> n = this.createFromArgsTest("a");

        int size = 1;
        int eSize = n.size();
        assertEquals(eSize, size);
    }

    //test the size of elements more than one
    @Test
    public void testSizeMany() {
        //create a set with more than one element
        Set<String> n = this.createFromArgsTest("a", "b", "c");

        int size = 3;
        int eSize = n.size();

        assertEquals(eSize, size);
    }

}
