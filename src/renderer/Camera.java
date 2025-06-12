package renderer;

import primitives.Point;
        import primitives.Vector;
        import primitives.Ray;
        import primitives.Util;
        import primitives.Color;
        import scene.Scene;

        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.MissingResourceException;
        import java.util.stream.IntStream;

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


    /** Amount of threads to use for rendering image by the camera */
    private int threadsCount = 0;

    /**
     * Amount of threads to spare for Java VM threads.
     * Spare threads if trying to use all the cores.
     */
    private static final int SPARE_THREADS = 2;

    /**
     * Debug print interval in seconds (for progress percentage).
     * If it is zero - there is no progress output.
     */
    private double printInterval = 0;

    /** Pixel manager for supporting multithreading */
    private PixelManager pixelManager;

    /** Number of rays for Depth of Field (DOF) effect. */
    private int amountOfRays_DOF = 1;

    /** the radius of the aperture for Depth of Field (DOF) effect. */
    private double aperture = 0;

    /** the distance from the camera to the focal plane for Depth of Field (DOF) effect. */
    private double depthOfField = 100;

    /** Amount of rays for Anti-Aliasing (AA) effect. */
    private int amountOfRays_AA = 1;

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
            Vector up =Vector.AXIS_Y;
            this.camera.vTo = target.subtract(this.camera.location).normalize();
            Vector vRight = (this.camera.vTo).crossProduct(up);
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
         * Sets the number of threads to use for rendering.
         * If threads is -2, it uses all available cores minus a few spare threads.
         * If threads is -1, it uses a single thread.
         * @param threads the number of threads to use
         * @return the builder instance
         * @throws IllegalArgumentException if threads is less than -2
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");

            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else {
                camera.threadsCount = threads;
            }
            return this;
        }


        /**
         * Sets the debug print interval for progress output.
         * @param interval the interval in seconds for printing progress
         * @return the builder instance
         * @throws IllegalArgumentException if interval is negative
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0)
                throw new IllegalArgumentException("interval must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the amount of rays for Depth of Field (DOF) effect.
         * @param amountOfRays the number of rays to use for DOF
         * @return the builder instance
         * @throws IllegalArgumentException if amountOfRays is less than or equal to zero
         */
        public Builder setAmountOfRays_DOF(int amountOfRays) {
            if (amountOfRays <= 0) {
                throw new IllegalArgumentException("amountOfRays must be greater than zero");
            }
            this.camera.amountOfRays_DOF = amountOfRays;
            return this;
        }

        /**
         * Sets the aperture radius for Depth of Field (DOF) effect.
         * @param aperture the radius of the aperture
         * @return the builder instance
         * @throws IllegalArgumentException if aperture is less than or equal to zero
         */
        public Builder setAperture(double aperture) {
            if (aperture <= 0) {
                throw new IllegalArgumentException("aperture must be greater than zero");
            }
            this.camera.aperture = aperture;
            return this;
        }

        /**
         * Sets the distance from the camera to the focal plane for Depth of Field (DOF) effect.
         * @param depthOfField the distance to the focal plane
         * @return the builder instance
         * @throws IllegalArgumentException if depthOfField is less than or equal to zero
         */
        public Builder setDepthOfField(double depthOfField) {
            if (depthOfField <= 0) {
                throw new IllegalArgumentException("depthOfField must be greater than zero");
            }
            this.camera.depthOfField = depthOfField;
            return this;
        }

        /**
         * Sets the amount of rays for Anti-Aliasing (AA) effect.
         * @param amountOfRays the number of rays to use for AA
         * @return the builder instance
         * @throws IllegalArgumentException if amountOfRays is less than or equal to zero
         */
        public Builder setAmountOfRays_AA(int amountOfRays) {
            if (amountOfRays <= 0) {
                throw new IllegalArgumentException("amountOfRays must be greater than zero");
            }
            this.camera.amountOfRays_AA = amountOfRays;
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

            if (this.camera.rayTracer == null) {
                setRayTracer(null, RayTracerType.SIMPLE);
            }
            try {
                return (Camera) this.camera.clone();
            } catch (CloneNotSupportedException e) {
               return null; // This should never happen since Camera implements Cloneable
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
        Point pij = calculatePixelCenter(j, i);
        Vector dir = pij.subtract(location).normalize();
        return new Ray(location, dir);
    }

    /**
     * Renders the image by casting rays through each pixel.
     * @return the camera instance
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
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
        Color color;

        boolean useAA = amountOfRays_AA > 1;
        boolean useDOF = amountOfRays_DOF > 1 && aperture != 0;

        if (useAA && useDOF) {
            color = AAandDOFCombinedColor(x, y);

        } else if (useAA) {
            List<Ray> rays = constructAARays(x, y);
            color = averageRays(rays);
        } else if (useDOF) {
            List<Ray> rays = constructDOFRays(x, y);
            color = averageRays(rays);
        } else {
            Ray ray = constructRay(nX, nY, x, y);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(x, y, color);
        pixelManager.pixelDone();
    }

    private Color averageRays(List<Ray> rays) {
        if (rays.isEmpty()) {
            return Color.BLACK;
        }
        Color total = Color.BLACK;
        for (Ray ray : rays) {
            total = total.add(rayTracer.traceRay(ray));
        }
        return total.reduce(rays.size());
    }


    /**
     * Renders the image using the specified rendering method.
     * @return the camera instance
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }

    /**
     * Renders the image using multiple threads.
     * The rendering is done in parallel for each pixel.
     * @return the camera instance
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }


    /**
     * Renders the image using multiple threads with a pixel manager.
     * Each thread processes pixels in parallel, improving performance.
     * @return the camera instance
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        for (int i = 0; i < threadsCount; ++i) {
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        }

        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }


    private ArrayList<Ray> constructDOFRays(int j, int i) {
        return constructDOFRaysWithDirection(j, i, null);
    }


    private ArrayList<Ray> constructDOFRaysWithDirection(int j, int i, Vector direction) {
        ArrayList<Ray> DOFRays = new ArrayList<>();

        if (direction == null) {
            Point pij = calculatePixelCenter(j, i);
            direction = pij.subtract(location).normalize();
        }

        Point focalPoint = location.add(direction.scale(depthOfField));

        List<Point> aperturePoints = BlackBoard.generateJitteredDiskSamples(
                location, vRight, vUp, aperture, amountOfRays_DOF);

        for (Point aperturePoint : aperturePoints) {
            Vector dir = focalPoint.subtract(aperturePoint).normalize();
            DOFRays.add(new Ray(aperturePoint, dir));
        }

        return DOFRays;
    }


    private ArrayList<Ray> constructAARays(int j, int i) {
        ArrayList<Ray> AARays = new ArrayList<>();
        ArrayList<Vector> AAVectors = getAAVectors(j, i);

        for (Vector AAVector : AAVectors) {
            AARays.add(new Ray(location, AAVector));
        }

        return AARays;
    }


    private ArrayList<Vector> getAAVectors(int j, int i) {
        ArrayList<Vector> AAVectors = new ArrayList<>();

        Point pij = calculatePixelCenter(j, i);

        double pixelRadius = (viewPlaneWidth / nX) / 2.0;

        List<Point> pixelPoints = BlackBoard.generateJitteredDiskSamples(
                pij, vRight, vUp, pixelRadius, amountOfRays_AA);

        for (Point pixelPoint : pixelPoints) {
            Vector dir = pixelPoint.subtract(location).normalize();
            AAVectors.add(dir);
        }

        return AAVectors;
    }


    private Point calculatePixelCenter(int j, int i) {
        Point pc = location.add(vTo.scale(vpDistance));
        double rX = viewPlaneWidth / nX;
        double rY = viewPlaneHeight / nY;
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        Point pij = pc;
        if (!Util.isZero(xJ)) pij = pij.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pij = pij.add(vUp.scale(yI));

        return pij;
    }

    private Color AAandDOFCombinedColor(int x, int y) {
        List<Vector> AAVectors = getAAVectors(x, y);
        Color totalColor = Color.BLACK;

        for (Vector AAVector : AAVectors) {
            ArrayList<Ray> DOFrays = constructDOFRaysWithDirection(x, y, AAVector);

            totalColor = totalColor.add(averageRays(DOFrays));
        }

        return totalColor.reduce(AAVectors.size());
    }



}

