package geometries;

import lighting.LightSource;
import primitives.Point;
import primitives.Ray;
import primitives.Material;
import primitives.Vector;

import java.util.List;

/**
 * Abstract class representing an intersectable geometry in 3D space.
 * Provides methods to calculate intersections of a ray with the geometry.
 */
public abstract class Intersectable {

    /**
     * Static nested class representing an intersection point between a ray and a geometry.
     * Contains the geometry and the intersection point.
     */
    public static class Intersection {
        /** The geometry that the ray intersects with. */
        public final Geometry geometry;

        /** The intersection point on the geometry. */
        public final Point point;

        /** The material of the geometry at the intersection point. */
        public final Material material;

        /** The direction of the ray that intersects with the geometry at the intersection point. */
        public Vector v;

        /** The normal vector at the intersection point. */
        public Vector n;

        /** The dot product of the ray direction and the normal vector. */
        public double nv;

        /** The light source **/
        public LightSource lightSource;

        /** The direction from the light source to the intersection point **/
        public Vector l;

        /** The dot product of the light direction and the normal vector. **/
        public double nl;


        /**
         * Constructor for the Intersection class.
         *
         * @param geometry the geometry that the ray intersects with
         * @param point the intersection point on the geometry
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : geometry.getMaterial();
        }

        /**
         * Returns a string representation of the intersection.
         *
         * @return a string representation of the intersection
         */
        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

        /**
         * Checks if this intersection is equal to another object.
         *
         * @param o the object to compare with
         * @return true if the objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Intersection intersection)) return false;
            return geometry == intersection.geometry && point.equals(intersection.point);
        }
    }

    /**
     * Calculates the intersections of a ray with the geometry.
     * This method should be implemented by subclasses to provide specific intersection logic.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersections, or null if there are no intersections
     */
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * Calculates the intersections of a ray with the geometry.
     * Calls the {@code calculateIntersectionsHelper} method to perform the calculation.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersections, or null if there are no intersections
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Finds the intersection points of a ray with the geometry.
     * Calls the {@code calculateIntersections} method and extracts the points from the intersections.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}