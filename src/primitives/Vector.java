package primitives;

import java.util.Objects;

public class Vector extends Point {

    public Vector(Double3 xyz) {
        super(xyz);
    }

    public Vector(double x, double y, double z) {
        super(new Double3(x, y, z));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector vector)) return false;
        return Objects.equals(xyz, vector.xyz);
    }

    public double dotProduct (Vector vector) {
        return xyz.d1() * vector.xyz.d1() + xyz.d2() * vector.xyz.d2() + xyz.d3() * vector.xyz.d3();
    }

    public Vector crossProduct (Vector vector) {
        return new Vector(
                xyz.d2() * vector.xyz.d3() - xyz.d3() * vector.xyz.d2(),
                xyz.d3() * vector.xyz.d1() - xyz.d1() * vector.xyz.d3(),
                xyz.d1() * vector.xyz.d2() - xyz.d2() * vector.xyz.d1()
        );
    }

    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public Vector normalize() {
        return new Vector(xyz.scale(1.0 / this.length()));
    }
}


