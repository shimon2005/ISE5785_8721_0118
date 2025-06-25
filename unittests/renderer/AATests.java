package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;
import renderer.BlackBoard.BoardShape;

import java.io.IOException;

public class AATests {



    @Test
    void AAWithJsonScene1() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene1.json");

        Camera.Builder cameraBuilder = Camera.getBuilder();

        cameraBuilder //
                .setLocation(Point.ZERO)
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)
                .setVpDistance(100) //
                .setVpSize(500, 500)
                .setBoardShape(BoardShape.SQUARE)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(false)
                .setAmountOfRays_AA(64)
                .setMultithreading(-1);

        cameraBuilder //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest1WithAA");
    }


    @Test
    void adaptiveSuperSamplingForAAWithJsonScene1() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene1.json");

        Camera.Builder cameraBuilder = Camera.getBuilder();

        cameraBuilder //
                .setLocation(Point.ZERO)
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)
                .setVpDistance(100) //
                .setVpSize(500, 500)
                .setBoardShape(BoardShape.SQUARE)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setColorThresholdAdaptiveAA(10)
                .setMultithreading(-1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest1WithAdaptiveSamplingForAA");
    }

    @Test
    void AAWithJsonScene4() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene4.json");

        Camera.Builder cameraBuilder = Camera.getBuilder();

        cameraBuilder //
                .setLocation(Point.ZERO)
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)
                .setVpDistance(100) //
                .setVpSize(500, 500)
                .setBoardShape(BoardShape.SQUARE)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(false)
                .setAmountOfRays_AA(64)
                .setMultithreading(-1);

        cameraBuilder //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest4WithAA");
    }


    @Test
    void adaptiveSuperSamplingForAAWithJsonScene4() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene4.json");

        Camera.Builder cameraBuilder = Camera.getBuilder();

        cameraBuilder //
                .setLocation(Point.ZERO)
                .setDirection(new Point(0, 0, -1), Vector.AXIS_Y)
                .setVpDistance(100) //
                .setVpSize(500, 500)
                .setBoardShape(BoardShape.SQUARE)
                .setUseAA(true)
                .setUseAdaptiveSuperSamplingForAA(true)
                .setNumOfSubAreaSamplesAdaptiveAA(4)
                .setMaxSamplesAdaptiveAA(64)
                .setColorThresholdAdaptiveAA(10)
                .setMultithreading(-1);

        cameraBuilder
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest4WithAdaptiveSamplingForAA");
    }

}
