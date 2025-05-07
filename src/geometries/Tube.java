package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

/**
 * The Tube class represents a tube in 3D space.
 * It is defined by an axis ray and a radius.
 */
public class Tube extends RadialGeometry {
    /** The axis ray of the tube. */
    protected Ray axis;

    /**
     * Constructs a Tube with a given axis and radius.
     *
     * @param axis the axis ray of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at a given point.
     *
     * @param point the point on the tube
     * @return the normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        // The starting point of the tube's axis
        Point p0 = axis.getHead();

        // The direction vector of the tube's axis
        Vector v = axis.getDirection();

        // Vector from the tube's axis origin to the given point
        Vector p0ToP = point.subtract(p0);

        // Projecting p0ToP onto the tube's axis direction to find the closest point on the axis
        double t = p0ToP.dotProduct(v);

        Point o = axis.getPoint(t);

        // The normal vector is the direction from o to the given point
        Vector normal = point.subtract(o);

        // Return the normalized normal vector
        return normal.normalize();
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

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}