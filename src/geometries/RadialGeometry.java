package geometries;

import static primitives.Util.isZero;

/**
 * The RadialGeometry class is an abstract class that represents a radial geometric object in 3D space.
 * It extends the Geometry class and adds a radius property.
 */
public abstract class RadialGeometry extends Geometry {
    /** The radius of the radial geometry. */
    protected double radius;

    /**
     * Constructs a RadialGeometry with a given radius.
     *
     * @param radius the radius of the radial geometry
     * @throws IllegalArgumentException if the radius is zero or negative
     */
    public RadialGeometry(double radius) {
        if (isZero(radius) || radius < 0)
            throw new IllegalArgumentException("Radius cannot be zero or negative");
        this.radius = radius;
    }

    /**
     * Returns a string representation of the radial geometry.
     *
     * @return a string representation of the radial geometry
     */
    @Override
    public String toString() {
        return "Radius: " + radius;
    }
}