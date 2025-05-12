package scene;

import geometries.*;
import lighting.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitives.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for importing a {@link Scene} from a JSON file.
 * Parses scene metadata, geometries, lights, and materials.
 */
public class JsonScene {

    private JsonScene() {
        // prevent instantiation
    }

    /**
     * Imports a {@link Scene} from a JSON file at the given path.
     *
     * @param path The path to the JSON file.
     * @return A fully constructed Scene.
     * @throws IOException    If the file cannot be read.
     * @throws ParseException If the JSON is malformed.
     */
    public static Scene importScene(String path) throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser()
                .parse(new FileReader(path));
        JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

        Scene scene = new Scene((String) sceneObj.get("name"));

        if (sceneObj.containsKey("background-color")) {
            scene.setBackground(parseColor((String) sceneObj.get("background-color")));
        }

        if (sceneObj.containsKey("ambient-light")) {
            JSONObject amb = (JSONObject) sceneObj.get("ambient-light");
            scene.setAmbientLight(new AmbientLight(parseColor((String) amb.get("color"))));
        }

        if (sceneObj.containsKey("geometries")) {
            scene.setGeometries(parseGeometries((JSONArray) sceneObj.get("geometries")));
        }

        if (sceneObj.containsKey("lights")) {
            scene.setLights(parseLights((JSONArray) sceneObj.get("lights")));
        }

