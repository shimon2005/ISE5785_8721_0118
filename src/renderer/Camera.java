package renderer;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import primitives.Util;
import primitives.Color;
import scene.Scene;

import java.awt.*;
import java.util.MissingResourceException;

/**
 * Represents a camera in 3D space, capable of generating rays through a view plane.
 * Provides a builder for flexible configuration.
 */
public class Camera implements Cloneable {
    // Camera fields
    private Point location; // The position of the camera in 3D space
    private Vector vTo;     // The forward direction vector
    private Vector vUp;     // The upward direction vector
    private Vector vRight;  // The rightward direction vector
    private ImageWriter imageWriter; // The image writer for rendering images
    private RayTracerBase rayTracer; // The ray tracer for rendering
    private int nX; // Number of pixels in width
    private int nY; // Number of pixels in height

    // View plane fields
    private double viewPlaneHeight = 0.0; // Height of the view plane
    private double viewPlaneWidth = 0.0;  // Width of the view plane
    private double vpDistance = 0.0;      // Distance from the camera to the view plane

    /**
     * Builder class for constructing a Camera instance with a fluent API.
     */
    public static class Builder {
        final Camera camera = new Camera();

        /**
         * Sets the camera's location in 3D space.
         * @param point the location of the camera
         * @return the builder instance
         */
        public Builder setLocation(Point point) {
            this.camera.location = point;
            return this;
        }

