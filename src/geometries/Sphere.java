package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Sphere class represents a sphere in 3D space.
 * It extends the RadialGeometry class and adds a center point.
 */
public class Sphere extends RadialGeometry {
    Point center;

    /**
     * Constructs a Sphere with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, Vector radius) {
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
        return null;
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
}