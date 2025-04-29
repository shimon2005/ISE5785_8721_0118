package renderer;

import primitives.Point;
import scene.Scene;
import primitives.Ray;
import primitives.Color;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = this.scene.geometries.findIntersections(ray);
        if(intersections == null) {
            return this.scene.background;
        }
        else {
            // Find the closest intersection point
            Point closestPoint = ray.findClosestPoint(intersections);
            return calcColor(closestPoint);
        }
    }

    public Color calcColor (Point point) {
        // Not implemented yet
        return this.scene.ambientLight.getIntensity();
    }


}
