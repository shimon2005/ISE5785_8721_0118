package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Triangle} class.
 */
class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     * This test checks that the normal vector returned by getNormal is correct.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check that the normal returned by getNormal is correct
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p1, p2, p3);

        // Expected normal (directly calculated based on the points)
        Vector expectedNormal = new Vector(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3));

        // Get the normal from the triangle
        Vector normal = triangle.getNormal(p1);

        // Check that the normal returned by getNormal is either expectedNormal or its opposite
        assertTrue(normal.equals(expectedNormal) || normal.equals(expectedNormal.scale(-1)),
                "Triangle normal returned by getNormal is incorrect");
    }

}
