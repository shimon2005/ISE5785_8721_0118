package geometries;

import java.util.List;
import java.util.stream.Stream;

import static primitives.Util.*;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * @author Dan
 */
public class Polygon extends Geometry {
   /** List of polygon's vertices */
   protected final List<Point> vertices;
   /** Associated plane in which the polygon lays */
   protected final Plane       plane;
   /** The size of the polygon - the amount of the vertices in the polygon */
   private final int           size;

   /**
    * Polygon constructor based on vertices list. The list must be ordered by edge
    * path. The polygon must be convex.
    * @param  vertices                 list of vertices according to their order by
    *                                  edge path
    * @throws IllegalArgumentException in any case of illegal combination of
    *                                  vertices:
    *                                  <ul>
    *                                  <li>Less than 3 vertices</li>
    *                                  <li>Consequent vertices are in the same
    *                                  point
    *                                  <li>The vertices are not in the same
    *                                  plane</li>
    *                                  <li>The order of vertices is not according
    *                                  to edge path</li>
    *                                  <li>Three consequent vertices lay in the
    *                                  same line (180&#176; angle between two
    *                                  consequent edges)
    *                                  <li>The polygon is concave (not convex)</li>
    *                                  </ul>
    */
   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
      this.vertices = List.of(vertices);
      size          = vertices.length;

      // Generate the plane according to the first three vertices and associate the
      // polygon with this plane.
      // The plane holds the invariant normal (orthogonal unit) vector to the polygon
      plane         = new Plane(vertices[0], vertices[1], vertices[2]);
      if (size == 3) return; // no need for more tests for a Triangle

      Vector  n        = plane.getNormal(vertices[0]);
      // Subtracting any subsequent points will throw an IllegalArgumentException
      // because of Zero Vector if they are in the same point
      Vector  edge1    = vertices[size - 1].subtract(vertices[size - 2]);
      Vector  edge2    = vertices[0].subtract(vertices[size - 1]);

      // Cross Product of any subsequent edges will throw an IllegalArgumentException
      // because of Zero Vector if they connect three vertices that lay in the same
      // line.
      // Generate the direction of the polygon according to the angle between last and
      // first edge being less than 180deg. It is hold by the sign of its dot product
      // with the normal. If all the rest consequent edges will generate the same sign
      // - the polygon is convex ("kamur" in Hebrew).
      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
      for (var i = 1; i < size; ++i) {
         // Test that the point is in the same plane as calculated originally
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
         // Test the consequent edges have
         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
      }
   }

    /**
     * Return the list of vertices of the polygon
     * @return the list of vertices of the polygon
     */
   @Override
   public Vector getNormal(Point point) { return plane.getNormal(point); }

   /**
    * Finds the intersections of the ray with the polygon.
    * If the ray intersects the polygon, returns a list containing the intersection.
    * Otherwise, returns null.
    * @param ray The ray to check for intersection.
    * @return A list containing the intersections, or null if there is no intersections.
    */
   @Override
   public List<Intersection> calculateIntersectionsHelper(Ray ray) {
      // Find the intersection point with the plane
      List<Point> planeIntersections = plane.findIntersections(ray);
      if (planeIntersections == null) return null;

      // Get the intersection point
      Point intersectionPoint = planeIntersections.getFirst();

      // Check if the intersection point is exactly one of the polygon's vertices.
      // If so, it is considered a boundary intersection.
      for (Point vertex : vertices) {
         if (vertex.equals(intersectionPoint)) {
            return null;
         }
      }

      int numVertices = vertices.size();
      for (int i = 0; i < numVertices; ++i) {
         // Compute vectors from the intersection point to the vertices.
         // We check if the subtraction would yield a zero vector and handle it upfront.
         Vector v1 = vertices.get(i).subtract(intersectionPoint);
         Vector v2 = vertices.get((i + 1) % numVertices).subtract(intersectionPoint);

         // Before computing the cross product, check if v1 and v2 are collinear to avoid creating a zero vector.
         // Two vectors are collinear if the absolute value of their normalized dot product is 1.
         double normalizedDot = v1.normalize().dotProduct(v2.normalize());
         if (Util.isZero(Math.abs(normalizedDot) - 1)) {
            return null; // The intersection point lies on the boundary (edge or vertex)
         }

         // Now safely compute the cross product, knowing it won't be the zero vector.
         Vector cross = v1.crossProduct(v2);

         // Check the sign of the dot product with the plane's normal.
         // A negative result means that the point is outside the polygon.
         if (Util.alignZero(cross.dotProduct(plane.getNormal())) < 0) {
            return null;
         }
      }

      return Stream.of(intersectionPoint)
              .map(p -> new Intersection(this, p))
              .toList();
   }


}
