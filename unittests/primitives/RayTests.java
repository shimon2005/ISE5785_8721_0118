package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/*
 * Unit tests for {@link primitives.Ray} class.
 * This class contains tests for the Ray class methods.
 */
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

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void findClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Closest point is in the middle of the list
        List<Point> points = List.of(
                new Point(3, 0, 0),
                new Point(1, 0, 0), // closest
                new Point(5, 0, 0)
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points),
                "TC01: Wrong closest point when it's in the middle of the list");

        // =============== Boundary Values Tests ==================

        // TC10: Null list
        assertNull(ray.findClosestPoint(null),
                "TC10: Expected null when input list is null");

        // TC11: Closest point is the first in the list
        points = List.of(
                new Point(1, 0, 0), // closest
                new Point(3, 0, 0),
                new Point(5, 0, 0)
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points),
                "TC11: Wrong closest point when it's the first in the list");

        // TC12: Closest point is the last in the list
        points = List.of(
                new Point(5, 0, 0),
                new Point(3, 0, 0),
                new Point(1, 0, 0) // closest
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points),
                "TC12: Wrong closest point when it's the last in the list");
    }

}