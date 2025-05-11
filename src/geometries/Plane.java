package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;
import java.util.stream.Stream;

/**
 * The Plane class represents a plane in 3D space.
 * It is defined by a point on the plane and a normal vector.
 */
public class Plane extends Geometry {
    private final Point point;
    private final Vector normal;

    /**
     * Constructs a Plane with a given point and normal vector.
     *
     * @param point the point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();   // the normal parameter is not necessarily normalized, so we normalize it
    }

    /**
     * Constructs a Plane with three points on the plane.
     * The normal vector is calculated from these points.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     * @throws IllegalArgumentException if any two points are identical or if the points are collinear (on the same line)
     */
    public Plane(Point point1, Point point2, Point point3) {
        // Check if any two points are identical
        if (point1.equals(point2) || point1.equals(point3) || point2.equals(point3)) {
            throw new IllegalArgumentException("Two or more points are identical.");
        }

        // Compute the vectors formed by the points
        Vector v1 = point2.subtract(point1); // Vector from point1 to point2
        Vector v2 = point3.subtract(point1); // Vector from point1 to point3

        // Check if the vectors are collinear by computing their cross product
        Vector crossProduct = v1.crossProduct(v2);

        // If the cross product's length is zero, the points are collinear (on the same line)
        if (Util.isZero(crossProduct.length())) {
            throw new IllegalArgumentException("Points are collinear and do not form a valid plane.");
        }

        // Set the point and calculate the normal vector
        this.point = point1;
        this.normal = crossProduct.normalize();  // Normal vector is the normalized cross product of v1 and v2
    }


    /**
     * Returns the normal vector to the plane.
     *
     * @return the normal vector to the plane
     */
    public Vector getNormal() {
        return this.normal;
    }

    /**
     * Returns the normal vector to the plane at a given point.
     *
     * @param point the point on the plane
     * @return the normal vector to the plane at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }

    /**
     * Returns a string representation of the plane.
     *
     * @return a string representation of the plane
     */
    @Override
    public String toString() {
        return "Point: " + point + ", Normal: " + normal;
    }


    /**
     * Finds the intersections of a ray with the plane.
     * If the ray intersects the plane, returns a list containing the intersection.
     * If the ray is parallel to the plane or does not intersect, returns null.
     * @param ray The ray to check for intersection.
     * @return A list containing the intersections, or null if there is no intersections.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        // Check if the ray's head is equal to the point field of the plane to avoid creating 0 vector during subtraction later on,
        if (point.equals(ray.getHead())) {
            return null; // The ray starts on the plane, no intersection
        }

        Vector rayDirection = ray.getDirection(); // Direction of the ray
        double denominator = Util.alignZero(normal.dotProduct(rayDirection)); // Dot product of the normal and ray direction


        // If the denominator is zero, the ray is parallel to the plane
        if (Util.isZero(denominator)) {
            return null; // No intersection
        }

        // Calculate the distance from the ray's origin to the plane
        double t = Util.alignZero(normal.dotProduct(point.subtract(ray.getHead())) / denominator);

        // If t is negative, there is no valid intersection
        if (t <= 0) {
            return null; // No intersection
        }

        // Calculate the intersection point
        Point intersectionPoint = ray.getPoint(t);

        return Stream.of(intersectionPoint)
                .map(p -> new Intersection(this, p))
                .toList();

    }
}