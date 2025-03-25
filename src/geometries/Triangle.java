package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Triangle is a subclass of Polygon that represents a triangle.
 * A triangle is defined by exactly three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructor for Triangle, receives three points.
     *
     * @param p1 First vertex of the triangle
     * @param p2 Second vertex of the triangle
     * @param p3 Third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3); // Calls the Polygon constructor with three points
    }

    /**
     * Returns the normal vector to the triangle at a given point.
     * Since a triangle is part of a plane, it should return the normal of its plane.
     *
     * @param point the point on the triangle
     * @return null (as requested)
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    /**
     * Returns a string representation of the triangle.
     *
     * @return a string representation of the triangle
     */
    @Override
    public String toString() {
        return "Vertices: " + vertices;
    }
}

