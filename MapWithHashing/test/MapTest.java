import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Teng.Zhang & Kierra.Smith
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     * Tests the no-argument constructor.
     */
    @Test
    public void noArgConstructorTest() {
        // Create instances
        Map<String, String> map = this.constructorTest();
        Map<String, String> mapExpected = this.constructorRef();
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * test add element to an empty map.
     */
    @Test
    public void testAddEmpty() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest();
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1");
        // Call method
        map.add("A", "1");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Add a pair of element to map which is not empty.
     */
    @Test
    public void testAdd() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call method
        map.add("C", "3");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Add more than a pair of elements to the map.
     */
    @Test
    public void testAdMany() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call methods
        map.add("B", "2");
        map.add("C", "3");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests remove only one pair of element.
     */
    @Test
    public void testRemoveOne() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2");
        // Call method
        map.remove("C");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Test remove more than one element.
     */
    @Test
    public void testRemoveMany() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("C", "3");
        // Call methods
        map.remove("A");
        map.remove("B");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Test remove any.
     */
    @Test
    public void testRemoveAny() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call method
        Map.Pair<String, String> test = map.removeAny();
        // Remove corresponding element from reference
        mapExpected.remove(test.key());
        // Assert equality
        assertEquals(map, mapExpected);

    }

    /**
     * Test the value of key is correct.
     */
    @Test
    public void testValueMultiple() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call methods
        String testValue = map.value("A");
        String expected = "1";
        // Assert equality
        assertEquals(testValue, expected);
    }

    /**
     * Test map doesn't have keys.
     */
    @Test
    public void testMapNoHasKeys() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("X", "1", "Y", "2",
                "Z", "3");
        // Call methods
        boolean hasKey1 = map.hasKey("A");
        boolean hasKey2 = map.hasKey("B");
        // Assert true
        assertTrue(!hasKey1);
        assertTrue(!hasKey2);
    }

    /**
     * Test map has keys.
     */
    @Test
    public void testMapHasKeys() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call methods
        boolean hasKey1 = map.hasKey("A");
        boolean hasKey2 = map.hasKey("B");
        // Assert true
        assertTrue(hasKey1);
        assertTrue(hasKey2);
    }

    /**
     * Test the size of empty map.
     */
    @Test
    public void testSizeEmpty() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest();
        // Create reference size
        int valueExpected = 0;
        // Assert equality
        assertEquals(map.size(), valueExpected);
    }

    /**
     * Test the size of an non-empty map.
     */
    @Test
    public void testSizeMultiple() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Create reference size
        int expectedSize = 3;
        // Assert equality
        assertEquals(map.size(), expectedSize);
    }

}
