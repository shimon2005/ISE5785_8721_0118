package primitives;

import java.util.Objects;

/**
 * The Point class represents a point in 3D space.
 * It is defined by three coordinates (x, y, z) encapsulated in a Double3 object.
 */
public class Point {
    /**
     * A constant representing the origin point (0, 0, 0).
     * */
    public static final Point ZERO = new Point(0, 0, 0);
    final Double3 xyz;

    /**
     * Constructs a Point with the given Double3 object.
     *
     * @param xyz the Double3 object representing the coordinates of the point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Constructs a Point with the given coordinates.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    public Point(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Checks if this point is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the object is a Point with the same coordinates, false otherwise
     */
    @Override
    public  boolean equals(Object o) {
        if (this== o) return true;
        if (!(o instanceof Point point)) return false;

        return xyz.equals(point.xyz);
    }

    /**
     * Returns the hash code of this point.
     *
     * @return the hash code of this point
     */
    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    /**
     * Returns a string representation of this point.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * Adds a vector to this point and returns the resulting point.
     *
     * @param vector the vector to add
     * @return the resulting point after addition
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    /**
     * Subtracts another point from this point and returns the resulting vector.
     *
     * @param point the point to subtract
     * @return the resulting vector after subtraction
     */
    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param point the other point
     * @return the squared distance between the two points
     */
    public double distanceSquared(Point point) {
        double x1 = xyz.d1();
        double y1 = xyz.d2();
        double z1 = xyz.d3();

        double x2 = point.xyz.d1();
        double y2 = point.xyz.d2();
        double z2 = point.xyz.d3();

        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param point the other point
     * @return the distance between the two points
     */
    public double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }
}