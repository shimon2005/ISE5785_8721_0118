package renderer;

import lighting.DirectionalLight;
import lighting.LightSource;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.Intersection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple implementation of a ray tracer for rendering a 3D scene.
 * This class calculates the color of a ray by finding its intersections
 * with geometries in the scene and determining the closest intersection point.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;


    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * A small value used to avoid floating-point precision issues.
     * This value is used to determine if two floating-point numbers are close enough to be considered equal.
     */




    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = this.scene.geometries.calculateIntersections(ray);
        return ray.findClosestIntersection(intersections);
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
        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null) {
            return scene.background;
        }
        return calcColor(closestIntersection, ray);
    }

    /**
     * Calculates the color at the given intersection point, including local lighting effects.
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

        Color iA = scene.ambientLight.getIntensity();
        Double3 kA = intersection.material.kA;

        return iA.scale(kA).add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }


    /**
     * Calculates the color at the given intersection point, including local lighting effects.
     * The method first initializes the intersection data using the ray.
     * If the intersection is not valid (e.g., the surface is perpendicular to the ray), it returns black.
     * Otherwise, it calculates and returns the local lighting effects including diffuse and specular reflections.
     *
     * @param intersection the intersection point containing the geometry and its material properties
     * @param level the recursion level for reflection/refraction
     * @param k the reflection/refraction coefficient
     * @return the calculated color at the intersection point, or black if the intersection is invalid
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }



    /**
     * Preprocesses the intersection data by calculating the ray direction, normal vector,
     * @param intersection the intersection point containing the geometry and its material properties
     * @param rayDirection the direction of the ray from the camera that intersects with the geometry
     * @return true if the intersection is valid (not perpendicular), false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection.normalize();
        intersection.n = intersection.geometry.getNormal(intersection.point);
        intersection.nv = Util.alignZero(intersection.n.dotProduct(intersection.v));

        // if rayDirectionDotProductNormal is 0 return false, else return true
        return intersection.nv != 0;
    }

    /**
     * Sets the light source for the intersection and calculates the light direction and its dot product with the normal.
     * If the light direction is perpendicular to the normal, it returns false.
     *
     * @param intersection the intersection point containing the geometry and its material properties
     * @param lightSource the light source to be set
     * @return true if the light direction is not perpendicular to the normal, false otherwise
     */
    private boolean setLightSource (Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.l = lightSource.getL(intersection.point); // no need to normalize since getL() returns a normalized vector
        intersection.nl = Util.alignZero(intersection.n.dotProduct(intersection.l));

        return intersection.nl * intersection.nv > 0;
    }

    /**
     * Calculates the local lighting effects on a given intersection point by combining
     * the geometry's emission color with the contributions from all light sources in the scene.
     * The contributions include diffusive and specular components if the conditions for light intensity
     * reflection are met.
     *
     * @param intersection the intersection point containing details about the geometry and its material
     * @return the calculated color at the intersection point after including the local lighting effects
     */
    private Color calcLocalEffects(Intersection intersection, Double3 k)
    {
        // Calculate the base color (only the contribution of the emission light)
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            // Set the light source and calculate the light direction
            if (!setLightSource(intersection, lightSource)) {
                // Skip to the next lightSource in the scene if the light from the lightSource
                // in the current repetition of the loop does not reach the intersection point,
                continue;
            }

            // find the shadowing factor (number in [0,1] range that represents the amount of light that reaches the intersection point)
            // 0 - no light, 1 - full light
            Double3 ktr = transparency(intersection);

            // if ktr * k is lower than MIN_CALC_COLOR_K, the intersection point is considered to be in shadow
            if (ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                continue;
            }
            Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
            color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));

        }
        return color;
    }


    /**
     * Calculates the diffuse reflection component using Lambert’s cosine law:
     * I_diffuse = kD * math.abs(nl)
     *
     * @param intersection the intersection containing material, normal, and light direction
     * @return the diffuse intensity factor as RGB Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.nl));
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

        // Compute reflection vector r = l - 2(n ⋅ l) * n (normalized)
        Vector r = (intersection.l.subtract(intersection.n.scale(2 * intersection.nl))).normalize();

        double minusVR = -(intersection.v.dotProduct(r)); // use -v·r because v points outward
        if (Util.alignZero(minusVR) <= 0) {
            return Double3.ZERO;
        }

        // Reach here if minusVR > 0
        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nSh));
    }


    /**
     * Determines whether a given intersection point is unshaded (i.e., not in shadow).
     *
     * To avoid self-shadowing artifacts ("shadow acne"), the method slightly offsets
     * the intersection point along the surface normal before casting a shadow ray
     * toward the light source.
     *
     * - If the light source is a {@link DirectionalLight}, the method checks whether any geometry
     *   blocks the shadow ray in the light's direction.
     * - If the light source is a {@link PointLight} or a subclass (e.g., {@link SpotLight}),
     *   the method checks whether any object blocks the ray before it reaches the light source.
     *
     * @param intersection the intersection details including the point, normal, light, etc.
     * @return {@code true} if the point is not in shadow (light is visible), {@code false} otherwise.
     */
    private boolean unshaded(Intersection intersection) {

        // Direction from point to light
        Vector pointToLight = intersection.l.scale(-1);

        // -intersection.nl has the same sign as the dot product of n and the light direction,
        Ray shadowRay = new Ray(intersection.point ,pointToLight, intersection.n, -intersection.nl);

        // distance from the light source to the intersection point
        double distanceToLightSource = intersection.lightSource.getDistance(intersection.point);

        List<Intersection> intersectionsInDistance = scene.geometries
                .calculateIntersections(shadowRay, distanceToLightSource);

        // Check if there is a blocking object that is opaque "enough" to cause shadowing
        // (that his kt is lower than MIN_CALC_COLOR_K).
        // This is a very limited and somewhat unrealistic shadow calculation method,
        // primarily useful to ensure that highly transparent objects do not cast shadows.
        for (Intersection i : intersectionsInDistance) {
            if (i.material.kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }

        return true;
    }



    private Double3 transparency(Intersection intersection) {
        Double3 ktr = Double3.ONE;

        // Direction from point to light
        Vector pointToLight = intersection.l.scale(-1);

        // -intersection.nl has the same sign as the dot product of n and the light direction
        Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.n, -intersection.nl);

        // Distance from the light source to the intersection point
        double distanceToLightSource = intersection.lightSource.getDistance(intersection.point);

        // Get intersections along the shadow ray up to the light source
        List<Intersection> intersectionsInDistance = scene.geometries
                .calculateIntersections(shadowRay, distanceToLightSource);

        // No intersections -> full transparency
        if (intersectionsInDistance == null || intersectionsInDistance.isEmpty()) {
            return ktr;
        }

        // Loop through intersections and calculate cumulative transparency
        for (Intersection i : intersectionsInDistance) {

            double distanceToIntersection = i.point.distance(intersection.point);
            ktr = ktr.product(i.material.kT);

            // Performance shortcut: exit early if ktr is very small
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }

        return ktr;
    }



    /**
     * Constructs the reflected ray from an intersection point using the Phong reflection model.
     *
     * @param intersection the intersection containing point, normal, and view vector
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector normal = intersection.n;
        Vector v = intersection.v;
        double nv = intersection.nv;

        // Reflection direction: r = v - 2(n ⋅ v) * n
        Vector reflectedDir = v.subtract(normal.scale(2 * nv)).normalize();

        // Offset the intersection point slightly along the normal to avoid self-shadowing
        // -nv has the same sign as the dot product of n and the reflection direction,
        return new Ray(intersection.point ,reflectedDir, normal, -nv);
    }



    /**
     * Constructs the transparency (refracted) ray from an intersection point.
     * Assumes simple straight-through refraction (no bending).
     *
     * @param intersection the intersection containing point, normal, and view vector
     * @return the transparency ray
     */
    private Ray constructRefractedRay(Intersection intersection) {

        Vector refractedDir = intersection.v;

        // Offset the intersection point slightly along the normal to avoid self-shadowing
        // nv has the same sign as the dot product of n and the refractedDir
        return new Ray(intersection.point ,refractedDir , intersection.n, intersection.nv);
    }


    /**
     * Calculates the global effects (reflection and refraction) at the intersection point.
     * It constructs the reflected and refracted rays and calculates their contributions
     * to the color at the intersection point.
     *
     * @param intersection the intersection point containing details about the geometry and its material
     * @param level the recursion level for reflection/refraction
     * @param k the reflection/refraction coefficient
     * @return the calculated color at the intersection point after including global effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(constructRefractedRay(intersection),
            level, k, intersection.material.kT)
            .add(calcGlobalEffect(constructReflectedRay(intersection),
                    level, k, intersection.material.kR));
    }


    /**
     * Calculates the global effect (reflection or refraction) at the intersection point.
     * It constructs the reflected or refracted ray and calculates its contribution
     * to the color at the intersection point.
     *
     * @param ray the ray to be traced
     * @param level the recursion level for reflection/refraction
     * @param k the reflection/refraction coefficient
     * @param kx the material property (kT or kR)
     * @return the calculated color at the intersection point after including global effects
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);

        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;

        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null)
            return scene.background.scale(kkx);

        return preprocessIntersection(closestIntersection, ray.getDirection())
                ? calcColor(closestIntersection, level - 1, kkx).scale(kkx) : Color.BLACK;
    }




}

