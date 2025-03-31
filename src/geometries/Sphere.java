package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * The Sphere class represents a sphere in 3D space.
 * It extends the RadialGeometry class and adds a center point.
 */
public class Sphere extends RadialGeometry {
    /** The center point of the sphere. */
    Point center;

    /**
     * Constructs a Sphere with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     *
     * @param point the point on the sphere
     * @return the normal vector to the sphere at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Returns a string representation of the sphere.
     *
     * @return a string representation of the sphere
     */
    @Override
    public String toString() {
        return super.toString() + ", Center: " + center;
    }

    /**
     * This method finds the intersection points between a ray and a sphere.
     *
     * The algorithm uses vector mathematics to determine the points where the ray intersects the sphere.
     * If the ray does not intersect the sphere, the method returns null. If there is one intersection point,
     * the method returns a list containing that point. If there are two intersection points,
     * the method returns a list containing both points.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead(); // Starting point of the ray.
        Vector v = ray.getDirection(); // Direction of the ray.
        Vector u = center.subtract(p0); // Vector from the ray's head to the sphere's center.

        // Calculate the projection of the ray's direction onto the vector u.
        double tm = Util.alignZero(v.dotProduct(u));

        // Calculate the squared distance from the ray to the sphere's center, excluding the projection part.
        double dSquared = Util.alignZero(u.lengthSquared() - tm * tm);

        // The squared radius of the sphere.
        double radiusSquared = radius * radius;

        // If the squared distance is greater than the radius squared, there is no intersection.
        if (dSquared > radiusSquared) {
            return null; // No intersections
        }

        // Calculate the distance along the ray to the intersection points.
        double th = Math.sqrt(radiusSquared - dSquared);

        // Calculate the two potential intersection points along the ray.
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        // If no valid intersection points exist (either t1 or t2 are negative), return null.
        if (Util.isZero(t1) || Util.isZero(t2)) {
            return null; // No intersections on the ray (if they are exactly zero).
        }

        // Check if both intersection points are valid (i.e., t1 and t2 are positive).
        List<Point> intersections = new java.util.ArrayList<>();

        if (t1 > 0) {
            Point p1 = p0.add(v.scale(t1)); // First intersection point.
            intersections.add(p1);
        }
        if (t2 > 0) {
            Point p2 = p0.add(v.scale(t2)); // Second intersection point.
            intersections.add(p2);
        }

        // If no intersection points found, return null.
        if (intersections.isEmpty()) {
            return null;
        }

        // If exactly two intersection points, sort them by their distance from the ray's head.
        if (intersections.size() == 2) {
            Point p1 = intersections.get(0);
            Point p2 = intersections.get(1);

            double dist1 = p0.distance(p1);
            double dist2 = p0.distance(p2);

            // Sort points by distance (ascending).
            if (dist1 > dist2) {
                intersections.set(0, p2);
                intersections.set(1, p1);
            }
        }

        // Return the sorted list of intersection points.
        return List.of(intersections.get(0), intersections.get(1)); // Always return the points in sorted order.
    }
}