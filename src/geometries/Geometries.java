package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A composite class representing a collection of geometric objects.
 * Implements the Intersectable interface using the Composite design pattern.
 * This class allows grouping multiple geometric objects and calculating their intersections
 * with a given ray as a single entity.
 */
public class Geometries extends Intersectable {

    /**
     * A list of geometric objects, initialized as an empty LinkedList,
     * using a composite design pattern.
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
     * Returns the collection of geometric objects.
     *
     * @return a collection of geometric objects.
     */
    public Collection<Object> getGeometries() {
        return Collections.singleton(geometries);
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
     * Calculates the intersection points between a given ray and all the geometries
     * in this composite geometry container (Geometries). Each intersection point is
     * wrapped in an {@link Intersection} object that includes the geometry it belongs to.
     *
     * @param ray the ray to intersect with the geometries.
     * @return a list of {@link Intersection} objects containing intersection points and
     *         their corresponding geometries; returns {@code null} if there are no intersections.
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersectionList = null;

        for (Intersectable geometry : geometries) {
            List<Point> points = geometry.findIntersections(ray);
            if (points != null) {
                if (intersectionList == null) {
                    intersectionList = new LinkedList<>();
                }
                intersectionList.addAll(
                        points.stream()
                                .map(p -> new Intersection((Geometry) geometry, p))
                                .toList()
                );
            }
        }

        return intersectionList;
    }
}