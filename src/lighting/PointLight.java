package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    /**
     * The attenuation factors for the light source.
     * kC - constant attenuation factor
     * kL - linear attenuation factor
     * kQ - quadratic attenuation factor
     */
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    public final Point Position;

    /**
     * Constructs a PointLight with color and position.
     *
     * @param intensity the color intensity of the light
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.Position = position;
    }

    /**
     * sets the Kc factor for the light source.
     * @param kC the Kc factor
     * @return the PointLight object itself
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor for the light source.
     *
     * @param kL the linear attenuation factor
     * @return the PointLight object itself
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor for the light source.
     *
     * @param kQ the quadratic attenuation factor
     * @return the PointLight object itself
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Gets the intensity of the light at a given point.
     *
     * @param p the point at which to calculate the intensity
     * @return the intensity of the light at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        double d = Position.distance(p);
        double attenuation = kC + kL * d + kQ * d * d;
        if (attenuation == 0) {
            return Color.BLACK;
        }
        return intensity.scale(1.0 / attenuation);

    }

    /**
     * Gets the direction of the light from a given point.
     *
     * @param p the point from which to get the direction
     * @return the direction of the light from the given point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(Position).normalize();
    }

}
