package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    /**
     * Test method for {@link Geometries#add(Intersectable...)}.
     */
    @Test
    void findIntersections() {
        // ============ Equivalence Partitions Tests ==============

        Geometries geometries = new Geometries();

        // TC01: Empty Geometries
        assertNull(geometries.findIntersections(new Ray(new Point(1, 2, 3), new Vector(1, 2, 3))),
                "findIntersections() should return null for an empty collection");

        // TC02: Geometries with 1 from each geometry, and none are intersected
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        Sphere sphere = new Sphere(new Point(0, 0, 1), 1);
        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        Polygon polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        geometries = new Geometries(plane, sphere, triangle, polygon);
        assertNull(geometries.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-2, -2, -2))),
                "findIntersections() should return null for no intersections");

        // TC03: Geometries with 1 from each geometry, and only one is intersected
        plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        sphere = new Sphere(new Point(0, 0, 1), 1);
        triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        geometries = new Geometries(plane, sphere, triangle, polygon);
        assertEquals(1,
                geometries.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-2, -2, -2))).size(),
                "findIntersections() should return 1 intersection point");

        // TC04: Geometries with 1 from each geometry, and 2 are intersected
        plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        sphere = new Sphere(new Point(0, 0, 1), 1);
        triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        geometries = new Geometries(plane, sphere, triangle, polygon);
        assertEquals(2,
                geometries.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-2, -2, -2))).size(),
                "findIntersections() should return 2 intersection points");

        // TC05: Geometries with 1 from each geometry, and all are intersected
        plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        sphere = new Sphere(new Point(0, 0, 1), 1);
        triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        geometries = new Geometries(plane, sphere, triangle, polygon);
        assertEquals(4,
                geometries.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-2, -2, -2))).size(),
                "findIntersections() should return 4 intersection points");
    }
}