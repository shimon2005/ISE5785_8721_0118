package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;

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

   /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
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

   /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
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
      Polygon polygon = new Polygon(
              new Point(0, 0, 1),
              new Point(1, 0, 0),
              new Point(0, 1, 0),
              new Point(-1, 1, 1)
      );

      // ============ Equivalence Partitions Tests ==============

      // TC01: Ray intersects inside the polygon (an interior intersection)
      // The ray starts at (0.25, 0.25, 1) and goes in the (0,0,-1) direction.
      // It intersects the polygon at (0.25, 0.25, 0.5), which is strictly inside.
      assertEquals(List.of(new Point(0.25, 0.25, 0.5)),
              polygon.findIntersections(new Ray(new Point(0.25, 0.25, 1), new Vector(0, 0, -1))),
              "Ray should intersect inside the polygon");

      // TC02: Ray intersects the plane of the polygon at a point clearly outside the polygon.
      // The ray from (2,1,-1) in the direction (-1,0,0) meets the plane at (1,1,-1), which lies outside.
      assertNull(polygon.findIntersections(new Ray(new Point(2, 1, -1), new Vector(-1, 0, 0))),
              "Ray intersects outside the polygon");

      // TC03: Ray intersects exactly at a vertex of the polygon (vertex B: (1,0,0)).
      // With the decision that boundary intersections are not counted, we expect no intersection.
      assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 1), new Vector(0, 0, -1))),
              "Ray intersects exactly at a vertex of the polygon, which should not count as an intersection");

      // =============== Boundary Values Tests ==================

      // TC04: Ray intersects on the edge of the polygon.
      // For a ray from (0.5, 0, 1) in the (0,0,-1) direction, the intersection lies exactly on an edge.
      // Hence, no intersection is counted.
      assertNull(polygon.findIntersections(new Ray(new Point(0.5, 0, 1), new Vector(0, 0, -1))),
              "Ray intersects on the edge of the polygon, which should not count as an intersection");

      // TC05: Ray intersects exactly in the vertex of the polygon (vertex A: (0,0,1)).
      // Even though the ray from (0,0,2) in the (0,0,-1) direction hits the vertex,
      // boundary intersections are not counted.
      assertNull(polygon.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))),
              "Ray intersects in the vertex of the polygon, which should not count as an intersection");

      // TC06: Ray intersects on the continuation of an edge (outside the polygon).
      // The ray from (2, 0, 0) with direction (-1, 0, 0) intersects the plane along the line extending an edge,
      // so no intersection with the polygon is counted.
      assertNull(polygon.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0))),
              "Ray intersects on the continuation of an edge, which should not count as an intersection");
   }


   /**
    * Tests the {@link Polygon#calculateIntersectionsHelper(Ray, double)} method.
    * This test checks intersection results of rays with a polygon, considering
    * valid intersections, intersections outside the polygon, on boundaries, or beyond max distance.
    */
   @Test
   void testPolygonIntersections_MaxDistance_SimpleCheck() {
      // Create a simple square polygon on XY plane
      Polygon polygon = new Polygon(
              new Point(1, 1, 0),
              new Point(-1, 1, 0),
              new Point(-1, -1, 0),
              new Point(1, -1, 0)
      );
      double maxDistance = 5.0;

      // Ray 1: intersects inside the polygon within maxDistance → expect 1 intersection
      Ray ray1 = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
      var intersections1 = polygon.calculateIntersectionsHelper(ray1, maxDistance);
      assertNotNull(intersections1, "Ray1 should intersect inside the polygon");
      assertEquals(1, intersections1.size(), "Ray1 should return 1 intersection");

      // Ray 2: intersects the polygon plane but outside the polygon → expect null
      Ray ray2 = new Ray(new Point(2, 2, -2), new Vector(0, 0, 1));
      assertNull(polygon.calculateIntersectionsHelper(ray2, maxDistance), "Ray2 intersects plane but outside polygon, should return null");

      // Ray 3: intersects beyond maxDistance → expect null
      Ray ray3 = new Ray(new Point(0, 0, -10), new Vector(0, 0, 1));
      assertNull(polygon.calculateIntersectionsHelper(ray3, maxDistance), "Ray3 intersects within polygon but beyond maxDistance, should return null");

      // Ray 4: intersects exactly on a vertex → expect null
      Ray ray4 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
      assertNull(polygon.calculateIntersectionsHelper(ray4, maxDistance), "Ray4 intersects on polygon vertex, should return null");

      // Ray 5: intersects exactly on an edge → expect null
      Ray ray5 = new Ray(new Point(0, 1, -1), new Vector(0, 0, 1));
      assertNull(polygon.calculateIntersectionsHelper(ray5, maxDistance), "Ray5 intersects on polygon edge, should return null");

      // Ray 6: parallel to polygon plane → expect null
      Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
      assertNull(polygon.calculateIntersectionsHelper(ray6, maxDistance), "Ray6 parallel to polygon plane, should return null");
   }


}
