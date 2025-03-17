package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry{
    final Point point;
    final Vector normal;

    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();   // the normal parameter is not necessarily normalized, so we normalize it
    }

    public Plane(Point point1, Point point2, Point point3) {
        this.point = point1;
        this.normal = null;
    }

    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }

    @Override
    public String toString() {
        return "Point: " + point + ", Normal: " + normal;
    }
}
