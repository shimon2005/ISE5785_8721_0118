package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

import java.io.IOException;

import static java.awt.Color.YELLOW;

public class JsonTests {
    /** Camera builder of the tests */
    private final Camera.Builder camera = Camera.getBuilder() //
            .setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
            .setVpDistance(100) //
            .setVpSize(500, 500);


    @Test
    void Json1() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene1.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest1");
    }

    @Test
    void Json2() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene2.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest2");
    }

    @Test
    void Json3() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene3.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest3");
    }


    @Test
    void Json4() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene4.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest4");
    }


    @Test
    void Json5() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene5.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest5");
    }


    @Test
    void Json6() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene6.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest6");
    }

    @Test
    void Json7() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene7.json");
        // enter XML file name and parse from JSON file into a scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("jsonRenderTest7");
    }
}
