package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Util;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Plane} class.
 */
class PlaneTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void constructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct plane creation, normal should be perpendicular to two vectors
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        // Compute vectors from the points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector normal = plane.getNormal();

        // Check that the normal is perpendicular to both vectors
        assertEquals(0, normal.dotProduct(v1),DELTA ,  "Plane normal is not perpendicular to v1");
        assertEquals(0, normal.dotProduct(v2),DELTA , "Plane normal is not perpendicular to v2");

        // Check that the normal is a unit vector
        assertTrue(Util.isZero(normal.length() - 1), "Plane normal is not a unit vector");


        // =============== Boundary Values Tests ==================

        // TC10: Two identical points (first and second) should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p1, p3),
                "Constructor should throw exception when first and second points are identical");

        // TC11: Two identical points (first and third) should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p2, p1),
                "Constructor should throw exception when first and third points are identical");

        // TC12: Two identical points (second and third) should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p2, p2),
                "Constructor should throw exception when second and third points are identical");

        // TC13: All points are identical should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p1, p1),
                "Constructor should throw exception when all points are identical");

        // TC14: Points are collinear (on the same line) should throw an exception
        Point collinearP1 = new Point(0, 0, 0);
        Point collinearP2 = new Point(1, 1, 1);
        Point collinearP3 = new Point(2, 2, 2); // Same direction, forming a line

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(collinearP1, collinearP2, collinearP3),
                "Constructor should throw exception when points are collinear");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal()}.
     * This test checks that the normal vector returned by getNormal is correct.
     */
    @Test
    void getNormalWithoutParameter() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check that the normal returned by getNormal is correct
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        // Expected normal (manual calculation based on the specific points)
        Vector expectedNormal = new Vector(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3));

        // Get the normal from the plane
        Vector normal = plane.getNormal();

        // Check that the normal returned by getNormal is correct
        assertTrue(normal.equals(expectedNormal) || normal.equals(expectedNormal.scale(-1)),
                "Plane normal returned by getNormal is incorrect");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point point)}.
     * This test checks that the normal vector returned by getNormal is correct.
     */
    @Test
    void getNormalWithParameter() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check that the normal returned by getNormal is correct
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        // Expected normal (manual calculation based on the specific points)
        Vector expectedNormal = new Vector(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3));

        // Get the normal from the plane, used p1 as a parameter (arbitrary decision)
        Vector normal = plane.getNormal(p1);

        // Check that the normal returned by getNormal is either expectedNormal or its opposite
        assertTrue(normal.equals(expectedNormal) || normal.equals(expectedNormal.scale(-1)),
                "Plane normal returned by getNormal is incorrect");
    }
}
