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
            return v.scale(-1).normalize();
        }

        // Vector from the axis point to the given point
        Vector p0ToP = point.subtract(p0);

        // Project the point onto the axis to find the closest point on the axis
        double t = p0ToP.dotProduct(v);

        // Determine the closest point on the axis
        Point o = (t == 0) ? p0 : p0.add(v.scale(t));

        // If the point is on one of the caps (top or bottom)
        if (t <= 0) {
            return v.scale(-1).normalize(); // Bottom cap
        } else if (t >= height) {
            return v.normalize(); // Top cap
        }

        // Otherwise, the point is on the curved surface
        return point.subtract(o).normalize();
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