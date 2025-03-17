package geometries;

import primitives.Vector;

public abstract class RadialGeometry extends Geometry {
    final Vector radius;

    public RadialGeometry(Vector radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Radius: " + radius;
    }
}
