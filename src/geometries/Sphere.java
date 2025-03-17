package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry {
    Point center;

    public Sphere(Point center, Vector radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", Center: " + center;
    }

}
