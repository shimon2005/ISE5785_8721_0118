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



    @Test
    void Json8() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene8.json");
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
                .writeToImage("jsonRenderTest8");
    }



    @Test
    void Json9() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene9.json");
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
                .writeToImage("jsonRenderTest9");
    }

    @Test
    void Json10() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/testScene10.json");
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
                .writeToImage("jsonRenderTest10");
    }

    @Test
    void JsonDiamondRing() throws IOException, ParseException {
        Scene scene = JsonScene.importScene("unittests/scene/diamondRing.json");

        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setLocation(new Point(0, 50, -3))
                .setDirection(new Vector(0, -1, 0).normalize(), new Vector(0, 0, 20).normalize())
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setVpDistance(100)
                .setVpSize(150, 150)
                .setResolution(800, 800);

        cameraBuilder
                .build()
                .renderImage()
                .writeToImage("diamond ring");
    }

    @Test
    public void diamond() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("jsonScenes/diamond.json");

            camera
                    .setImageWriter(new ImageWriter("diamond", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene))

                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -320, 40))
                    .setVpDistance(500)
                    .setAmountOfRaysAA(2)
                    .setVpSize(150, 150)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }
}