        /**
         * Sets the camera's direction using vTo and vUp vectors.
         * @param vTo the forward direction vector
         * @param vUp the upward direction vector
         * @return the builder instance
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            this.camera.vTo = vTo.normalize();
            this.camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the camera's direction using a target point and vUp vector.
         * @param target the target point the camera looks at
         * @param vUp the upward direction vector
         * @return the builder instance
         * @throws IllegalArgumentException if the target point is the same as the camera location
         */
        public Builder setDirection(Point target, Vector vUp) {
            if (target == this.camera.location) {
                throw new IllegalArgumentException("the target point cannot be the camera position");
            }
            this.camera.vTo = target.subtract(this.camera.location).normalize();
            Vector vRight = (this.camera.vTo).crossProduct(vUp);    // no need to normalize since it is only a temporary vector and not the actual vRight field
            this.camera.vUp = vRight.crossProduct(this.camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the camera's direction using a target point.
         * @param target the target point the camera looks at
         * @return the builder instance
         * @throws IllegalArgumentException if the target point is the same as the camera location
         */
        public Builder setDirection(Point target) {
            if (target == this.camera.location) {
                throw new IllegalArgumentException("the target point cannot be the camera position");
            }
            this.camera.vUp = new Vector(0, 1, 0);
            this.camera.vTo = target.subtract(this.camera.location).normalize();
            Vector vRight = (this.camera.vTo).crossProduct(this.camera.vUp);    // no need to normalize since it is only a temporary vector and not the actual vRight field
            this.camera.vUp = (vRight).crossProduct(this.camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the view plane's size.
         * @param height the height of the view plane
         * @param width the width of the view plane
         * @return the builder instance
         * @throws IllegalArgumentException if height or width is less than or equal to zero
         */
        public Builder setVpSize(double height, double width) {
            if (height <= 0 || width <= 0) {
                throw new IllegalArgumentException("height and width must be greater than zero");
            }
            this.camera.viewPlaneHeight = height;
            this.camera.viewPlaneWidth = width;
            return this;
        }

        /**
         * Sets the distance between the camera and the view plane.
         * @param vpDistance the distance to the view plane
         * @return the builder instance
         * @throws IllegalArgumentException if vpDistance is less than or equal to zero
         */
        public Builder setVpDistance(double vpDistance) {
            if (vpDistance <= 0.0) {
                throw new IllegalArgumentException("vpDistance must be greater than zero");
            }
            this.camera.vpDistance = vpDistance;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         * @param rayTracerType the ray tracer to be used
         * @param scene the scene to be rendered
         * @return the builder instance
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
           switch (rayTracerType) {
               case SIMPLE:
                     this.camera.rayTracer = new SimpleRayTracer(scene);
                     break;

               case GRID:
                     this.camera.rayTracer = null;
                     break;

                default:
                     throw new IllegalArgumentException("Unknown ray tracer type");
              }

            return this;
        }

        /**
         * Sets the resolution of the view plane.
         * @param nX the number of horizontal pixels
         * @param nY the number of vertical pixels
         * @return the builder instance
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("nX and nY must be greater than zero");
            }
            this.camera.nX = nX;
            this.camera.nY = nY;
            this.camera.imageWriter = new ImageWriter(nX, nY);
            return this;
        }

        /**
         * Builds and returns the configured Camera instance.
         * @return the constructed Camera
         * @throws MissingResourceException if any required field is missing
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Camera build() {
            final String camFieldMissingMsg = "The camera field is missing";
            if (this.camera.location == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "Camera location");
            }
            if (this.camera.vTo == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "Vector vTo");
            }
            if (this.camera.vUp == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "Vector vUp");
            }
            if (Util.alignZero(camera.viewPlaneHeight) <= 0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "View plane Height");
            }
            if (Util.alignZero(camera.viewPlaneWidth) <= 0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "View plane Width");
            }
            if (Util.alignZero(camera.vpDistance) <= 0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera", "view plane to camera distance");
            }

            // Validate orthogonality of vTo and vUp
            if (!Util.isZero(camera.vTo.dotProduct(camera.vUp))) {
                throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");
            }

            // Calculate vRight
            // No need to normalize since vTo and vUp are both normalized and are orthogonal to each other,
            // so the result of their cross product will be a unit vector.
            this.camera.vRight = (this.camera.vTo).crossProduct(this.camera.vUp);
            try {
                return (Camera) this.camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning is not supported for Camera", e);
            }
        }
    }

    /**
     * Private constructor to enforce the use of the Builder.
     */
    private Camera() {}

    /**
     * Returns a new Builder instance for constructing a Camera.
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through pixel (j,i) on an nX×nY view plane.
     *
     * @param nX number of pixels in width
     * @param nY number of pixels in height
     * @param j  pixel column (0..nX-1)
     * @param i  pixel row    (0..nY-1)
     * @return the ray through the center of that pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // 1) Compute center point of view plane: P_c = P0 + vTo * d
        Point pc = location.add(vTo.scale(vpDistance));

        // 2) Pixel size
        double rX = viewPlaneWidth / nX;
        double rY = viewPlaneHeight / nY;

        // 3) Pixel’s center offsets from P_c
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        // 4) Compute P_ij = P_c + xJ * vRight + yI * vUp
        Point pij = pc;
        if (!Util.isZero(xJ)) pij = pij.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pij = pij.add(vUp.scale(yI));

        // 5) Ray direction is from P0 toward P_ij
        Vector dir = pij.subtract(location).normalize();

        return new Ray(location, dir);
    }

    public Camera renderImage (){
        for(int x = 0; x < nX; ++x){
            for(int y = 0; y < nY; ++y){
                castRay(x, y);
            }
        }
        return this;
    }

    /**
     * Draws a grid on the image by coloring horizontal and vertical lines at fixed intervals.
     *
     * @param interval the spacing in pixels between the grid lines
     * @param color    the color of the grid lines
     */
    public Camera printGrid(int interval, Color color) {
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                if (x % interval == 0 || y % interval == 0) {
                    imageWriter.writePixel(x, y, color);
                }
            }
        }
        return this;
    }


    public Camera writeToImage (String imageName){
        imageWriter.writeToImage(imageName);
        return this;
    }

    public void castRay (int x, int y) {
        Ray ray = constructRay(nX, nY, x, y);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(x, y, color);
    }


}