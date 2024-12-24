import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author Kierra Smith & Teng.Zhang
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    // TODO - add test cases for four constructors, multiplyBy10, divideBy10, isZero

    /**
     * Tests empty constructor.
     */
    @Test
    public void testConstructorEmpty() {
        // Create instances
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef();
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests integer constructor when input is zero
     */
    @Test
    public void testConstructorInteger() {
        // Create instances
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests integer constructor when input is normal one（positive integer)
     */
    @Test
    public void testConstructorInteger2() {
        // Create instances
        int i = 5;
        NaturalNumber n = this.constructorTest(i);
        NaturalNumber nExpected = this.constructorRef(i);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * constructor with String.
     */
    @Test
    public void testConstructorString() {
        // Create instances
        String s = "1";
        NaturalNumber n = this.constructorTest(s);
        NaturalNumber nExpected = this.constructorRef(s);
        // Assert
        assertEquals(n, nExpected);
    }

    /**
     * constructor with String zero.
     */
    @Test
    public void testConstructorStringZero() {
        // Create instances
        String s = "0";
        NaturalNumber n = this.constructorTest(s);
        NaturalNumber nExpected = this.constructorRef(s);
        // Assert
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with NaturalNumber.
     */
    @Test
    public void testConstructorNN() {
        // Create instances
        NaturalNumber i = new NaturalNumber1L(10);
        NaturalNumber n = this.constructorTest(i);
        NaturalNumber nExpected = this.constructorRef(i);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests multiplyBy10 with a rep initialized by empty
     */
    @Test
    public void testZeroMultiplyBy10() {
        // initialize the value
        NaturalNumber n = this.constructorTest();
        final int i = 5;
        NaturalNumber nExpected = this.constructorRef(i);
        // call the multiply-method
        n.multiplyBy10(i);
        // Assert equality
        assertEquals(nExpected, n);

    }

    /**
     * Tests multiplyBy10 of normal one
     */
    @Test
    public void testMultiplyBy10() {
        // instances
        NaturalNumber n = this.constructorTest(5);
        int i = 5;
        NaturalNumber nExpected = this.constructorRef(55);
        //use n to call the method
        n.multiplyBy10(i);
        // Assert equality
        assertEquals(nExpected, n);
    }

    /**
     * Tests divideBy10 when the initial value less than 10
     */
    @Test
    public void testDivideBy10() {
        //initialize the value
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(0);
        int i = 5;

        int iExpected = n.divideBy10();
        // Assert
        assertEquals(nExpected, n);
        assertEquals(iExpected, i);

    }

    /**
     * divideBy10 with zero.
     */
    @Test
    public void testDivideBy10Zero() {
        //initialize the value
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef(0);
        int iExpected = 0;

        int i = n.divideBy10();
        // Assert
        assertEquals(nExpected, n);
        assertEquals(iExpected, i);

    }

    /**
     * Tests divideBy10 with a normal value
     */
    @Test
    public void testDivideBy10EndRoutine() {
        // initialize the value
        NaturalNumber n = this.constructorTest(555);
        NaturalNumber nExpected = this.constructorRef(55);
        final int iExpected = 5;
        // Call method
        int i = n.divideBy10();
        // Assert
        assertEquals(nExpected, n);
        assertEquals(iExpected, i);

    }

    /**
     * Tests IsZero
     */
    @Test
    public void testIsZeroTrue() {
        // initialize the value
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef();
        // Assert equality
        //each of them should be zero
        assertEquals(n, nExpected);
        assertTrue(n.isZero());
        assertTrue(nExpected.isZero());
    }

    /**
     * When isZero is false
     */
    @Test
    public void testIsZeroFalse() {
        //initialize the value( not equal to zero)
        NaturalNumber n = this.constructorTest(5);
        NaturalNumber nExpected = this.constructorRef(5);
        // Assert equality/call methods
        assertEquals(n, nExpected);
        assertTrue(!n.isZero());
        assertTrue(!nExpected.isZero());
    }

}
