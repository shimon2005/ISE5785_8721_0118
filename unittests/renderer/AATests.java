package renderer;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

import java.io.IOException;

public class AATests {

    /** Camera builder of the tests */
    private final Camera.Builder camera = Camera.getBuilder() //
            .setLocation(Point.ZERO)
            .setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
            .setVpDistance(100) //
            .setVpSize(500, 500)
            .setAmountOfRays_AA(121)
            .setMultithreading(-1);


    @Test
    void AAJson1() throws IOException, ParseException {
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
                .writeToImage("jsonRenderTest1WithAA");
    }

}
