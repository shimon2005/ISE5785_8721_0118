package lighting;

import primitives.Color;

/**
 * Represents the ambient light in a scene.
 * Ambient light is a global light source that affects all objects equally,
 * simulating indirect light in the environment.
 */
public class AmbientLight {

    /** The intensity of the ambient light. */
    private final Color intensity;

    /** A constant representing no ambient light (intensity is black). */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Gets the intensity of the ambient light.
     *
     * @return the intensity of the ambient light as a {@link Color}.
     */
    public Color getIntensity() {
        return intensity;
    }

    /**
     * Constructs an AmbientLight with the specified intensity.
     *
     * @param Ia the intensity of the ambient light as a {@link Color}.
     */
    public AmbientLight(Color Ia) {
        this.intensity = Ia;
    }
}