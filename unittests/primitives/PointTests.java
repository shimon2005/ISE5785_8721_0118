package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Point} class.
 */
class PointTests {

    private static final Point P1 = new Point(1, 2, 3);
    private static final Point P2 = new Point(2, 3, 4);
    private static final Point P3 = new Point(1, 2, 6);
    private static final Vector V1 = new Vector(1, 1, 1);
    private static final Vector oppositeOfP1 = new Vector(-1, -2, -3);
    private static final double DELTA = 1e-10;

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtracting P1 from P2 should give vector V1
        assertEquals(V1, P2.subtract(P1), "subtract() wrong result for P2 - P1");

        // =============== Boundary Values Tests ==================

        // TC10: Subtracting point from itself should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> P1.subtract(P1), "subtract() point minus itself should throw exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding V1 to P1 should return P2
        assertEquals(P2, P1.add(V1), "add() wrong result for P1 + V1");

        // =============== Boundary Values Tests ==================

        // TC10: Adding vector that is the opposite of point coordinates should return the ZERO point
        assertEquals(Point.ZERO, P1.add(oppositeOfP1), "add() wrong result for P1 + (-P1) to get ZERO");
    }


    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Distance squared between P1 and P3 is 9
        assertEquals(9, P1.distanceSquared(P3), DELTA, "distanceSquared() wrong result between P1 and P3");

        // TC02: Distance squared between P3 and P1 is symmetric and should be 9
        assertEquals(9, P3.distanceSquared(P1), DELTA, "distanceSquared() wrong result between P3 and P1");

        // =============== Boundary Values Tests ==================

        // TC10: Distance squared between P1 and itself should be zero
        assertEquals(0, P1.distanceSquared(P1), DELTA, "distanceSquared() for point to itself should be zero");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Distance between P1 and P3 is 3
        assertEquals(3, P1.distance(P3), DELTA, "distance() wrong result between P1 and P3");

        // TC02: Distance between P3 and P1 is symmetric and should be 3
        assertEquals(3, P3.distance(P1), DELTA, "distance() wrong result between P3 and P1");

        // =============== Boundary Values Tests ==================

        // TC10: Distance between P1 and itself should be zero
        assertEquals(0, P1.distance(P1), DELTA, "distance() for point to itself should be zero");
    }

}
