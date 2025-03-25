package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import static org.junit.jupiter.api.Assertions.*;

class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     * This test checks that the normal vector returned by getNormal is correct.
     */
    @Test
    void getNormal() {
        // ===================== Equivalence Partitions Tests ====================

        // TC01: Check normal for a point on the lateral surface of the cylinder
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)); // Axis along the Z-axis
        Cylinder cylinder = new Cylinder(10, axis, 2);

        // Point on the lateral surface (x = 2, y = 0, z = 5)
        Point pointOnSurface = new Point(2, 0, 5);
        Vector expectedNormal = new Vector(1, 0, 0);  // Normal is along the X-axis

        // Get normal from the cylinder at the given point
        Vector normal = cylinder.getNormal(pointOnSurface);

        // Check that the normal is correct
        assertEquals(expectedNormal, normal, "Normal vector on the lateral surface is incorrect.");

        // TC02: Check normal for a point on the top base of the cylinder
        Point pointOnTopBase = new Point(0, 0, 10);  // Point at the center of the top base
        Vector expectedTopBaseNormal = new Vector(0, 0, 1);  // Normal is along the Z-axis

        // Get normal from the cylinder at the given point
        Vector topBaseNormal = cylinder.getNormal(pointOnTopBase);

        // Check that the normal is correct
        assertEquals(expectedTopBaseNormal, topBaseNormal, "Normal vector on the top base is incorrect.");

        // TC03: Check normal for a point on the bottom base of the cylinder
        Point pointOnBottomBase = new Point(0, 0, 0);  // Point at the center of the bottom base
        Vector expectedBottomBaseNormal = new Vector(0, 0, -1);  // Normal is along the negative Z-axis

        // Get normal from the cylinder at the given point
        Vector bottomBaseNormal = cylinder.getNormal(pointOnBottomBase);

        // Check that the normal is correct
        assertEquals(expectedBottomBaseNormal, bottomBaseNormal, "Normal vector on the bottom base is incorrect.");

        // ===================== Boundary Cases Tests ====================

        // TC10: Check normal for a point at the center of the top base
        Point pointOnCenterTop = new Point(0, 0, 10);  // Center of the top base
        Vector expectedNormalTopCenter = new Vector(0, 0, 1);  // Normal should be along the Z-axis

        // Get normal from the cylinder at the center of the top base
        Vector normalTopCenter = cylinder.getNormal(pointOnCenterTop);

        // Check that the normal is correct
        assertEquals(expectedNormalTopCenter, normalTopCenter, "Normal vector at the center of the top base is incorrect.");

        // TC11: Check normal for a point at the center of the bottom base
        Point pointOnCenterBottom = new Point(0, 0, 0);  // Center of the bottom base
        Vector expectedNormalBottomCenter = new Vector(0, 0, -1);  // Normal should be along the negative Z-axis

        // Get normal from the cylinder at the center of the bottom base
        Vector normalBottomCenter = cylinder.getNormal(pointOnCenterBottom);

        // Check that the normal is correct
        assertEquals(expectedNormalBottomCenter, normalBottomCenter, "Normal vector at the center of the bottom base is incorrect.");

        // TC12: Check normal for a point on the intersection of the lateral surface and the top base
        Point pointOnIntersectionTop = new Point(2, 0, 10);  // Point on the intersection (edge of top base)
        Vector expectedNormalTopEdge = new Vector(1, 0, 0);  // Normal should be along X-axis

        // Get normal from the cylinder at the intersection point
        Vector normalTopEdge = cylinder.getNormal(pointOnIntersectionTop);

        // Check that the normal is correct
        assertEquals(expectedNormalTopEdge, normalTopEdge, "Normal vector on the intersection of top base and lateral surface is incorrect.");

        // TC13: Check normal for a point on the intersection of the lateral surface and the bottom base
        Point pointOnIntersectionBottom = new Point(2, 0, 0);  // Point on the intersection (edge of bottom base)
        Vector expectedNormalBottomEdge = new Vector(1, 0, 0);  // Normal should be along X-axis

        // Get normal from the cylinder at the intersection point
        Vector normalBottomEdge = cylinder.getNormal(pointOnIntersectionBottom);

        // Check that the normal is correct
        assertEquals(expectedNormalBottomEdge, normalBottomEdge, "Normal vector on the intersection of bottom base and lateral surface is incorrect.");
    }
}
