package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

import java.io.IOException;

public class CombinedDofAndAATests {


    /**
     * Test method fot rendering a scene without depth of field and without anti-aliasing, using a JSON scene
     */
    @Test
    public void noDofNoAaJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("no_dof_no_aa_json_test");
    }



    /**
     * Test method for rendering a scene without depth of field and with non adaptive anti-aliasing, using a JSON scene
     */
    @Test
    public void noDofNonAdaptiveAaJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setUseAA(true)
                .setAmountOfRays_AA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("no_dof_non_adaptive_aa_json_test");
    }


    /**
     * Test method for rendering a scene with non adaptive depth of field and without anti-aliasing, using a JSON scene
     */
    @Test
    public void nonAdaptiveDofNoAaJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setUseDOF(true)
                .setDepthOfField(160)
                .setApertureRadius(1)
                .setAmountOfRays_DOF(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("non_adaptive_dof_no_aa_json_test");
    }



    /**
     * Test method for rendering a scene with non adaptive depth of field and non adaptive anti-aliasing, using a JSON scene
     */
    @Test
    public void nonAdaptiveDofNonAdaptiveAAJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build()
                .renderImage()
                .writeToImage("non_dof_non_aa_json_test");
    }



    /**
     * Test method for non adaptive depth of field and adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void nonAdaptiveDofAdaptiveAAJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setColorThresholdAdaptiveAA(2)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build()
                .renderImage()
                .writeToImage("non_dof_adaptive_aa_json_test");
    }


    /**
     * Test method for adaptive depth of field and non adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void adaptiveDofNonAdaptiveAAJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setUseDOF(true)
                .setDepthOfField(200)
                .setApertureRadius(1)
                .setUseAdaptiveSuperSamplingForDOF(true)
                .setColorThresholdAdaptiveDOF(2)
                .setNumOfSubAreaSamplesAdaptiveDOF(4)
                .setMaxSamplesAdaptiveDOF(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build()
                .renderImage()
                .writeToImage("adaptive_dof_non_aa_json_test");
    }


    /**
     * Test method for adaptive depth of field and adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void adaptiveDofAdaptiveAAJsonTest() throws IOException, ParseException {
        Camera.Builder cameraBuilder = Camera.getBuilder();
        Scene scene = JsonScene.importScene("unittests/scene/dof_json_scene.json");

        cameraBuilder
                .setLocation(new Point(-5, 10, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(75, 75)
                .setBoardShape(BlackBoard.BoardShape.SQUARE)
                .setUseDOF(true)
                .setUseAdaptiveSuperSamplingForDOF(true)
                .setDepthOfField(200)
                .setApertureRadius(1)
                .setUseAdaptiveSuperSamplingForDOF(true)
                .setColorThresholdAdaptiveDOF(2)
                .setNumOfSubAreaSamplesAdaptiveDOF(4)
                .setMaxSamplesAdaptiveDOF(64)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setColorThresholdAdaptiveAA(2)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build()
                .renderImage()
                .writeToImage("adaptive_dof_adaptive_aa_json_test");
    }
}
