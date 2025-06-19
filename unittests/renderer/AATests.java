package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;
import renderer.BlackBoard.BoardShape;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AATests {

    /** Cameras builders for testing */
    private final Camera.Builder camera1 = Camera.getBuilder() //
            .setLocation(Point.ZERO)
            .setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
            .setVpDistance(100) //
            .setVpSize(500, 500)
            .setBoardShape(BoardShape.SQUARE);

    private final Camera.Builder camera2 = Camera.getBuilder() //
            .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
            .setLocation(new Point(0, -320, 40))
            .setVpDistance(500)
            .setVpSize(150, 150)
            .setBoardShape(BoardShape.CIRCLE);



    @Test
    void AAWithJsonScene1() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene1.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera1 //
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(false)
                .setAmountOfRays_AA(256)
                .setMultithreading(-1)
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest1WithAA");
    }


    @Test
    void adaptiveSuperSamplingForAAWithJsonScene1() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene1.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera1 //
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setMaxSamplesAdaptiveAA(256)
                .setColorThresholdAdaptiveAA(10)
                .setMultithreading(-1)
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest1WithAdaptiveSamplingForAA");
    }

    @Test
    void AAWithJsonScene4() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene4.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera1 //
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(false)
                .setAmountOfRays_AA(256)
                .setMultithreading(-1)
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest4WithAA");
    }


    @Test
    void adaptiveSuperSamplingForAAWithJsonScene4() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene4.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera1 //
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setMaxSamplesAdaptiveAA(256)
                .setColorThresholdAdaptiveAA(2)
                .setMultithreading(-1)
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(2000, 2000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest4WithAdaptiveSamplingForAA");
    }

/*
    @Test
    public void crownAA() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("unittests/scene/crown.json");

            camera2
                    .setResolution(1000, 1000)
                    .setUseAA(true)
                    .setUseAdaptiveSuperSamplingForAA(false)
                    .setAmountOfRays_AA(36)
                    .setDebugPrint(1)
                    .setRayTracer(scene,RayTracerType.SIMPLE)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage("CrownAA");

        }, "Failed to render image");
    }


    @Test
    public void crownAdaptiveAA() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("unittests/scene/crown.json");

            camera2
                    .setResolution(1000, 1000)
                    .setUseAA(true)
                    .setUseAdaptiveSuperSamplingForAA(true)
                    .setMaxSamplesAdaptiveAA(36)
                    .setColorThresholdAdaptiveAA(10)
                    .setDebugPrint(1)
                    .setRayTracer(scene,RayTracerType.SIMPLE)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage("CrownAdaptiveAA");

        }, "Failed to render image");
    }

 */

}
