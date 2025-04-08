package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between Camera ray‐generation and geometry intersections,
 * covering the exact cases and counts from the “Intersections” slides.
 *
 * View‐plane: 3×3 pixels, physical size=3×3, distance=1.
 */
class CameraIntersectionsIntegrationTests {

    private static Camera camera;

    static void initCamera() {
        camera = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
    }

    /**
     * Helper: fires 3×3 rays through every pixel, intersects with {@code shape},
     * sums all intersection points, and asserts against {@code expectedCount}.
     */
    private void assertIntersectionsCount(Intersectable shape, int expectedCount) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = camera.constructRay(3, 3, j, i);
                List<Point> pts = shape.findIntersections(ray);
                if (pts != null) count += pts.size();
            }
        }
        assertEquals(expectedCount, count,
                "Total intersections for " + shape.getClass().getSimpleName());
    }

    /** Sphere cases (r=1→2,2.5→18,2→10,4→9,0.5→0) :contentReference[oaicite:0]{index=0} */
    @Test
    void testSphereIntegration() {
        assertIntersectionsCount(new Sphere(new Point(0,  0, -3),   1  ),  2);
        assertIntersectionsCount(new Sphere(new Point(0,  0, -2.5), 2.5), 18);
        assertIntersectionsCount(new Sphere(new Point(0,  0, -2),   2  ), 10);
        assertIntersectionsCount(new Sphere(new Point(0,  0, -1),   4  ),  9);
        assertIntersectionsCount(new Sphere(new Point(0,  0, -1), 0.5 ),  0);
    }

    /**
     * Plane cases exactly as on the slides:
     * <ul>
     *   <li>Plane at z=–1, n=(0,0,1) → 9 intersections</li>
     *   <li>Plane at x= 0, n=(1,0,0) → 9 intersections</li>
     *   <li>Plane at y= 0, n=(0,1,0) → 6 intersections</li>
     * </ul>
     * :contentReference[oaicite:1]{index=1}
     */
    @Test
    void testPlaneIntegration() {
        // perpendicular to view‐direction, in front of camera
        assertIntersectionsCount(
                new Plane(new Point(0, 0, -1), new Vector(0, 0, 1)),
                9
        );
        // vertical plane through camera (x=0)
        assertIntersectionsCount(
                new Plane(Point.ZERO, new Vector(1, 0, 0)),
                9
        );
        // horizontal plane through camera (y=0)
        assertIntersectionsCount(
                new Plane(Point.ZERO, new Vector(0, 1, 0)),
                6
        );
    }

    /** Triangle cases (centered→1, shifted up→2) :contentReference[oaicite:2]{index=2} */
    @Test
    void testTriangleIntegration() {
        assertIntersectionsCount(
                new Triangle(
                        new Point( 0,  1, -2),
                        new Point( 1, -1, -2),
                        new Point(-1, -1, -2)
                ),
                1
        );
        assertIntersectionsCount(
                new Triangle(
                        new Point( 0, 20, -2),
                        new Point( 1, -1, -2),
                        new Point(-1, -1, -2)
                ),
                2
        );
    }
}
