package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Tube} class (infinite cylinder).
 */
class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     * This test checks that the normal vector returned by getNormal is correct for a point on the surface of the tube.
     * It also covers edge cases such as points directly opposite the axis.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Check that the normal returned by getNormal is correct on the surface of the tube.
        Point p1 = new Point(1, 0, 0);  // A point on the surface of the tube
        Point p2 = new Point(0, 0, 0);  // Point at the base of the axis (for the axis)
        Ray axis = new Ray(p2, new Vector(0, 0, 1));  // Axis of the tube along Z-axis
        Tube tube = new Tube(axis, 1);  // Creating a tube with a radius of 1

        // Expected normal: The normal should be in the radial direction (X-axis in this case)
        Vector expectedNormal = new Vector(1, 0, 0);  // Normal to the surface of the tube at p1

        // Get the normal from the tube
        Vector normal = tube.getNormal(p1);

        // Check that the normal returned by getNormal is correct
        assertEquals(expectedNormal, normal, "Tube normal returned by getNormal is incorrect");

        // ============ Boundary Values Tests ==================

        // TC10: Check that the normal is correct for a point opposite the axis (directly across from the axis, at 90 degrees).
        Point p3 = new Point(0, 1, 0);  // A point directly to the side of the axis (perpendicular to it)

        // Expected normal: The normal should be in the radial direction (Y-axis in this case)
        Vector expectedNormal2 = new Vector(0, 1, 0);  // Normal to the surface of the tube at p3

        // Get the normal from the tube
        Vector normal2 = tube.getNormal(p3);

        // Check that the normal returned by getNormal is correct
        assertEquals(expectedNormal2, normal2, "Tube normal returned by getNormal is incorrect in edge case");
    }
}
