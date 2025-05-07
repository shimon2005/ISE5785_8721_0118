package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
/**
 * The Geometry class is an abstract class that represents a geometric object in 3D space.
 * It provides a method to get the normal vector to the geometry at a given point.
 */
public abstract class Geometry extends Intersectable {

    protected Color emission = Color.BLACK;

    /**
     * Returns a string representation of the geometry.
     *
     * @return a string representation of the geometry
     */
    public Color getEmission() {return emission;}

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

    @Override
    public abstract List<Intersection> calculateIntersectionsHelper(Ray ray);


}