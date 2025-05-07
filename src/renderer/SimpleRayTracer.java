package renderer;

import scene.Scene;
import primitives.Ray;
import primitives.Color;
import geometries.Intersectable. Intersection;

import java.util.List;

/**
 * A simple implementation of a ray tracer for rendering a 3D scene.
 * This class calculates the color of a ray by finding its intersections
 * with geometries in the scene and determining the closest intersection point.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     * If there are no intersections, it returns the background color of the scene.
     *
     * @param ray the ray to be traced
     * @return the color at the intersection point or the background color if no intersection
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = this.scene.geometries.calculateIntersections(ray);
        if (intersections == null) {
            return this.scene.background;
        } else {
            Intersection closestIntersection = ray.findClosestIntersection(intersections);
            return calcColor(closestIntersection);
        }
    }

    /**
     * Calculates the color at the intersection point.
     * The color is determined by combining the ambient light intensity and the emission color of the geometry.
     * The ambient light intensity is scaled by the ambient reflection coefficient (ka) of the geometry's material.
     *
     * @param intersection the intersection point containing the geometry and its material properties
     * @return the calculated color at the intersection point, including ambient light and emission
     */
    public Color calcColor(Intersection intersection) {
        return this.scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().ka)
                .add(intersection.geometry.getEmission());
    }
}