package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Cylinder} class.
 * This class contains tests for the Cylinder class methods.
 */
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
        Vector expectedNormal = new Vector(1, 0, 0);

        // Get normal from the cylinder at the given point
        Vector normal = cylinder.getNormal(pointOnSurface);

        // Check that the normal is correct
        assertTrue(normal.equals(expectedNormal) || normal.equals(expectedNormal.scale(-1)), "Normal vector on the lateral surface is incorrect.");

        // TC02: Check normal for a point on the top base of the cylinder
        Point pointOnTopBase = new Point(0, 0, 10);
        Vector expectedTopBaseNormal = new Vector(0, 0, 1);
        Vector topBaseNormal = cylinder.getNormal(pointOnTopBase);
        assertTrue(topBaseNormal.equals(expectedTopBaseNormal) || topBaseNormal.equals(expectedTopBaseNormal.scale(-1)), "Normal vector on the top base is incorrect.");

        // TC03: Check normal for a point on the bottom base of the cylinder
        Point pointOnBottomBase = new Point(0, 0, 0);
        Vector expectedBottomBaseNormal = new Vector(0, 0, -1);
        Vector bottomBaseNormal = cylinder.getNormal(pointOnBottomBase);
        assertTrue(bottomBaseNormal.equals(expectedBottomBaseNormal) || bottomBaseNormal.equals(expectedBottomBaseNormal.scale(-1)), "Normal vector on the bottom base is incorrect.");

        // ===================== Boundary Cases Tests ====================

        // TC10: Check normal for a point at the center of the top base
        Point pointOnCenterTop = new Point(0, 0, 10);
        Vector expectedNormalTopCenter = new Vector(0, 0, 1);
        Vector normalTopCenter = cylinder.getNormal(pointOnCenterTop);
        assertTrue(normalTopCenter.equals(expectedNormalTopCenter) || normalTopCenter.equals(expectedNormalTopCenter.scale(-1)), "Normal vector at the center of the top base is incorrect.");

        // TC11: Check normal for a point at the center of the bottom base
        Point pointOnCenterBottom = new Point(0, 0, 0);
        Vector expectedNormalBottomCenter = new Vector(0, 0, -1);
        Vector normalBottomCenter = cylinder.getNormal(pointOnCenterBottom);
        assertTrue(normalBottomCenter.equals(expectedNormalBottomCenter) || normalBottomCenter.equals(expectedNormalBottomCenter.scale(-1)), "Normal vector at the center of the bottom base is incorrect.");

        // TC12: Check normal for a point on the intersection of the lateral surface and the top base
        Point pointOnIntersectionTop = new Point(2, 0, 10);
        Vector expectedNormalTopEdge = new Vector(0, 0, 1);
        Vector normalTopEdge = cylinder.getNormal(pointOnIntersectionTop);
        assertTrue(normalTopEdge.equals(expectedNormalTopEdge) || normalTopEdge.equals(expectedNormalTopEdge.scale(-1)), "Normal vector on the intersection of top base and lateral surface is incorrect.");

        // TC13: Check normal for a point on the intersection of the lateral surface and the bottom base
        Point pointOnIntersectionBottom = new Point(2, 0, 0);
        Vector expectedNormalBottomEdge = new Vector(0, 0, -1);
        Vector normalBottomEdge = cylinder.getNormal(pointOnIntersectionBottom);
        assertTrue(normalBottomEdge.equals(expectedNormalBottomEdge) || normalBottomEdge.equals(expectedNormalBottomEdge.scale(-1)), "Normal vector on the intersection of bottom base and lateral surface is incorrect.");
    }
}
