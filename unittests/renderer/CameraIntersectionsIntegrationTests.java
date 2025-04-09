package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between Camera ray‐generation and geometry intersections.
 *
 * View‐plane: 3×3 pixels, physical size=3×3, distance=1.
 */
class CameraIntersectionsIntegrationTests {

    private static Camera cam0;
    private static Camera camHalf;

    @BeforeAll
    static void initCameras() {
        // Camera at (0,0,0) looking at (0,0,-1) with up vector (0,-1,0)
        cam0 = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        // Camera at (0,0,0.5) looking at (0,0,-1) with up vector (0,-1,0)
        camHalf = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
    }

    /**
     * Helper: fires 3×3 rays through every pixel, intersects with {@code shape},
     * sums all intersection points, and asserts against {@code expectedCount}.
     */
    private void assertIntersectionsCount(Camera cam, Intersectable shape, int expectedCount) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = cam.constructRay(3, 3, j, i);
                List<Point> pts = shape.findIntersections(ray);
                if (pts != null) count += pts.size();
            }
        }
        assertEquals(expectedCount, count,
                "Total intersections for " + shape.getClass().getSimpleName());
    }

    /** Sphere cases: radius→expectedCount */
    @Test
    void testSphereIntegration() {
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, -3),   1  ),  2);
        assertIntersectionsCount(camHalf, new Sphere(new Point(0,  0, -2.5), 2.5), 18);
        assertIntersectionsCount(camHalf, new Sphere(new Point(0,  0, -2),   2  ), 10);
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, -1),   4  ),  9);
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, 1),   0.5),  0);
    }

    /** Plane cases: perpendicular → 9, vertical → 9, horizontal → 6 */
    @Test
    void testPlaneIntegration() {
        // perpendicular to view direction
        assertIntersectionsCount(cam0,
                new Plane(new Point(0, 0, -1), new Vector(0, 0, 1)), 9);

        // vertical plane through camera (x=0)
        assertIntersectionsCount(cam0,
                new Plane(Point.ZERO, new Vector(1, 0, 0)), 9);

        // horizontal plane through camera (y=0)
        assertIntersectionsCount(cam0,
                new Plane(Point.ZERO, new Vector(0, 1, 0)), 6);
    }

    /** Triangle cases: centered → 1, large → 2 */
    @Test
    void testTriangleIntegration() {
        // only center pixel hits
        assertIntersectionsCount(cam0,
                new Triangle(
                        new Point( 0,  1, -2),
                        new Point( 1, -1, -2),
                        new Point(-1, -1, -2)
                ), 1);

        // 2 pixels hit
        assertIntersectionsCount(cam0,
                new Triangle(
                        new Point( 0, 20, -2),
                        new Point( 1, -1, -2),
                        new Point(-1, -1, -2)
                ), 2);
    }
}