package renderer;

import scene.Scene;
import primitives.Ray;
import primitives.Color;

/**
 * Abstract base class for ray tracing in a 3D scene.
 * Provides the foundation for implementing specific ray tracing algorithms.
 */
public abstract class RayTracerBase {
    /** The scene to be rendered. */
    protected final Scene scene;

    /**
     * Constructs a RayTracerBase with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray through the scene and calculates the color at the intersection point.
     *
     * @param ray the ray to trace
     * @return the color at the intersection point
     */
    public abstract Color traceRay(Ray ray);
}