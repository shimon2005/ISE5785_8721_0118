package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
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
}
