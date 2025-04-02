package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
     */
    @Test
    void findIntersections() {
        Triangle triangle = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects inside the triangle
        assertEquals(List.of(new Point(0.25, 0.25, 0.5)),
                triangle.findIntersections(new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1))),
                "Ray intersects inside the triangle");

        // TC02: Ray intersects outside the triangle against an edge
        assertNull(triangle.findIntersections(new Ray(new Point(2, 0.5, 0.5), new Vector(-1, 0, 0))),
                "Ray intersects outside the triangle against an edge");

        // TC03: Ray intersects outside the triangle against a vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0, 1, 1), new Vector(0, 0, -1))),
                "Ray intersects outside the triangle against a vertex");

        // =============== Boundary Values Tests ==================

        // TC10: Ray intersects exactly on an edge of the triangle (should return null)
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0, 1), new Vector(0, 0, -1))),
                "Ray intersects exactly on an edge of the triangle");

        // TC11: Ray intersects exactly at a vertex of the triangle (should return null)
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))),
                "Ray intersects exactly at a vertex of the triangle");

        // TC12: Ray intersects on the continuation of an edge
        assertNull(triangle.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0))),
                "Ray intersects on the continuation of an edge");
    }
}
