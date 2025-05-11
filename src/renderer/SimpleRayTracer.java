package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
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
            return calcColor(closestIntersection, ray);
        }
    }

    /**
     * Calculates the color at the given intersection point, including local lighting effects.
     *
     * The method first initializes the intersection data using the ray.
     * If the intersection is not valid (e.g., the surface is perpendicular to the ray), it returns black.
     * Otherwise, it calculates and returns the local lighting effects including diffuse and specular reflections.
     *
     * @param intersection the intersection point containing the geometry and its material properties
     * @param ray the ray that intersects with the geometry
     * @return the calculated color at the intersection point, or black if the intersection is invalid
     */
    public Color calcColor(Intersection intersection, Ray ray) {
        // Initialize intersection details
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        // Calculate viewer vector and dot product
        Vector v = intersection.rayDirection.normalize();
        Vector n = intersection.normal.normalize();
        double nv = intersection.rayDirectionDotProductNormal;

        // If the angle is zero (viewer perpendicular to surface), return black
        if (Util.isZero(nv)) {
            return Color.BLACK;
        }

        // Return only local lighting effects (diffuse + specular)
        return calcLocalEffects(intersection, n, v, nv);
    }


    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.rayDirectionDotProductNormal = intersection.rayDirection.dotProduct(intersection.normal);

        // if rayDirectionDotProductNormal is 0 return false, else return true
        return !Util.isZero(intersection.rayDirectionDotProductNormal);
    }

    public boolean setLightSource (Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point);
        intersection.lightDirectionDotProductNormal = intersection.lightDirection.dotProduct(intersection.normal);

        // if lightDirectionDotProductNormal is 0 return false, else return true
        return !Util.isZero(intersection.lightDirectionDotProductNormal);
    }

    /**
     * Calculates the local lighting effects on a given intersection point by combining
     * the geometry's emission color with the contributions from all light sources in the scene.
     * The contributions include diffusive and specular components if the conditions for light intensity
     * reflection are met.
     *
     * @param intersection the intersection point containing details about the geometry and its material
     * @param n the normal vector at the intersection point
     * @param v the direction vector from the intersection point back to the viewer
     * @param nv the dot product of the normal vector and the viewer direction vector
     * @return the calculated color at the intersection point after including the local lighting effects
     */
    private Color calcLocalEffects(Intersection intersection, Vector n, Vector v, double nv)
    {
        Material material = intersection.geometry.getMaterial();
        Color iA = scene.ambientLight.getIntensity();
        Double3 kA = intersection.material.kA;
        Color iE = intersection.geometry.getEmission();

        // Calculate the base color (only the contribution of the ambient light and the emission light)
        Color color = (iA.scale(kA)).add(iE);

        for (LightSource lightSource : scene.lights) {
            // Set the light source and calculate the light direction
            if (!setLightSource(intersection, lightSource)) {
                // Skip to the next lightSource in the scene if the light from the lightSource
                // in the current repetition of the loop does not reach the intersection point
                continue;
            }
            Vector l = lightSource.getL(intersection.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Color iL = lightSource.getIntensity(intersection.point);
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }


    /**
     * Calculates the diffuse reflection component using Lambert’s cosine law:
     * I_diffuse = kD * max(0, n ⋅ l)
     *
     * @param intersection the intersection containing material, normal, and light direction
     * @return the diffuse intensity factor as RGB Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        Vector n = intersection.normal.normalize();
        Vector l = intersection.lightDirection.normalize();
        double nl = Math.abs( n.dotProduct(l));
        return intersection.material.kD.scale(nl);
    }



    /**
     * Calculates the specular reflection component using the Phong reflection model:
     * I_specular = kS * (max(0, -v ⋅ r))^nSh
     * where r is the reflection of the light vector l about the normal n.
     *
     * @param intersection the intersection containing material, normal, view and light vectors
     * @return the specular intensity factor as RGB Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector v = intersection.rayDirection.normalize();         // view direction (from point to viewer)
        Vector l = intersection.lightDirection.normalize();       // light direction (from light to point)
        Vector n = intersection.normal.normalize();               // normal at the point

        // Compute reflection vector r = l - 2(n ⋅ l) * n
        Vector r = l.subtract(n.scale(2 * n.dotProduct(l))).normalize();

        double minusVR = -v.dotProduct(r); // use -v·r because v points outward
        if (Util.alignZero(minusVR) <= 0) {
            return Double3.ZERO;
        }

        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nSh));
    }


}