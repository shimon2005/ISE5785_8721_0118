package primitives;

import java.util.Objects;

/**
 * The Ray class represents a ray in 3D space.
 * It is defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /** The starting point of the ray. */
    final Point head;

    /** The direction vector of the ray. */
    final Vector direction;

    /**
     * Constructs a Ray with the given starting point and direction vector.
     *
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();   // the direction vector is not necessarily normalized, so we normalize it
    }

    /**
     * Returns a string representation of this ray.
     *
     * @return a string representation of this ray
     */
    @Override
    public String toString() {
        return "Head" + head + ", Direction" + direction;
    }

    /**
     * Checks if this ray is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the object is a Ray with the same head and direction, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ray ray)) return false;
        return Objects.equals(head, ray.head) && Objects.equals(direction, ray.direction);
    }

    /**
     * Returns the hash code of this ray.
     *
     * @return the hash code of this ray
     */
    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}