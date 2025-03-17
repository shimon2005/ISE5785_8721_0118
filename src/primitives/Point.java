package primitives;

import java.util.Objects;

public class Point {
    final Double3 xyz;

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Point(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point point)) return false;
        return Objects.equals(xyz, point.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }

    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    public double distanceSquared(Point point) {
        double x1 = xyz.d1();
        double y1 = xyz.d2();
        double z1 = xyz.d3();
        double x2 = point.xyz.d1();
        double y2 = point.xyz.d2();
        double z2 = point.xyz.d3();
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
    }

    public double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }

}
