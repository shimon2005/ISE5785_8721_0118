package geometries;
import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A composite class representing a collection of geometric objects.
 * Implements the Intersectable interface using the Composite design pattern.
 */
public class Geometries implements Intersectable {

    /**
     * A list of geometric objects, initialized as an empty LinkedList.
     * This list cannot be reassigned to another list instance.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor - creates an empty Geometries collection.
     */
    public Geometries() {}

    /**
     * Constructor that initializes the Geometries collection with given geometries.
     *
     * @param geometries one or more geometric objects to add to the collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometric objects to the collection.
     *
     * @param geometries one or more geometric objects to add.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds intersection points of a given ray with all geometries in the collection.
     * Currently, this method returns null (not implemented yet).
     *
     * @param ray the ray to check for intersections.
     * @return a list of intersection points, or null if no intersections are found.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null; // Not implemented yet
    }
}
