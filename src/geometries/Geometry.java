package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
/**
 * The Geometry class is an abstract class that represents a geometric object in 3D space.
 * It provides a method to get the normal vector to the geometry at a given point.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Returns the normal vector to the geometry at a given point.
     *
     * @param point the point on the geometry
     * @return the normal vector to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);

    public abstract List<Point> findIntersections(Ray ray);
}