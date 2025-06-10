package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackBoard {

    private static final Random random = new Random();

    /**
     * Generates jittered samples on a disk using polar mapping of a jittered grid.
     *
     * @param center    center of the disk
     * @param vRight    right-direction vector (X axis on disk)
     * @param vUp       up-direction vector (Y axis on disk)
     * @param radius    radius of the disk
     * @param numPoints number of jittered points (should be square number, like 81)
     * @return list of 3D points on the disk
     */
    public static List<Point> generateJitteredDiskSamples(Point center, Vector vRight, Vector vUp, double radius, int numPoints) {
        List<Point> points = new ArrayList<>();
        int gridSize = (int) Math.sqrt(numPoints);
        if (gridSize * gridSize != numPoints)
            throw new IllegalArgumentException("numPoints must be a perfect square (e.g. 81, 100, 121)");

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // jitter within grid cell
                double x = (i + random.nextDouble()) / gridSize * 2 - 1; // in [-1,1]
                double y = (j + random.nextDouble()) / gridSize * 2 - 1; // in [-1,1]

                if (x * x + y * y <= 1) { // inside unit disk
                    Point p = center
                            .add(vRight.scale(x * radius))
                            .add(vUp.scale(y * radius));
                    points.add(p);
                }
            }
        }

        return points;
    }
}
