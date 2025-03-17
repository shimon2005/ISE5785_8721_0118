package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Cylinder extends Tube {
    double height;

    public Cylinder(Ray axis, double height) {
        super(axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", Height: " + height;
    }
}
