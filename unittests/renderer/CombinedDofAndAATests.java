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
     * Test method for combined depth of field without anti-aliasing effects, using a JSON scene
     */
    @Test
    public void DepthOfFieldWithoutAAJsonTestForComparison() throws IOException, ParseException {
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
                .writeToImage("dof_without_aa_json_test_for_comparison");
    }

    /**
     * Test method for combined depth of field and anti-aliasing effects, using a JSON scene
     */
    @Test
    public void nonAdaptiveDepthOfFieldAndNonAdaptiveAAJsonTest() throws IOException, ParseException {
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
                .setAmountOfRays_DOF(16)
                .setUseAA(true)
                .setAmountOfRays_AA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("non_adaptive_dof_non_adaptive_aa_json_test");
    }


    /**
     * Test method for depth of field and adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void nonAdaptiveDepthOfFieldAndAdaptiveAAJsonTest() throws IOException, ParseException {
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
                .setAmountOfRays_DOF(16)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setColorThresholdAdaptiveAA(2)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("non_adaptive_dof_adaptive_aa_json_test");
    }

    /**
     * Test method for depth of field and adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void adaptiveDepthOfFieldAndNonAdaptiveAAJsonTest() throws IOException, ParseException {
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
                .setUseAdaptiveSuperSamplingForDOF(true)
                .setNumOfSubAreaSamplesAdaptiveDOF(4)
                .setMaxSamplesAdaptiveDOF(16)
                .setUseAA(true)
                .setAmountOfRays_AA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("adaptive_dof_non_adaptive_aa_json_test");
    }

    /**
     * Test method for depth of field and adaptive anti-aliasing effects, using a JSON scene
     */
    @Test
    public void adaptiveDepthOfFieldAndAdaptiveAAJsonTest() throws IOException, ParseException {
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
                .setDepthOfField(160)
                .setApertureRadius(1)
                .setUseAdaptiveSuperSamplingForDOF(true)
                .setNumOfSubAreaSamplesAdaptiveDOF(4)
                .setMaxSamplesAdaptiveDOF(16)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setColorThresholdAdaptiveAA(2)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setMultithreading(-1)
                .setDebugPrint(1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(500, 500) //
                .build()
                .renderImage()
                .writeToImage("adaptive_dof_adaptive_aa_json_test");
    }
}
