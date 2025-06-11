package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    public void diamondRing() {
        assertDoesNotThrow(() -> {
                    Scene scene = JsonScene.importScene("unittests/scene/diamondRing.json");
                    final Camera.Builder camera = Camera.getBuilder()
                            .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 0.1, 1).normalize())
                            .setLocation(new Point(0, -350, 60))//Point(0, 130, 30)
                            .setVpDistance(500)
                            .setMultithreading(-1)
                            .setVpSize(150, 150)
                            .setResolution(1000, 1000)
                            .setDebugPrint(1)
                            .setRayTracer(scene,RayTracerType.SIMPLE);

                    camera
                            .build()
                            .renderImage()
                            .writeToImage("Diamond Ring");
                }, "Failed to render image"
        );
    }

    @Test
    public void house() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("unittests/scene/house.json");


            camera
                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -320, 40))
                    .setVpDistance(500)
                    .setMultithreading(-1)
                    .setVpSize(150, 150)
                    .setResolution(1000, 1000)
                    .setDebugPrint(1)
                    .setRayTracer(scene,RayTracerType.SIMPLE);

            camera
                    .build()
                    .renderImage()
                    .writeToImage("House");

        }, "Failed to render image");
    }

    @Test
    public void crown() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("unittests/scene/crown.json");

            camera
                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -320, 40))
                    .setVpDistance(500)
                    .setVpSize(150, 150)
                    .setResolution(1000, 1000)
                    .setDebugPrint(1)
                    .setRayTracer(scene,RayTracerType.SIMPLE)
                    .setMultithreading(-1);
            camera
                    .build()
                    .renderImage()
                    .writeToImage("Crown");

        }, "Failed to render image");
    }

}
