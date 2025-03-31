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

        // Edges of the triangle
        Vector edge1 = p1.subtract(p0);
        Vector edge2 = p2.subtract(p0);

        // Compute determinant
        Vector h = ray.getDirection().crossProduct(edge2);
        double det = edge1.dotProduct(h);

        // If determinant is near zero, ray is parallel to the triangle
        if (Util.isZero(det)) return null;

        double invDet = 1.0 / det;

        // Calculate barycentric coordinates
        Vector s = ray.getHead().subtract(p0);
        double u = s.dotProduct(h) * invDet;
        if (u < 0 || u > 1) return null; // Intersection is outside the triangle

        Vector q = s.crossProduct(edge1);
        double v = ray.getDirection().dotProduct(q) * invDet;
        if (v < 0 || u + v > 1) return null; // Intersection is outside the triangle

        // Compute t (distance along the ray)
        double t = edge2.dotProduct(q) * invDet;

        // If t is negative, the intersection is behind the ray's origin
        if (t < 0) return null;

        // Compute intersection point
        Point intersectionPoint = ray.getHead().add(ray.getDirection().scale(t));

        return List.of(intersectionPoint);
    }
}

