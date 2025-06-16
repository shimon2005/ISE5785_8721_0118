package renderer;

import geometries.Sphere;
import lighting.PointLight;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;
import renderer.BlackBoard.BoardShape;

import java.io.IOException;

public class DofTests {

    private final Camera.Builder cameraBuilder = Camera.getBuilder();

    /**
     * Test method for advanced depth of field effects
     */
    @Test
    public void DepthOfFieldTest() {

        Scene scene = new Scene("advanced depth of field test");

        Material mat = new Material().setKD(0.5).setKS(0.5).setShininess(100);

        scene.lights.add(new PointLight(new Color(150, 150, 150), new Point(20, 20, 20))); // Increased light intensity
        scene.geometries.add(
                new Sphere(new Point(10, 15, -80), 5.0).setEmission(new Color(100, 50, 50)).setMaterial(mat),
                new Sphere(new Point(5, 10, -40), 5.0).setEmission(new Color(100, 150, 50)).setMaterial(mat),
                new Sphere(new Point(0, 5, 0), 5.0).setEmission(new Color(50, 50, 100)).setMaterial(mat),
                new Sphere(new Point(-5, 0, 40), 5.0).setEmission(new Color(50, 100, 50)).setMaterial(mat),
                new Sphere(new Point(-10, -5, 80), 5.0).setEmission(new Color(50, 100, 100)).setMaterial(mat)
        );

        cameraBuilder
                .setLocation(new Point(-5, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(40, 40)
                .setBoardShape(BoardShape.CIRCLE)
                .setDepthOfField(160)
                .setApertureRadius(2)
                .setAmountOfRays_DOF(36)
                .setMultithreading(-1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build()
                .renderImage()
                .writeToImage("dof_test");
    }


    /**
     * Test method for advanced depth of field effects with AA
     */
    @Test
    public void CombinedDepthOfFieldAndAATest() {

        Scene scene = new Scene("advanced depth of field test");

        Material mat = new Material().setKD(0.5).setKS(0.5).setShininess(100);

        scene.lights.add(new PointLight(new Color(150, 150, 150), new Point(20, 20, 20))); // Increased light intensity
        scene.geometries.add(
                new Sphere(new Point(10, 15, -80), 5.0).setEmission(new Color(100, 50, 50)).setMaterial(mat),
                new Sphere(new Point(5, 10, -40), 5.0).setEmission(new Color(100, 150, 50)).setMaterial(mat),
                new Sphere(new Point(0, 5, 0), 5.0).setEmission(new Color(50, 50, 100)).setMaterial(mat),
                new Sphere(new Point(-5, 0, 40), 5.0).setEmission(new Color(50, 100, 50)).setMaterial(mat),
                new Sphere(new Point(-10, -5, 80), 5.0).setEmission(new Color(50, 100, 100)).setMaterial(mat)
        );

        cameraBuilder
                .setLocation(new Point(-5, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(40, 40)
                .setBoardShape(BoardShape.CIRCLE)
                .setDepthOfField(160)
                .setApertureRadius(2)
                .setAmountOfRays_DOF(16)
                .setAmountOfRays_AA(16)
                .setMultithreading(-1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build()
                .renderImage()
                .writeToImage("combined_dof_and_aa_test");
    }


    /**
     * Test method for depth of field effects with a JSON scene
     */
    @Test
    public void DepthOfFieldJsonTest() throws IOException, ParseException {

        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(40, 40)
                .setBoardShape(BoardShape.SQUARE)
                .setDepthOfField(160)
                .setApertureRadius(1)
                .setAmountOfRays_DOF(16)
                .setAmountOfRays_AA(16)
                .setMultithreading(-1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build()
                .renderImage()
                .writeToImage("dof_json_test");
    }


}
