package renderer;

import scene.Scene;
import primitives.Ray;
import primitives.Color;

public abstract class RayTracerBase {
    protected final Scene scene;

    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    public abstract Color traceRay(Ray ray);



}
