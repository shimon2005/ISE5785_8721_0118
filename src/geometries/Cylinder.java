package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * The Cylinder class represents a cylinder in 3D space.
 * It extends the Tube class and adds a height property.
 */
public class Cylinder extends Tube {
    /** The height of the cylinder. */
    double height;

    /**
     * Constructs a Cylinder with a given axis, radius, and height.
     *
     * @param axis the axis ray of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double height, Ray axis, double radius) {
        super(axis, radius);
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
        // The starting point of the axis
        Point p0 = axis.getHead();

        // The direction vector of the axis
        Vector v = axis.getDirection();

        // Check if the point is exactly at p0 (bottom base center), it is necessary to treat it separately to avoid creating a zero vector in the next step (substraction)
        if (point.equals(p0)) {
            return v.scale(-1); // No need to normalize, since v is already a unit vector
        }

        // Vector from the axis point to the given point
        Vector p0ToP = point.subtract(p0);

        // Project the point onto the axis to find the closest point on the axis
        double t = p0ToP.dotProduct(v);

        // If the point is on one of the caps (top or bottom)
        if (t == 0) {
            return v.scale(-1); // Bottom cap, no need to normalize, since v is already a unit vector
        } else if (t == height) {
            return v; // Top cap, no need to normalize, since v is already a unit vector
        }
        // If reach here the point is on the curved surface

        // Determine the closest point to the point parameter on the axis
        Point o = p0.add(v.scale(t));

        // Calculate the normal for a point on the curved surface
        Vector normal = point.subtract(o);

        // Return the normalized normal vector
        return normal.normalize();
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