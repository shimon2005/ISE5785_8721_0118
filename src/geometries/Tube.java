package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Tube {
    Ray axis;

    public Tube(Ray axis) {
        this.axis = axis;
    }

    public Vector getNormal(Point point) {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", Axis: " + axis;
    }
}
