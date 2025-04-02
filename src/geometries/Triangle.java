package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Triangle is a subclass of Polygon that represents a triangle.
 * A triangle is defined by exactly three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructor for Triangle, receives three points.
     *
     * @param p1 First vertex of the triangle
     * @param p2 Second vertex of the triangle
     * @param p3 Third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor with three points
    }

    /**
     * Returns a string representation of the triangle.
     *
     * @return a string representation of the triangle
     */
    @Override
    public String toString() {
        return "Vertices: " + vertices;
    }


    /**
     * Finds the intersection of a ray with the triangle using the Möller–Trumbore algorithm.
     * This method directly computes the intersection without precomputing the plane.
     * @param ray The ray to check for intersection.
     * @return A list containing the intersection point, or null if there is no intersection.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Triangle vertices
        Point p0 = vertices.get(0);
        Point p1 = vertices.get(1);
        Point p2 = vertices.get(2);

        // Triangle edges
        Vector edge1 = p1.subtract(p0);
        Vector edge2 = p2.subtract(p0);

        Vector dir = ray.getDirection();

        // Check if ray direction is parallel to edge2 before computing h (to prevent 0 vector build)
        double dotDirEdge2 = Math.abs(dir.normalize().dotProduct(edge2.normalize()));
        if (Util.isZero(dotDirEdge2 - 1.0)) {
            return null;
        }

        // Compute h = ray direction x edge2
        Vector h = dir.crossProduct(edge2);

        double det = edge1.dotProduct(h);
        if (Util.isZero(det))
            return null; // Ray is parallel to the triangle

        double invDet = 1.0 / det;

        // Calculate u parameter
        Vector s = ray.getHead().subtract(p0);

        // Check if s is parallel to edge1 before computing q (to prevent 0 vector build)
        double dotSEdge1 = Math.abs(s.normalize().dotProduct(edge1.normalize()));
        if (Util.isZero(dotSEdge1 - 1.0)) {
            return null;
        }

        double u = s.dotProduct(h) * invDet;
        if (u < 0 || u > 1)
            return null; // Intersection is outside the triangle

        // Compute q = s x edge1
        Vector q = s.crossProduct(edge1);

        double v = dir.dotProduct(q) * invDet;
        if (v < 0 || u + v > 1)
            return null; // Intersection is outside the triangle

        // If the intersection point is on the edge of the triangle, its not considered an intersection so we return null
        if (Util.isZero(u) || Util.isZero(v) || Util.isZero(1 - u - v))
            return null;

        double t = edge2.dotProduct(q) * invDet;
        if (t < 0)
            return null; // Intersection is behind the ray's origin

        Point intersectionPoint = ray.getPoint(t);
        return List.of(intersectionPoint);
    }

}
