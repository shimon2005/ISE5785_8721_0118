package scene;


import lighting.AmbientLight;
import primitives.Color;
import geometries.Geometries;
import lighting.LightSource;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene containing geometries, lights, and ambient light.
 * The scene can be rendered with a specific background color and lighting conditions.
 */
public class Scene {

    /** The name of the scene. */
    public final String name;
    /** The background color of the scene. */
    public Color background = Color.BLACK;
    /** The ambient light in the scene. */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /** The geometries in the scene. */
    public Geometries geometries = new Geometries();
    /** The list of light sources in the scene. */
    public List<LightSource> lights = new LinkedList<>();


    /**
     * Constructs a scene with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets background for the scene
     *
     * @param background the background for the scene
     * @return the scene object itself
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light for the scene.
     *
     * @param  ambientLight the ambient light for the scene
     * @return the scene object itself
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries for the scene.
     *
     * @param  geometries list of geometries for the scene
     * @return the scene object itself
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Adds a light source to the scene.
     *
     * @param  lights list of light sources for the scene
     * @return the scene object itself
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }



}
