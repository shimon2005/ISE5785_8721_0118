package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The Geometry class is an abstract class that represents a geometric object in 3D space.
 * It provides methods to get the normal vector to the geometry at a given point and to manage
 * the emission color of the geometry.
 */
public abstract class Geometry extends Intersectable {

    /** The emission color of the geometry, default is black. */
    protected Color emission = Color.BLACK;

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the current geometry object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Returns the normal vector to the geometry at a given point.
     *
     * @param point the point on the geometry
     * @return the normal vector to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);

    /**
     * Calculates the intersections of a ray with the geometry.
     * This method must be implemented by subclasses to provide specific intersection logic.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersections, or null if there are no intersections
     */
    @Override
    public abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
}