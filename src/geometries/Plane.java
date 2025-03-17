package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Plane class represents a plane in 3D space.
 * It is defined by a point on the plane and a normal vector.
 */
public class Plane extends Geometry {
    final Point point;
    final Vector normal;

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
     */
    public Plane(Point point1, Point point2, Point point3) {
        this.point = point1;
        this.normal = null;
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
}