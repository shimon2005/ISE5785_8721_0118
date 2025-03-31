package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    /**
     * Adds a list of geometries to the collection.
     *
     * @param geometries the list of geometries to add
     */
    private List<Intersectable> geometries = new LinkedList<>();


    public Geometries() {
    }

    public Geometries(Intersectable geometries) {

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
