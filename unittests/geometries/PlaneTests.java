package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

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

    /**
     * Test method for {@link geometries.Plane#findIntersections(Ray)}.
     * This test checks the intersection points of a ray with the plane.
     */
    @Test
    void findIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray starts outside and intersects the plane
        assertEquals(List.of(new Point(1.0 / 3, 1.0 / 3, 1.0 / 3)),
                plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-2, -2, -2))),
                "Ray starts outside and intersects the plane, should return intersection point");

        // TC02: Ray starts outside and does not intersect the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 1, 1))),
                "Ray starts outside and does not intersect the plane, should return null");


        // =============== Boundary Values Tests ==================

        // TC10: Ray is parallel and included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
                "Ray is included in the plane, should return null");

        // TC11: Ray is parallel and not included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 2), new Vector(1, 1, 0))),
                "Ray is parallel and outside the plane, should return null");

        // TC12: Ray intersects the plane and starts before the plane
        assertEquals(List.of(new Point(2.0/3, 2.0/3, -1.0/3)),
                plane.findIntersections(new Ray(new Point(0, 0, -1), new Vector(1, 1, 1))),
                "Ray starts before the plane and intersects it, should return intersection point");

        // TC013: Ray is orthogonal and starts inside the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, -1))),
                "Ray orthogonal and starts inside the plane, should return null");

        // TC14: Ray is orthogonal and starts after the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 2), new Vector(1, 1, 1))),
                "Ray orthogonal and starts after the plane, should return null");

        // TC15: Ray is neither orthogonal nor parallel and begins inside the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 1))),
                "Ray starts in the plane but is not parallel, should return null");

        // TC16: Ray is neither orthogonal nor parallel and begins at the reference point of the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 1, -1))),
                "Ray starts at the reference point of the plane, should return null");
    }

    /**
     * Tests the {@link Plane#calculateIntersectionsHelper(Ray, double)} method.
     * This test verifies correct intersection results of rays with a plane
     * considering the maximum distance constraint, by checking only the size of the intersection list
     * or null if there are no intersections.
     */
    @Test
    void testPlaneIntersections_MaxDistance_SimpleCheck() {
        // Create a plane with point at origin and normal along Z axis (XY plane)
        Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 0, 1));
        double maxDistance = 5.0;

        // Ray 1: intersects plane within maxDistance, expect 1 intersection
        Ray ray1 = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
        var intersections1 = plane.calculateIntersectionsHelper(ray1, maxDistance);
        assertNotNull(intersections1, "Ray1 should intersect the plane");
        assertEquals(1, intersections1.size(), "Ray1 should return 1 intersection");

        // Ray 2: intersects plane beyond maxDistance, expect null (no intersection)
        Ray ray2 = new Ray(new Point(0, 0, -10), new Vector(0, 0, 1));
        assertNull(plane.calculateIntersectionsHelper(ray2, maxDistance), "Ray2 intersection beyond maxDistance should return null");

        // Ray 3: parallel ray (direction orthogonal to plane normal), expect null
        Ray ray3 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.calculateIntersectionsHelper(ray3, maxDistance), "Ray3 parallel to plane should return null");

        // Ray 4: ray starts exactly on the plane, expect null
        Ray ray4 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNull(plane.calculateIntersectionsHelper(ray4, maxDistance), "Ray4 starting on plane should return null");

        // Ray 5: ray points away from plane (negative t), expect null
        Ray ray5 = new Ray(new Point(0, 0, 3), new Vector(0, 0, 1));
        assertNull(plane.calculateIntersectionsHelper(ray5, maxDistance), "Ray5 pointing away from plane should return null");
    }

}
