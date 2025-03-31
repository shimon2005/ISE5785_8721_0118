package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import primitives.*;

/**
 * Testing Polygons
 * @author Dan
 */
class PolygonTests {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */
   private static final double DELTA = 0.000001;

   /** Test method for {@link Polygon#Polygon(Point...)}. */
   @Test
   void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                                           new Point(1, 0, 0),
                                           new Point(0, 1, 0),
                                           new Point(-1, 1, 1)),
                         "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link Polygon#getNormal(Point)}. */
   @Test
   void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }

    /**
     * Test method for {@link Polygon#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
       Polygon polygon = new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1));

       // ============ Equivalence Partitions Tests ==============

       // TC01: Ray intersects inside the polygon
       assertEquals(List.of(new Point(0.25, 0.25, 0.5)),
               polygon.findIntersections(new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1))),
               "Ray intersects inside the polygon");

       // TC02: Ray intersects outside the polygon against an edge
       assertNull(polygon.findIntersections(new Ray(new Point(2, 0.5, 0.5), new Vector(-1, 0, 0))),
               "Ray intersects outside the polygon against an edge");

       // TC03: Ray intersects outside the polygon against a vertex
       assertNull(polygon.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-1, -1, -1))),
               "Ray intersects outside the polygon against a vertex");

       // =============== Boundary Values Tests ==================

       // TC04: Ray intersects on the edge of the polygon
       assertEquals(List.of(new Point(0.5, 0, 0)),
               polygon.findIntersections(new Ray(new Point(0.5, 0, 1), new Vector(0, 0, -1))),
               "Ray intersects on the edge of the polygon");

       // TC05: Ray intersects in the vertex of the polygon
       assertEquals(List.of(new Point(0, 0, 1)),
               polygon.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))),
               "Ray intersects in the vertex of the polygon");

       // TC06: Ray intersects on the continuation of an edge
       assertNull(polygon.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0))),
               "Ray intersects on the continuation of an edge");
    }
}
