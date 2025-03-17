package primitives;

import java.util.Objects;

public class Ray {
    final Point head;
    final Vector direction;

    public Ray(Point point, Vector vector) {
        this.head = point;
        this.direction = vector;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ray ray)) return false;
        return Objects.equals(head, ray.head) && Objects.equals(direction, ray.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}
