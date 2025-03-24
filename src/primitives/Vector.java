package primitives;

import java.util.Objects;

/**
 * The Vector class represents a vector in 3D space.
 * It extends the Point class and provides additional vector-specific operations.
 */
public class Vector extends Point {

    /**
     * Constructs a Vector with the given coordinates.
     *
     * @param xyz the coordinates of the vector
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be the zero vector");
    }

    /**
     * Constructs a Vector with the given x, y, and z coordinates.
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Returns a string representation of this vector.
     *
     * @return a string representation of this vector
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the object is a Vector with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof Vector vector)) return false;
        return xyz.equals(vector.xyz);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the dot product of the two vectors
     */
    public double dotProduct(Vector vector) {
        return xyz.d1() * vector.xyz.d1() + xyz.d2() * vector.xyz.d2() + xyz.d3() * vector.xyz.d3();
    }

    /**
     * Adds another vector to this vector and returns the resulting vector.
     *
     * @param vector the vector to add
     * @return the resulting vector after addition
     * @throws IllegalArgumentException if the vector to add is the same as this vector
     */
    public Vector add(Vector vector) {
        if (this.equals(vector))
            throw new IllegalArgumentException("Cannot add a vector to itself");
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a scalar and returns the resulting vector.
     *
     * @param scalar the scalar to scale by
     * @return the resulting vector after scaling
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the cross product of the two vectors
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                xyz.d2() * vector.xyz.d3() - xyz.d3() * vector.xyz.d2(),
                xyz.d3() * vector.xyz.d1() - xyz.d1() * vector.xyz.d3(),
                xyz.d1() * vector.xyz.d2() - xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return the squared length of this vector
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    /**
     * Calculates the length of this vector.
     *
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector and returns the resulting unit vector.
     *
     * @return the resulting unit vector after normalization
     */
    public Vector normalize() {
        return new Vector(xyz.scale(1.0 / this.length()));
    }
}