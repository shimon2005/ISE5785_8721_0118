package renderer;

import primitives.Point;
import scene.Scene;
import primitives.Ray;
import primitives.Color;

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
     * Traces a ray through the scene and calculates the color at the intersection point.
     * If no intersection is found, the background color of the scene is returned.
     * else it returns the color at the intersection point using the method calcColor.
     *
     *
     * @param ray the ray to trace
     * @return the color at the intersection point or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = this.scene.geometries.findIntersections(ray);
        if (intersections == null) {
            return this.scene.background;
        } else {
            Point closestPoint = ray.findClosestPoint(intersections);
            return calcColor(closestPoint);
        }
    }

    /**
     * Calculates the color at a specific point in the scene.
     * Currently, this method only returns the ambient light intensity.
     *
     * @param point the point at which to calculate the color
     * @return the color at the specified point
     */
    public Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }
}