        return scene;
    }

    /**
     * Parses an array of light definitions.
     *
     * @param lightsArray JSON array of light objects.
     * @return List of {@link LightSource}.
     */
    private static List<LightSource> parseLights(JSONArray lightsArray) {
        List<LightSource> list = new LinkedList<>();
        for (Object o : lightsArray) {
            JSONObject obj = (JSONObject) o;
            if (obj.containsKey("point")) {
                list.add(parsePointLight((JSONObject) obj.get("point")));
            } else if (obj.containsKey("directional")) {
                list.add(parseDirectionalLight((JSONObject) obj.get("directional")));
            } else if (obj.containsKey("spot")) {
                list.add(parseSpotLight((JSONObject) obj.get("spot")));
            } else {
                throw new IllegalArgumentException("Unknown light type in JSON");
            }
        }
        return list;
    }

    /**
     * Parses a point light from the JSON object.
     *
     * @param o the JSON object of the point light
     * @return the point light source
     */
    private static LightSource parsePointLight(JSONObject o) {
        PointLight pl = new PointLight(
                parseColor((String) o.get("color")),
                parsePoint((String) o.get("position"))
        );
        if (o.containsKey("kc")) pl.setKc(((Number) o.get("kc")).doubleValue());
        if (o.containsKey("kl")) pl.setKl(((Number) o.get("kl")).doubleValue());
        if (o.containsKey("kq")) pl.setKq(((Number) o.get("kq")).doubleValue());
        return pl;
    }

    /**
     * Parses a directional light from the JSON object.
     *
     * @param o the JSON object of the directional light
     * @return the directional light source
     */
    private static LightSource parseDirectionalLight(JSONObject o) {
        return new DirectionalLight(
                parseColor((String) o.get("color")),
                parseVector((String) o.get("direction"))
        );
    }

    /**
     * Parses the spotlight from the JSON object.
     *
     * @param lightObj the JSON object of the spotlight
     * @return the spotlight source
     */
    private static LightSource parseSpotLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Point position = parsePoint((String) lightObj.get("position"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        SpotLight spotLight = new SpotLight(color, position, direction);
        if (lightObj.containsKey("kc")) {
            spotLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            spotLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            spotLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        if (lightObj.containsKey("narrow-beam")) {
            spotLight.setNarrowBeam(((Number) lightObj.get("narrow-beam")).doubleValue());
        }
        return spotLight;
    }


    /**
     * Parses a JSON array of geometry definitions.
     * Each element must be a single-key object, e.g.:
     *   { "triangle": { ... } }
     *
     * @param arr JSON array
     * @return {@link Geometries}
     */
    private static Geometries parseGeometries(JSONArray arr) {
        Geometries geometries = new Geometries();
        for (Object o : arr) {
            JSONObject geoWrapper = (JSONObject) o;
            Geometry g;
            String type = (String) geoWrapper.keySet().iterator().next();
            JSONObject data = (JSONObject) geoWrapper.get(type);
            switch (type) {
                case "sphere" ->
                        g = parseSphere(data);
                case "triangle" ->
                        g = parseTriangle(data);
                case "plane" ->
                        g = parsePlane(data);
                case "polygon" ->
                        g = parsePolygon(data);
                case "cylinder" ->
                        g = parseCylinder(data);
                case "tube" ->
                        g = parseTube(data);
                default -> throw new IllegalArgumentException("Unknown geometry: " + type);
            }
            geometries.add(g);
        }
        return geometries;
    }

    /**
     * Parses a Sphere plus its optional material and emission.
     */
    private static Geometry parseSphere(JSONObject o) {
        Sphere s = new Sphere(
                parsePoint((String) o.get("center")),
                ((Number) o.get("radius")).doubleValue()
        );
        applyMaterialAndEmission(o, s);
        return s;
    }

    /**
     * Parses a Triangle plus its optional material and emission.
     * Expects a "points" array of exactly three strings.
     */
    private static Geometry parseTriangle(JSONObject o) {
        JSONArray pts = (JSONArray) o.get("points");
        if (pts.size() != 3)
            throw new IllegalArgumentException("Triangle must have exactly 3 points");
        Point[] p = parseVertices(pts);
        Triangle t = new Triangle(p[0], p[1], p[2]);
        applyMaterialAndEmission(o, t);
        return t;
    }

    /**
     * Parses a Plane plus its optional material and emission.
     * Expects "point" and "normal" strings.
     */
    private static Geometry parsePlane(JSONObject o) {
        Plane p = new Plane(
                parsePoint((String) o.get("point")),
                parseVector((String) o.get("normal"))
        );
        applyMaterialAndEmission(o, p);
        return p;
    }

    /**
     * Parses a Polygon plus its optional material and emission.
     * Expects "points" array of 3+ vertices.
     */
    private static Geometry parsePolygon(JSONObject o) {
        Polygon poly = new Polygon(parseVertices((JSONArray) o.get("points")));
        applyMaterialAndEmission(o, poly);
        return poly;
    }

    /**
     * Parses a Cylinder plus its optional material and emission.
     * Expects "axis" object with "origin" and "direction", plus "radius" and "height".
     */
    private static Geometry parseCylinder(JSONObject o) {
        Cylinder c = new Cylinder(
                ((Number) o.get("height")).doubleValue(),
                parseRay((JSONObject) o.get("axis")),
                ((Number) o.get("radius")).doubleValue()
        );
        applyMaterialAndEmission(o, c);
        return c;
    }

    /**
     * Parses a Tube plus its optional material and emission.
     * Parses a Tube plus its optional material and emission.
     * Expects "axis" object with "origin" and "direction", plus "radius".
     */
    private static Geometry parseTube(JSONObject o) {
        Tube t = new Tube(
                parseRay((JSONObject) o.get("axis")),
                ((Number) o.get("radius")).doubleValue()
        );
        applyMaterialAndEmission(o, t);
        return t;
    }

    /**
     * Shared helper to read optional "material" and "emission" from the geometry object.
     */
    private static void applyMaterialAndEmission(JSONObject o, Geometry g) {
        if (o.containsKey("material")) {
            JSONObject m = (JSONObject) o.get("material");
            Material mat = new Material();
            if (m.containsKey("kd")) mat.setKD(((Number) m.get("kd")).doubleValue());
            if (m.containsKey("ks")) mat.setKS(((Number) m.get("ks")).doubleValue());
            if (m.containsKey("ns")) mat.setShininess(((Number) m.get("ns")).intValue());
            if (m.containsKey("ka")) mat.setKa(((Number) m.get("ka")).doubleValue());  // הוספת התמיכה ל-ka
            g.setMaterial(mat);
        }
        if (o.containsKey("emission")) {
            g.setEmission(parseColor((String) o.get("emission")));
        }
    }


    // --- primitives parsing helpers ---

    /**
     * Parses a Ray from a JSON object.
     * Expects "origin" and "direction" strings.
     */
    private static Ray parseRay(JSONObject o) {
        return new Ray(
                parsePoint((String) o.get("origin")),
                parseVector((String) o.get("direction"))
        );
    }

    /**
     * Parses an array of vertices from a JSON array.
     * Expects each element to be a string with 3 coordinates.
     */
    private static Point[] parseVertices(JSONArray arr) {
        Point[] pts = new Point[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            pts[i] = parsePoint((String) arr.get(i));
        }
        return pts;
    }

    /**
     * Parses a Point from a string with 3 coordinates.
     * Expects format: "x y z".
     */
    private static Point parsePoint(String s) {
        double[] c = parseCoordinates(s);
        return new Point(c[0], c[1], c[2]);
    }

    /**
     * Parses a Vector from a string with 3 coordinates.
     * Expects format: "x y z".
     */
    private static Vector parseVector(String s) {
        double[] c = parseCoordinates(s);
        return new Vector(c[0], c[1], c[2]);
    }

    /**
     * Parses a Color from a string with 3 coordinates.
     * Expects format: "r g b".
     */
    private static Color parseColor(String s) {
        double[] c = parseCoordinates(s);
        return new Color(c[0], c[1], c[2]);
    }

    /**
     * Parses a string with 3 coordinates into a double array.
     * Expects format: "x y z".
     */
    private static double[] parseCoordinates(String str) {
        return Arrays.stream(str.split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
