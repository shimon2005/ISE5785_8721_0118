package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 */
class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     * This test checks that the normal vector returned by getNormal is correct.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check that the normal returned by getNormal is correct
        Point center = new Point(0, 0, 0);  // Center of the sphere
        double radius = 1;  // Radius of the sphere
        Point pointOnSphere = new Point(1, 0, 0);  // A point on the sphere
        Sphere sphere = new Sphere(center, radius);

        // Expected normal: The normal is the vector from the center to the point, normalized
        Vector expectedNormal = new Vector(1, 0, 0);  // Vector from (0,0,0) to (1,0,0)

        // Get the normal from the sphere
        Vector normal = sphere.getNormal(pointOnSphere);

        // Check that the normal returned by getNormal is correct
        assertEquals(expectedNormal, normal, "Sphere normal returned by getNormal is incorrect");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    void findIntersections() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

        // ============ Equivalence Partitions Tests (4 tests) ==============

        // TC01: Ray is outside the sphere and does not intersect
        assertNull(sphere.findIntersections(new Ray(new Point(2, 2, 2), new Vector(1, 1, 1))),
                "Ray is outside the sphere, should return null");

        // TC02: Ray starts before and intersects the sphere twice
        assertEquals(List.of(new Point(0, -1, 0), new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 0))),
                "Ray starts outside and intersects twice, should return two points");

        // TC03: Ray starts inside the sphere and intersects once
        assertEquals(List.of(new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0.5, 0), new Vector(0, 1, 0))),
                "Ray starts inside and intersects once, should return one point");

        // TC04: Ray starts after the sphere and does not intersect
        assertNull(sphere.findIntersections(new Ray(new Point(0, 2, 0), new Vector(0, 1, 0))),
                "Ray starts outside after the sphere, should return null");

        // =============== Boundary Values Tests (13 tests) ==================

        // TC10: Ray intersects but does not pass through the center
        assertEquals(List.of(new Point(0.8660254037844386, 0.5, 0), new Point(-0.8660254037844386, 0.5, 0)),
                sphere.findIntersections(new Ray(new Point(2, 0.5, 0), new Vector(-1, 0, 0))),
                "Ray intersects sphere but does not pass through center, should return two points");


        // TC11: Ray goes through the center - starts before and intersects twice
        assertEquals(List.of(new Point(0, -1, 0), new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 0))),
                "Ray goes through center and intersects twice, should return two points");

        // TC12: Ray is tangent to the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1, -1, 0), new Vector(0, 1, 0))),
                "Ray is tangent to the sphere, should return null");

        // TC13: Ray originates from sphere’s surface and goes outward
        assertNull(sphere.findIntersections(new Ray(new Point(0, 1, 0), new Vector(0, 1, 0))),
                "Ray originates from sphere’s surface and goes outward, should return null");

        // TC14: Ray originates from sphere’s center
        assertEquals(List.of(new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
                "Ray originates from sphere’s center, should return one intersection point");

        // TC15: Ray is tangent to the sphere (ray's direction is orthogonal to the vector from the ray's origin to the sphere's center)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 1, 0), new Vector(1, 0, 0))),
                "Ray is tangent to the sphere, should return null");

        // TC16: Ray is outside the sphere and does not intersect (parallel to an axis)
        assertNull(sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 1, 0))),
                "Ray is outside the sphere and parallel to the Y-axis, should return null");

        // TC17: Ray starts inside the sphere and exits once
        assertEquals(List.of(new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, 0.5, 0), new Vector(0, 1, 0))),
                "Ray starts inside the sphere and exits once, should return one point");

        // TC19: Ray starts at a tangent point and moves outward
        assertNull(sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "Ray starts at a tangent point and moves outward, should return null");

        // TC20: Ray intersects exactly at the center
        assertEquals(List.of(new Point(0, -1, 0), new Point(0, 1, 0)),
                sphere.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 0))),
                "Ray intersects at the center, should return two points");

        // TC21: Ray passes near the sphere but does not touch it
        assertNull(sphere.findIntersections(new Ray(new Point(2, 2, 2), new Vector(1, 0, 0))),
                "Ray passes near the sphere but does not touch, should return null");

        // TC22: Ray grazes the edge of the sphere but does not intersect
        assertNull(sphere.findIntersections(new Ray(new Point(1.001, 0, 0), new Vector(1, 0, 0))),
                "Ray grazes the edge but does not intersect, should return null");
    }

}