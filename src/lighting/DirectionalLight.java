package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {

    /**
     * The direction of the light source.
     */
    private final Vector direction;

    /**
     * Constructs a DirectionalLight with the given intensity and direction.
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Gets the intensity of the light at a given point.
     *
     * @param p the point at which to calculate the intensity
     * @return the intensity of the light at the given point
     */
    @Override
    public Color getIntensity(Point p) {return intensity; }

    /**
     * Gets the direction of the light from a given point.
     *
     * @param p the point from which to get the direction
     * @return the direction of the light from the given point
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Gets the distance from the light source to a given point.
     *
     * @param point the point at which to calculate the distance
     * @return the distance from the light source to the given point
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY; // Directional light is considered to be infinitely far away
    }
}
