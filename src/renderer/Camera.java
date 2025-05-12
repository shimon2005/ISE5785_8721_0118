package renderer;

            import primitives.Point;
            import primitives.Vector;
            import primitives.Ray;
            import primitives.Util;
            import primitives.Color;
            import scene.Scene;

            import java.util.MissingResourceException;

            /**
             * Represents a camera in 3D space, capable of generating rays through a view plane.
             * Provides a builder for flexible configuration.
             */
            public class Camera implements Cloneable {
                // Camera fields
                /** The position of the camera in 3D space. */
                private Point location = null;

                /** The forward direction vector of the camera. */
                private Vector vTo = null;

                /** The upward direction vector of the camera. */
                private Vector vUp = null;

                /** The rightward direction vector of the camera. */
                private Vector vRight = null;

                /** The image writer for rendering images. */
                private ImageWriter imageWriter = null;

                /** The ray tracer for rendering the scene. */
                private RayTracerBase rayTracer = null;

                /** Number of pixels in the width of the view plane. */
                private int nX = 1;

                /** Number of pixels in the height of the view plane. */
                private int nY = 1;

                // View plane fields
                /** Height of the view plane. */
                private double viewPlaneHeight = 0.0;

                /** Width of the view plane. */
                private double viewPlaneWidth = 0.0;

                /** Distance from the camera to the view plane. */
                private double vpDistance = 0.0;

                /**
                 * Builder class for constructing a Camera instance with a fluent API.
                 */
                public static class Builder {
                    /** The camera instance being built. */
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
                     * @param vUp an indicator of the camera's up direction, it is not guaranteed to be orthogonal to vTo
                     * @return the builder instance
                     * @throws IllegalArgumentException if the target point is the same as the camera location
                     */
                    public Builder setDirection(Point target, Vector vUp) {
                        if (this.camera.location == null) {
                            throw new IllegalStateException("Camera location must be set before setting direction with target point.");
                        }
                        if (target.equals(this.camera.location)) {
                            throw new IllegalArgumentException("the target point cannot be the camera position");
                        }
                        this.camera.vTo = target.subtract(this.camera.location).normalize();
                        Vector vRight = (this.camera.vTo).crossProduct(vUp);

                        // we need to recalculate vUp to ensure orthogonality, because the vUp vector in the parameter
                        // is not guaranteed to be orthogonal to vTo and is only used to calculate vRight
                        this.camera.vUp = vRight.crossProduct(this.camera.vTo).normalize();
                        return this;
                    }

                    /**
                     * Sets the camera's direction using a target point.
                     * @param target the target point the camera looks at
                     * @return the builder instance
                     * @throws IllegalArgumentException if the target point is the same as the camera location
                     */
                    //to fix
                    public Builder setDirection(Point target) {
                        if (this.camera.location == null) {
                            throw new IllegalStateException("Camera location must be set before setting direction with target point.");
                        }
                        if (target.equals(this.camera.location)) {
                            throw new IllegalArgumentException("the target point cannot be the camera position");
                        }
                        this.camera.vUp =Vector.AXIS_Y;
                        this.camera.vTo = target.subtract(this.camera.location).normalize();
                        Vector vRight = (this.camera.vTo).crossProduct(this.camera.vUp);
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

                        if (!Util.isZero(camera.vTo.dotProduct(camera.vUp))) {
                            throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");
                        }

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
                    Point pc = location.add(vTo.scale(vpDistance));
                    double rX = viewPlaneWidth / nX;
                    double rY = viewPlaneHeight / nY;
                    double xJ = (j - (nX - 1) / 2.0) * rX;
                    double yI = -(i - (nY - 1) / 2.0) * rY;
                    Point pij = pc;
                    if (!Util.isZero(xJ)) pij = pij.add(vRight.scale(xJ));
                    if (!Util.isZero(yI)) pij = pij.add(vUp.scale(yI));
                    Vector dir = pij.subtract(location).normalize();
                    return new Ray(location, dir);
                }

                /**
                 * Renders the image by casting rays through each pixel.
                 * @return the camera instance
                 */
                public Camera renderImage() {
                    for (int x = 0; x < nX; ++x) {
                        for (int y = 0; y < nY; ++y) {
                            castRay(x, y);
                        }
                    }
                    return this;
                }

                /**
                 * Draws a grid on the image with the specified interval and color.
                 * Grid lines are drawn every 'interval' pixels, meaning the spacing between them is (interval - 1) pixels.
                 * For example, if interval = 10, grid lines appear at x = 0, 10, 20, ..., and spacing between lines is 9 pixels.
                 *
                 * @param interval The distance in pixels between consecutive grid lines.
                 * @param color    The color used to draw the grid lines.
                 * @return         The current Camera instance (for method chaining).
                 */

                public Camera printGrid(int interval, Color color) {
                    // Draw vertical grid lines
                    for (int x = 0; x < nX; x += interval) {
                        for (int y = 0; y < nY; y++) {
                            imageWriter.writePixel(x, y, color);
                        }
                    }

                    // Draw horizontal grid lines
                    for (int y = 0; y < nY; y += interval) {
                        for (int x = 0; x < nX; x++) {
                            imageWriter.writePixel(x, y, color);
                        }
                    }

                    return this;
                }


                /**
                 * Writes the rendered image to a file.
                 * @param imageName the name of the output image file
                 * @return the camera instance
                 */
                public Camera writeToImage(String imageName) {
                    imageWriter.writeToImage(imageName);
                    return this;
                }

                /**
                 * Casts a ray through a specific pixel and writes the resulting color to the image.
                 * @param x the horizontal pixel index
                 * @param y the vertical pixel index
                 */
                public void castRay(int x, int y) {
                    Ray ray = constructRay(nX, nY, x, y);
                    Color color = rayTracer.traceRay(ray);
                    imageWriter.writePixel(x, y, color);
                }
            }