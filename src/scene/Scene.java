package scene;


import lighting.AmbientLight;
import primitives.Color;
import geometries.Geometries;

public class Scene {

    public final String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();


    public Scene(String name) {
        this.name = name;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }



}
