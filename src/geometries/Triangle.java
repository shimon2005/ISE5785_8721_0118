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


    public List<Point> findIntersections(Ray ray) {
        final double EPSILON = 1e-10;

        // Triangle vertices: p1, p2, p3
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);

        // Compute two edges and the plane normal
        Vector edge1 = p2.subtract(p1);
        Vector edge2 = p3.subtract(p1);
        Vector normal = edge1.crossProduct(edge2).normalize();

        // Calculate the denominator (dot product of ray direction and plane normal)
        double denom = Util.alignZero(ray.getDirection().dotProduct(normal));
        if (Util.isZero(denom))
            return null; // Ray is parallel to the triangle's plane

        // Compute the parameter t for the ray–plane intersection
        double t = Util.alignZero(normal.dotProduct(p1.subtract(ray.getHead())) / denom);
        if (t < EPSILON)
            return null; // Intersection is behind the ray's origin

        // Compute the intersection point P
        Point P = ray.getPoint(t);

        // Additional check: if the intersection point equals one of the vertices, return null.
        if (P.equals(p1) || P.equals(p2) || P.equals(p3))
            return null;

        // Convert P to barycentric coordinates relative to the triangle (with p1 as reference)
        Vector v0 = edge1;              // p1 -> p2
        Vector v1 = edge2;              // p1 -> p3
        Vector v2 = P.subtract(p1);     // p1 -> P

        double dot00 = Util.alignZero(v0.dotProduct(v0));
        double dot01 = Util.alignZero(v0.dotProduct(v1));
        double dot02 = Util.alignZero(v0.dotProduct(v2));
        double dot11 = Util.alignZero(v1.dotProduct(v1));
        double dot12 = Util.alignZero(v1.dotProduct(v2));

        double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
        double u = Util.alignZero((dot11 * dot02 - dot01 * dot12) * invDenom);
        double v = Util.alignZero((dot00 * dot12 - dot01 * dot02) * invDenom);
        double w = 1 - u - v;

        // Check that all barycentric coordinates are strictly greater than EPSILON
        if (u <= EPSILON || v <= EPSILON || w <= EPSILON)
            return null;

        return List.of(P);
    }
}
