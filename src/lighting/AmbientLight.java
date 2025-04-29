package lighting;

import primitives.Color;

public class AmbientLight {

    private final Color intensity;

    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    public Color getIntensity() {return intensity;}

    /**
     * Constructor for AmbientLight.
     *
     * @param Ia the intensity of the ambient light
     */
    public AmbientLight(Color Ia) {
        this.intensity = Ia;
    }
}
