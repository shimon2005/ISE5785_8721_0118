package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

public abstract class Intersectable {


    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Intersection intersection)) return false;
            return geometry == intersection.geometry && point.equals(intersection.point);
        }


    }

    /**
     * This method is used to calculate the intersections of a ray with the geometry.
     * It should be implemented by subclasses to provide specific intersection logic.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersections, or null if there are no intersections
     */
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * This method is used to calculate the intersections of a ray with the geometry.
     * It should be implemented by subclasses to provide specific intersection logic.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersections, or null if there are no intersections
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * This method is used to find the intersections of a ray with the geometry.
     * It calls the calculateIntersections method and extracts the points from the intersections.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }
}
