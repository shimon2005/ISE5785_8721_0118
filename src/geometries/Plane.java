package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry{
    final Point point;
    final Vector normal;

    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }

}
