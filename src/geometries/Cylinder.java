package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * The Cylinder class represents a cylinder in 3D space.
 * It extends the Tube class and adds a height property.
 */
public class Cylinder extends Tube {
    double height;

    /**
     * Constructs a Cylinder with a given axis and height.
     *
     * @param axis the axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray axis, double height) {
        super(axis);
        this.height = height;
    }

    /**
     * Returns the normal vector to the cylinder at a given point.
     *
     * @param point the point on the cylinder
     * @return the normal vector to the cylinder at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    /**
     * Returns a string representation of the cylinder.
     *
     * @return a string representation of the cylinder
     */
    @Override
    public String toString() {
        return super.toString() + ", Height: " + height;
    }
}
