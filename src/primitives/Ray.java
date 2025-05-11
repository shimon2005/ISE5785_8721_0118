package primitives;

import java.util.List;
import geometries.Intersectable.Intersection;


/**
 * The Ray class represents a ray in 3D space.
 * It is defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /** The starting point of the ray. */
    private final Point head;

    /** The direction vector of the ray (a unit vector). */
    private final Vector direction;

    /**
     * Constructs a Ray with the given starting point and direction vector.
     *
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();   // the direction vector is not necessarily normalized, so we normalize it
    }

    /**
     * Returns the starting point of the ray.
     *
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a point on the ray at a distance t from the starting point.
     *
     * @param t the distance from the starting point
     * @return a point on the ray at distance t from the starting point
     */
    public Point getPoint(double t){
        return Util.isZero(t) ? head : head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the head of the ray from a list of points.
     * @param points
     * @return the closest point to the head of the ray from the list of points
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Finds the closest intersection point from a list of intersections.
     *
     * @param intersectionsList the list of intersections
     * @return the closest intersection point to the head of the ray
     */
    public Intersection findClosestIntersection(List<Intersection> intersectionsList) {
        if (intersectionsList == null || intersectionsList.isEmpty()) {
            return null; // or throw an exception
        }

        Intersection closestIntersection = intersectionsList.get(0);
        double minDistance = this.head.distanceSquared(closestIntersection.point);

        for (Intersection intersection : intersectionsList) {
            double distance = this.head.distanceSquared(intersection.point);
            if (distance < minDistance) {
                minDistance = distance;
                closestIntersection = intersection;
            }
        }
        return closestIntersection;
    }


    /**
     * Returns a string representation of this ray.
     *
     * @return a string representation of this ray
     */
    @Override
    public String toString() {
        return "Head" + head + ", Direction" + direction;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray ray)) return false;

        return head.equals(ray.head) && direction.equals(ray.direction);
    }

    /**
     * Returns the hash code of this ray.
     *
     * @return the hash code of this ray
     */
    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }
}