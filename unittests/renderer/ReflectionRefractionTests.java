package renderer;

import static java.awt.Color.*;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.JsonScene;
import scene.Scene;

import java.io.IOException;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
      .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
                           new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                              .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
                           new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.lights.add( //
                       new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                          .setKl(0.0004).setKq(0.0000006));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(150, 150) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("refractionTwoSpheres");
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
                           new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                 .setKT(new Double3(0.5, 0, 0))), //
                           new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                              .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(670, 670, 3000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(1)), //
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                                        new Point(-1500, -1500, -2000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
         .setKl(0.00001).setKq(0.000005));

      cameraBuilder
         .setLocation(new Point(0, 0, 10000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(10000).setVpSize(2500, 2500) //
         .setResolution(500, 500) //
         .build() //
         .renderImage() //
         .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
                           new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                        new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                           new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                              .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.lights.add(
                       new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                          .setKl(4E-5).setKq(2E-7));

      cameraBuilder
         .setLocation(new Point(0, 0, 1000)) //
         .setDirection(Point.ZERO, Vector.AXIS_Y) //
         .setVpDistance(1000).setVpSize(200, 200) //
         .setResolution(600, 600) //
         .build() //
         .renderImage() //
         .writeToImage("refractionShadow");
   }

   @Test
   void reflectionTransparencyCombinedTest() {
      // Create a new scene with ambient light and black background
      Scene scene = new Scene("ReflectionTransparencyCombined");
      scene.setBackground(new Color(0, 0, 0));
      scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

      // Add a large semi-transparent blue sphere
      scene.geometries.add(
              new Sphere(new Point(-1, 0, -5), 2d)
                      .setEmission(new Color(0, 0, 255))
                      .setMaterial(new Material()
                              .setKD(0.3)
                              .setKS(0.7)
                              .setShininess(100)
                              .setKT(0.5) // Partial transparency
                              .setKR(Double3.ZERO) // No reflection
                      )
      );

      // Add a medium red reflective sphere
      scene.geometries.add(
              new Sphere(new Point(2.5, 0, -6), 1.5)
                      .setEmission(new Color(255, 0, 0))
                      .setMaterial(new Material()
                              .setKD(0.4)
                              .setKS(0.8)
                              .setShininess(200)
                              .setKT(Double3.ZERO) // Opaque
                              .setKR(new Double3(0.8, 0.8, 0.8)) // Strong reflection
                      )
      );

      // Add a ground plane with mild reflection
      scene.geometries.add(
              new Plane(new Point(0, -2, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(50, 50, 50))
                      .setMaterial(new Material()
                              .setKD(0.7)
                              .setKS(0.5)
                              .setShininess(60)
                              .setKR(new Double3(0.3, 0.3, 0.3)) // Mild reflection on floor
                              .setKT(Double3.ZERO)
                      )
      );

      // Add a small green sphere with high transparency
      scene.geometries.add(
              new Sphere(new Point(0, 0.5, -4), 0.7)
                      .setEmission(new Color(0, 255, 0))
                      .setMaterial(new Material()
                              .setKD(0.4)
                              .setKS(0.4)
                              .setShininess(50)
                              .setKT(0.7) // High transparency
                              .setKR(Double3.ZERO)
                      )
      );

      // Add a white point light with attenuation
      scene.lights.add(
              new PointLight(new Color(255, 255, 255), new Point(4, 7, -7))
                      .setKc(1).setKl(0.001).setKq(0.0001)
      );

      // Setup camera with given specs, Y axis is up
      Camera camera = Camera.getBuilder()
              .setLocation(Point.ZERO)
              .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
              .setVpDistance(100)
              .setVpSize(500, 500)
              .setRayTracer(scene, RayTracerType.SIMPLE)
              .setResolution(1000, 1000)
              .build();

      // Render image, print grid, write output file
      camera.renderImage()
              //.printGrid(100, new Color(java.awt.Color.YELLOW))
              .writeToImage("reflectionTransparencyCombinedTest");
   }

}
