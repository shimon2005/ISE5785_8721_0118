package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.Comparator;
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
     * The algorithm uses vector mathematics to determine the points where the ray intersects the sphere.
     * If the ray does not intersect the sphere, the method returns null. If there is one intersection point,
     * the method returns a list containing that point. If there are two intersection points,
     * the method returns a list containing both points.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Special case: if the ray originates at the center, it intersects the sphere at exactly one point.
        if (center.equals(p0)) {
            Point intersection = p0.add(v.scale(radius));
            return List.of(new Intersection(this, intersection, null));
        }

        // Compute vector from ray's origin to the sphere's center.
        Vector u = center.subtract(p0);

        // Project u onto the ray direction v.
        double tm = Util.alignZero(v.dotProduct(u));

        // Compute the squared distance from the sphere's center to the projection.
        double dSquared = Util.alignZero(u.lengthSquared() - tm * tm);
        double radiusSquared = radius * radius;

        // If the distance from the ray to the center is greater than the sphere's radius, no intersections occur.
        if (dSquared > radiusSquared) {
            return null;
        }

        // Distance from the projection point to the intersection points along the ray.
        double th = Math.sqrt(radiusSquared - dSquared);

        // If the ray is tangent to the sphere, return null.
        if (Util.isZero(th)) {
            return null;
        }

        // Calculate potential intersection distances along the ray.
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        if (t1 > 0 && t2 > 0) {
            Point p1 = ray.getPoint(t1);
            Point p2 = ray.getPoint(t2);
            return p0.distance(p1) <= p0.distance(p2) ?
                    List.of(new Intersection(this, p1, this.getMaterial()), new Intersection(this, p2, this.getMaterial())) :
                    List.of(new Intersection(this, p2, this.getMaterial()), new Intersection(this, p1, this.getMaterial()));
        }
        if (t1 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t1), this.getMaterial()));
        }
        if (t2 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t2), this.getMaterial()));
        }
        return null;
    }
}