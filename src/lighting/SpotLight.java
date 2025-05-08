package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {

    private final Vector direction;

    /**
     * Constructs a spot light source with the given intensity, position, and direction.
     *
     * @param intensity the intensity of the light
     * @param position  the position of the light source
     * @param direction the direction of the light beam
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the distance attenuation factors for the light source.
     * @param kc
     * @return
     */
    @Override
    public SpotLight setKc(double kc) {
        super.setKc(kc);
        return this;
    }

    /**
     * Sets the linear attenuation factor for the light source.
     * @param kl
     * @return
     */
    @Override
    public SpotLight setKl(double kl) {
        super.setKl(kl);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor for the light source.
     * @param kq
     * @return
     */
    @Override
    public SpotLight setKq(double kq) {
        super.setKq(kq);
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
        Vector l = getL(p); // וקטור מהמקור לנקודה
        double dirFactor = Math.max(0, direction.dotProduct(l)); // cos(θ)

        if (dirFactor == 0) {
            return Color.BLACK; // זווית ישרה או הפוכה => אין אור
        }

        double d = Position.distance(p);
        double attenuation = kC + kL * d + kQ * d * d;
        if (attenuation == 0) {
            return Color.BLACK; // הגנה מחלוקה באפס
        }

        return intensity.scale(dirFactor / attenuation);
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
