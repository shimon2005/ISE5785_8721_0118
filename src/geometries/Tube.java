package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * The Tube class represents a tube in 3D space.
 * It is defined by an axis ray.
 */
public class Tube {
    Ray axis;

    /**
     * Constructs a Tube with a given axis.
     *
     * @param axis the axis ray of the tube
     */
    public Tube(Ray axis) {
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at a given point.
     *
     * @param point the point on the tube
     * @return the normal vector to the tube at the given point
     */
    public Vector getNormal(Point point) {
        return null;
    }

    /**
     * Returns a string representation of the tube.
     *
     * @return a string representation of the tube
     */
    @Override
    public String toString() {
        return super.toString() + ", Axis: " + axis;
    }
}