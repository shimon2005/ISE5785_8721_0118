package lighting;


import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The LightSource interface represents a light source in a 3D scene.
 * It provides methods to get the intensity and direction of the light at a specific point.
 */
public interface LightSource {

    /**
     * Gets the intensity of the light at a specific point.
     *
     * @param p the point in space
     * @return the color intensity of the light at the point
     */
    Color getIntensity(Point p);

    /**
     * Gets the direction of the light from a specific point.
     *
     * @param p the point in space
     * @return the vector direction of the light from the point
     */
    Vector getL(Point p);

    /**
     * Gets the distance from the light source to a specific point.
     *
     * @param point the point in space
     * @return the distance from the light source to the point
     */
    double getDistance(Point point);
}
