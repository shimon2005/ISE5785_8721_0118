package renderer;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BlackBoard} class.
 */
class BlackBoardTests {

    /**
     * Test method for {@link BlackBoard#generateJitteredDiskSamples(Point, Vector, Vector, double, int)}.
     * Validates that all generated points lie within the unit disk.
     */
    @Test
    void testPointsAreWithinDisk() {
        Point center = new Point(0, 0, 0);
        Vector vRight = new Vector(1, 0, 0); // X axis
        Vector vUp = new Vector(0, 1, 0);    // Y axis
        double radius = 1.0;
        int numPoints = 100; // 10x10

        List<Point> samples = BlackBoard.generateJitteredDiskSamples(center, vRight, vUp, radius, numPoints);

        for (Point p : samples) {
            Vector fromCenter = p.subtract(center);
            double x = fromCenter.dotProduct(vRight.normalize());
            double y = fromCenter.dotProduct(vUp.normalize());
            double distanceSquared = x * x + y * y;

            assertTrue(distanceSquared <= radius * radius + 1e-10,
                    "Point outside disk: " + p);
        }
    }

    /**
     * Test method for {@link BlackBoard#generateJitteredDiskSamples} - size of output list.
     */
    @Test
    void testPointCountWithinReasonableRange() {
        Point center = new Point(0, 0, 0);
        Vector vRight = new Vector(1, 0, 0);
        Vector vUp = new Vector(0, 1, 0);
        double radius = 1.0;
        int numPoints = 100;

        List<Point> points = BlackBoard.generateJitteredDiskSamples(center, vRight, vUp, radius, numPoints);

        assertNotNull(points, "Returned list should not be null");
        assertTrue(points.size() <= numPoints,
                "Too many points returned");
        assertTrue(points.size() >= 70,
                "Too many points were filtered out (expected at least 70)");
    }

    /**
     * Test method for invalid number of points (not a perfect square).
     */
    @Test
    void testInvalidNumPoints() {
        Point center = new Point(0, 0, 0);
        Vector vRight = new Vector(1, 0, 0);
        Vector vUp = new Vector(0, 1, 0);
        double radius = 1.0;

        assertThrows(IllegalArgumentException.class, () ->
                        BlackBoard.generateJitteredDiskSamples(center, vRight, vUp, radius, 50),
                "Expected exception for non-square number of points");
    }

    /**
     * Test method for return type is not null.
     */
    @Test
    void testNotNullReturn() {
        Point center = new Point(0, 0, 0);
        Vector vRight = new Vector(1, 0, 0);
        Vector vUp = new Vector(0, 1, 0);
        double radius = 2.0;
        int numPoints = 81;

        List<Point> result = BlackBoard.generateJitteredDiskSamples(center, vRight, vUp, radius, numPoints);
        assertNotNull(result, "Method should return non-null list");
    }
}
