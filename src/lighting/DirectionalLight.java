package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import lighting.Light;

public class DirectionalLight extends Light implements LightSource {

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
    public Color getIntensity(Point p) {
        return intensity;
    }

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
}
