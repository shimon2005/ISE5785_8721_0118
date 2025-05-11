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
        Vector l = getL(p); // direction from light to point
        double dirFactor = Math.max(0, direction.dotProduct(l));

        if (dirFactor <= 0) {
            // the angle between the light direction and the vector from the light source to the point
            // is over 90 degrees
            return Color.BLACK;
        }

        // reach here only if dirFactor > 0

        // get the intensity from the parent class to be used as part of the calculation for the final intensity
        Color pointIntensity = super.getIntensity(p);

        if (pointIntensity.equals(Color.BLACK)) {
            return Color.BLACK;
        }

        return pointIntensity.scale(dirFactor);
    }
}
