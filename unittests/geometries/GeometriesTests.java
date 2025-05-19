package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Geometries} class.
 */
class GeometriesTests {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     * This test checks the findIntersections method of the Geometries class.
     */
    @Test
    void testFindIntersections() {

        // ============ Equivalence Partitions Tests ==============

        // Setup geometries: Sphere, Plane, Triangle
        Sphere sphere = new Sphere(new Point(1, 0, 0), 1);
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        Triangle triangle = new Triangle(new Point(0, 1, 0), new Point(1, -1, 0), new Point(-1, -1, 0));
        Geometries geometries = new Geometries(sphere, plane, triangle);

        // TC01: Empty geometries collection
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        Geometries emptyGeometries = new Geometries();
        assertNull(emptyGeometries.findIntersections(ray),
                "findIntersections() should return null for an empty collection");

        // TC02: Ray does not intersect any geometries
        Ray rayMiss = new Ray(new Point(0, 0, -1), new Vector(0, -1, -1));
        assertNull(geometries.findIntersections(rayMiss),
                "findIntersections() should return null when no geometries are intersected");

        // TC03: Ray intersects only one geometry (Sphere, with two real intersection points)
        Ray rayOne = new Ray(new Point(0, 0, -0.5), new Vector(1, 0, 0));
        assertEquals(2, geometries.findIntersections(rayOne).size(),
                "findIntersections() wrong number of intersections when only one geometry (Sphere) is intersected");

        // TC04: Ray that intersects some but not all geometries
        // Expected intersections: 2 from the Sphere + 1 from the Plane = 3 intersections.
        Ray rayPartial = new Ray(new Point(0, 0, -1), new Vector(1, 0, 1));
        assertEquals(3, geometries.findIntersections(rayPartial).size(),
                "findIntersections() wrong number of intersections when some geometries are intersected");

        // TC05: Ray that intersects all geometries
        Ray rayAll = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertEquals(2, geometries.findIntersections(rayAll).size(),
                "findIntersections() wrong number of intersections when all geometries are intersected (tangencies not counted)");

    }


    /**
     * Tests {@link Geometries#calculateIntersections(Ray, double)}
     * focus on the maximum distance parameter.
     */
    @Test
    void testCalculateIntersectionsMaxDistance() {
        Sphere sphere = new Sphere(new Point(5, 0, 0), 2); // Sphere centered at x=5, radius=2
        Plane plane = new Plane(new Point(12, 0, 0), new Vector(1, 0, 0)); // Plane at x=10
        Geometries geometries = new Geometries(sphere, plane);

        double maxDistance = 3.5;
        Vector direction = new Vector(1, 0, 0); // X axis direction

        // Case 1: Ray starts and "ends" before all geometries
        Ray ray1 = new Ray(new Point(-2, 0, 0), direction);
        assertNull(geometries.calculateIntersections(ray1, maxDistance), "Ray1 before all geometries - no intersections");

        // Case 2: Ray starts after all geometries
        Ray ray2 = new Ray(new Point(15, 0, 0), direction);
        assertNull(geometries.calculateIntersections(ray2, maxDistance), "Ray2 after all geometries - no intersections");

        // Case 3: Ray starts before the sphere and end inside the sphere
        Ray ray3 = new Ray(new Point(2, 0, 0), direction);
        var intersections3 = geometries.calculateIntersections(ray3, maxDistance);
        assertNotNull(intersections3, "Ray3 should intersect sphere only");
        assertEquals(1, intersections3.size(), "Ray3 should return 1 intersections (1 side of the sphere)");

        // Case 4: Ray starts after the sphere and "ends" before the plane
        Ray ray4 = new Ray(new Point(8, 0, 0), direction);
        assertNull(geometries.calculateIntersections(ray4, maxDistance), "Ray4 starts sphere and 'end' before plane - no intersections");

        // Case 5: Increase maxDistance to cover both sphere and plane
        double maxDistanceLarge = 12.0;
        Ray ray5 = new Ray(new Point(2, 0, 0), direction);
        var intersections5 = geometries.calculateIntersections(ray5, maxDistanceLarge);
        assertNotNull(intersections5, "Ray5 should intersect sphere and plane");
        assertEquals(3, intersections5.size(), "Ray5 should return 3 intersections (2 from sphere, 1 from plane)");
    }


}
