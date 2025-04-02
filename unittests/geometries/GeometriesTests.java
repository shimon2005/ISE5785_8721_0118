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
}
