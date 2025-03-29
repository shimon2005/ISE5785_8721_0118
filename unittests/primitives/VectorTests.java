package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Vector} class.
 */
class VectorTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    private static final Vector V1 = new Vector(1, 2, 3);
    private static final Vector V2 = new Vector(-4, -5, -6);
    private static final Vector V3 = new Vector(3, -6, 3);
    private static final Vector V1_OPPOSITE = new Vector(-1, -2, -3);
    private static final Vector V_ZERO_LENGTH_3 = new Vector(0, 3, 0);

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void constructor3Parameters() {
        // ============ Equivalence Partitions Tests ==============

        // Check the components of the vector
        assertEquals(1, V1.xyz.d1(), DELTA, "X component is incorrect after constructor");
        assertEquals(2, V1.xyz.d2(), DELTA, "Y component is incorrect after constructor");
        assertEquals(3, V1.xyz.d3(), DELTA, "Z component is incorrect after constructor");

        // =============== Boundary Values Tests ==================

        // TC10: Zero vector should throw exception
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0),
                "Vector constructor for zero vector should throw exception");
    }


    /**
     * Test method for {@link primitives.Vector#Vector(Double3)}.
     */
    @Test
    void constructor1Parameter() {
        // ============ Equivalence Partitions Tests ==============

        // Create a vector using the Double3 constructor
        Vector v = new Vector(new Double3(1, 2, 3));

        // Check the components of the vector
        assertEquals(1, v.xyz.d1(), DELTA, "X component is incorrect after constructor");
        assertEquals(2, v.xyz.d2(), DELTA, "Y component is incorrect after constructor");
        assertEquals(3, v.xyz.d3(), DELTA, "Z component is incorrect after constructor");

        // =============== Boundary Values Tests ==================

        // TC10: Zero vector should throw exception
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO),
                "Vector constructor for zero vector should throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Dot product of orthogonal vectors should be zero
        assertEquals(0, V1.dotProduct(V3), "dotProduct() for orthogonal vectors should be zero");

        // TC02: Dot product of V1 and V2, check calculated value
        assertEquals(-32, V1.dotProduct(V2), "dotProduct() wrong result for V1 and V2");
    }

    /**
     * Test method for {@link primitives.Vector#add(Vector)}.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding two vectors, general case
        assertEquals(new Vector(-3, -3, -3), V1.add(V2), "add() wrong result for V1 + V2");

        // =============== Boundary Values Tests ==================

        // TC10: Adding a vector to its negative should throw exception (result zero vector)
        assertThrows(IllegalArgumentException.class, () -> V1.add(V1_OPPOSITE),
                "add() for vector and its opposite should throw exception");
    }

    /**
     * Test method for vector subtraction (even though there is no such method in vector and it will use the method from point)
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtracting two general vectors
        assertEquals(new Vector(5, 7, 9), V1.subtract(V2), "subtract() wrong result for V1 - V2");

        // =============== Boundary Values Tests ==================

        // TC10: Subtracting a vector from itself should throw exception (result zero vector)
        assertThrows(IllegalArgumentException.class, () -> V1.subtract(V1),
                "subtract() for vector and itself should throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Scaling vector by positive number
        assertEquals(new Vector(2, 4, 6), V1.scale(2), "scale() wrong result for positive scalar");

        // =============== Boundary Values Tests ==================

        // TC10: Scaling by zero should throw exception (result zero vector)
        assertThrows(IllegalArgumentException.class, () -> V1.scale(0),
                "scale() by zero should throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Cross product of V1 and V3
        Vector vr = V1.crossProduct(V3);

        // Check length
        assertEquals(V1.length() * V3.length(), vr.length(), DELTA,
                "crossProduct() wrong length");

        // Check orthogonality
        assertEquals(0, vr.dotProduct(V1), DELTA, "crossProduct() result is not orthogonal to V1");
        assertEquals(0, vr.dotProduct(V3), DELTA, "crossProduct() result is not orthogonal to V3");

        // =============== Boundary Values Tests ==================

        // TC10: Cross product of parallel vectors should throw exception
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(V1_OPPOSITE),
                "crossProduct() for parallel vectors should throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check length squared of V1 (1^2 + 2^2 + 3^2 = 14)
        assertEquals(14, V1.lengthSquared(), DELTA, "lengthSquared() wrong value for V1");

        // TC02: Check length squared of V_ZERO_LENGTH_3 (0^2 + 3^2 + 0^2 = 9)
        assertEquals(9, V_ZERO_LENGTH_3.lengthSquared(), DELTA, "lengthSquared() wrong value for V_ZERO_LENGTH_3");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Length of V1 (sqrt(14))
        assertEquals(Math.sqrt(14), V1.length(), DELTA, "length() wrong value for V1");

        // TC02: Length of V_ZERO_LENGTH_3 (3)
        assertEquals(3, V_ZERO_LENGTH_3.length(), DELTA, "length() wrong value for V_ZERO_LENGTH_3");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normalizing V1 should result in unit vector (length = 1)
        Vector u = V1.normalize();
        assertEquals(1, u.length(), DELTA, "normalize() result is not unit vector");

        // Check that u is parallel to V1 (cross product should throw)
        assertThrows(IllegalArgumentException.class, () -> V1.crossProduct(u),
                "cross product() of parallel vectors should throw exception");

        // Check that u points in the same direction (dot product > 0)
        assertTrue(V1.dotProduct(u) > 0, "normalize() vector points in opposite direction");
    }
}
