package geometries;

import primitives.Vector;

/**
 * The RadialGeometry class is an abstract class that represents a radial geometric object in 3D space.
 * It extends the Geometry class and adds a radius property.
 */
public abstract class RadialGeometry extends Geometry {
    final Vector radius;

    /**
     * Constructs a RadialGeometry with a given radius.
     *
     * @param radius the radius of the radial geometry
     */
    public RadialGeometry(Vector radius) {
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