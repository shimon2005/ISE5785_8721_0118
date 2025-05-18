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
     * Tests {@link Geometries#calculateIntersectionsHelper(Ray, double)}
     */
    @Test
    void testGeometriesIntersections_MaxDistance_CountOnly() {
        Sphere sphere = new Sphere(new Point(0, 0, 5), 2); // Sphere center (0,0,5), radius=2
        Plane plane = new Plane(new Point(0, 0, 10), new Vector(0, 0, 1)); // Plane at z=10

        Geometries geometries = new Geometries(sphere, plane);

        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));

        // maxDistance = 7: sphere intersects twice within distance 3..7, plane beyond maxDistance
        var result1 = geometries.calculateIntersectionsHelper(ray, 7.0);
        assertNotNull(result1, "Expected intersections within maxDistance 7");
        assertEquals(2, result1.size(), "Expected exactly 2 intersections (sphere only)");

        // maxDistance = 9: same as above, plane at 10 still excluded
        var result2 = geometries.calculateIntersectionsHelper(ray, 9.0);
        assertNotNull(result2, "Expected intersections within maxDistance 9");
        assertEquals(2, result2.size(), "Expected exactly 2 intersections (sphere only)");

        // maxDistance = 3: sphere intersection exactly at distance 3, should return intersection(s)
        var result3 = geometries.calculateIntersectionsHelper(ray, 3.0);
        assertNotNull(result3, "Expected intersections at maxDistance 3 (distance equal to max)");
        assertTrue(result3.size() >= 1, "Expected at least 1 intersection at distance equal to maxDistance");

        // Ray missing all geometries, arbitrary maxDistance
        Ray missRay = new Ray(new Point(10, 10, 10), new Vector(1, 0, 0));
        var result4 = geometries.calculateIntersectionsHelper(missRay, 20.0);
        assertNull(result4, "No intersections expected for ray missing all geometries");
    }
}
