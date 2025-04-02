package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    /**
     * Test method for {@link primitives.Ray#Ray(primitives.Point, primitives.Vector)}.
     */
    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============

        Ray ray = new Ray(new Point(1, 2, 3), new Vector(0, 1, 0));

        // TC01: Positive distance
        assertEquals(new Point(1, 3, 3), ray.getPoint(1), "getPoint() wrong result for positive distance");

        // TC02: Negative distance
        assertEquals(new Point(1, 1, 3), ray.getPoint(-1), "getPoint() wrong result for negative distance");

        // =============== Boundary Values Tests ==================

        // TC10: Distance is zero (should return the ray head)
        assertEquals(new Point(1, 2, 3), ray.getPoint(0), "getPoint() wrong result for zero distance");
    }

}