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
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // Setup geometries: Sphere, Plane, Triangle
        Sphere sphere = new Sphere(new Point(1, 0, 0), 1);
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        Triangle triangle = new Triangle(new Point(0, 1, 0), new Point(1, -1, 0), new Point(-1, -1, 0));
        Geometries geometries = new Geometries(sphere, plane, triangle);

        // Ray that intersects some but not all geometries
        Ray rayPartial = new Ray(new Point(0, 0, -1), new Vector(1, 0, 1));
        assertEquals(2, geometries.findIntersections(rayPartial).size(),
                "findIntersections() wrong number of intersections when some geometries are intersected");

        // Ray that intersects all geometries
        Ray rayAll = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertEquals(4, geometries.findIntersections(rayAll).size(),
                "findIntersections() wrong number of intersections when all geometries are intersected");

        // =============== Boundary Values Tests ==================

        // TC10: Empty geometries collection
        Geometries emptyGeometries = new Geometries();
        assertNull(emptyGeometries.findIntersections(rayAll),
                "findIntersections() should return null for an empty collection");

        // TC11: Ray does not intersect any geometries
        Ray rayMiss = new Ray(new Point(0, 0, -1), new Vector(0, -1, -1));
        assertNull(geometries.findIntersections(rayMiss),
                "findIntersections() should return null when no geometries are intersected");

        // TC12: Ray intersects only one geometry
        Ray rayOne = new Ray(new Point(0, 0, -1), new Vector(1, 0, 0));
        assertEquals(2, geometries.findIntersections(rayOne).size(),
                "findIntersections() wrong number of intersections when only one geometry is intersected");
    }
}
