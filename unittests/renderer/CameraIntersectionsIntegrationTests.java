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
 * View‐plane: 3×3 pixels, physical size=3×3, distance=1.
 */
class CameraIntersectionsIntegrationTests {

    /**
     * the cameras to be used in the tests
     */
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

    /**
     * Sphere cases:
     * - Sphere centered at (0,0,-3) with radius 1: 2 intersections.
     * - Sphere centered at (0,0,-2.5) with radius 2.5: 18 intersections.
     * - Sphere centered at (0,0,-2) with radius 2: 10 intersections.
     * - Sphere centered at (0,0,-1) with radius 4: 9 intersections.
     * - Sphere centered at (0,0,1) with radius 0.5: 0 intersections.
     */
    @Test
    void testSphereIntegration() {
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, -3),   1  ),  2);
        assertIntersectionsCount(camHalf, new Sphere(new Point(0,  0, -2.5), 2.5), 18);
        assertIntersectionsCount(camHalf, new Sphere(new Point(0,  0, -2),   2  ), 10);
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, -1),   4  ),  9);
        assertIntersectionsCount(cam0, new Sphere(new Point(0,  0, 1),   0.5),  0);
    }

    /**
     * Plane cases:
     * - Plane parallel to the y-axis: 9 intersections (all camera rays hit it).
     * - Tilted plane such that every ray from the view plane intersects it: 9 intersections.
     * - Tilted plane so that only the upper 6 rays (from the top two rows) hit the plane: 6 intersections.
     */
    @Test
    void testPlaneIntegration() {
       // Case 1: Plane parallel to the y-axis (normal = (0, 0, 1)).
        assertIntersectionsCount(cam0,
                new Plane(new Point(0, 0, -1), new Vector(0, 0, 1)),
                9);

        // Case 2: Tilted plane such that every ray from the view plane intersects it.
        assertIntersectionsCount(cam0,
                new Plane(new Point(0, 0, -1), new Vector(0, 0.5, -1)),
                9);

        // Case 3: Tilted plane so that only the upper 6 rays (from the top two rows) hit the plane.
        assertIntersectionsCount(cam0,
                new Plane(new Point(0, 0, -1), new Vector(0, 2, -1)),
                6);
    }


    /** Triangle cases:
     * - Triangle with vertices at (0,1,-2), (1,-1,-2), (-1,-1,-2): 1 intersection.
     * - Triangle with vertices at (0,20,-2), (1,-1,-2), (-1,-1,-2): 2 intersections.
     */
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