package renderer;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import primitives.Util;
import primitives.Color;
import scene.Scene;
import renderer.BlackBoard.BoardShape;

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
    // Many of the fields (not all of them) are initialized to invalid values,
    // so we can throw an exception if the user doesn't set them himself with valid values
    // when using features that those fields are relevant for.
    // That will help to prevent a situation where the user forgets to set a field that is necessary
    // for the image he wishes to render.
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

    /** The shape of the board for jittered sampling (SQUARE or CIRCLE). */
    private BoardShape boardShape = BoardShape.SQUARE;

    /** Field that detects whether to use Depth of Field (DOF) effect or not. */
    private boolean useDOF = false;

    /** Number of rays for Depth of Field (DOF) effect. */
    private int amountOfRays_DOF = 0;

    /** the radius of the aperture for Depth of Field (DOF) effect. */
    private double apertureRadius = 0;

    /** the distance from the camera to the focal plane for Depth of Field (DOF) effect. */
    private double depthOfField = 0;

    /** Field that detects whether to use Anti-Aliasing (AA) effect or not. */
    private boolean useAA = false;

    /** Number of rays for Anti-Aliasing (AA) effect. */
    private int amountOfRays_AA = 0;

    /** Field that detects whether to use adaptive super sampling for Depth of Field (DOF) effect or not. */
    private boolean useAdaptiveSuperSamplingForDOF = false;

    /** The number of samples per area for Depth of Field (DOF) effect with adaptive super sampling. */
    private int numOfSubAreaSamplesAdaptiveDOF = 0;

    /** Maximum number of samples for adaptive super sampling for Depth of Field (DOF) effect. */
    private int maxSamplesAdaptiveDOF = 0;

    /** Threshold for color difference for adaptive super sampling for Depth of Field (DOF) effect. */
    private double colorThresholdAdaptiveDOF = -1;

    /** Field that detects whether to use adaptive super sampling for Anti-Aliasing (AA) effect or not. */
    private boolean useAdaptiveSuperSamplingForAA = false;

    /** The number of samples per area for Anti-Aliasing (AA) effect with adaptive super sampling. */
    private int numOfSubAreaSamplesAdaptiveAA = 0;

    /** Maximum number of samples for adaptive super sampling for Anti-Aliasing (AA) effect. */
    private int maxSamplesAdaptiveAA = 0;

    /** Threshold for color difference for adaptive super sampling for Anti-Aliasing (AA) effect. */
    private double colorThresholdAdaptiveAA = -1;





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
        public Builder setDirection(Point target) {
            if (this.camera.location == null) {
                throw new IllegalStateException("Camera location must be set before setting direction with target point.");
            }
            if (target.equals(this.camera.location)) {
                throw new IllegalArgumentException("the target point cannot be the camera position");
            }
            Vector up = Vector.AXIS_Y;
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
         * Sets the shape of the sample area for jittered sampling.
         * @param boardShape the shape of the sample area (SQUARE or CIRCLE)
         * @return the builder instance
         */
        public Builder setBoardShape(BoardShape boardShape) {
            if (boardShape == null) {
                throw new IllegalArgumentException("sampleShape cannot be null");
            }
            this.camera.boardShape = boardShape;
            return this;
        }

        /**
         * Sets whether to use Depth of Field (DOF) effect or not.
         * @param useDOF true to enable DOF, false to disable
         * @return the builder instance
         */
        public Builder setUseDOF(boolean useDOF) {
            this.camera.useDOF = useDOF;
            return this;
        }

        /**
         * Sets the number of rays for Depth of Field (DOF) effect.
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
         * @param apertureRadius the radius of the aperture
         * @return the builder instance
         * @throws IllegalArgumentException if aperture is less than or equal to zero
         */
        public Builder setApertureRadius(double apertureRadius) {
            if (apertureRadius <= 0) {
                throw new IllegalArgumentException("aperture must be greater than zero");
            }
            this.camera.apertureRadius = apertureRadius;
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
         * Sets whether to use Anti-Aliasing (AA) effect or not.
         * @param useAA true to enable AA, false to disable
         * @return the builder instance
         */
        public Builder setUseAA(boolean useAA) {
            this.camera.useAA = useAA;
            return this;
        }

        /**
         * Sets the number of rays for Anti-Aliasing (AA) effect.
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
         * Sets whether to use adaptive super sampling for Depth of Field (DOF) effect or not.
         * @param useAdaptiveSuperSamplingForDOF true to enable adaptive super sampling, false to disable
         * @return the builder instance
         */
        public Builder setUseAdaptiveSuperSamplingForDOF(boolean useAdaptiveSuperSamplingForDOF) {
            this.camera.useAdaptiveSuperSamplingForDOF = useAdaptiveSuperSamplingForDOF;
            return this;
        }


        /**
         * Sets the number of samples per area for Depth of Field (DOF) effect with adaptive super sampling.
         * @param numOfSubAreaSamplesAdaptiveDOF the number of samples per area
         * @return the builder instance
         * @throws IllegalArgumentException if numOfAreaSamples is less than or equal to zero
         */
        public Builder setNumOfSubAreaSamplesAdaptiveDOF(int numOfSubAreaSamplesAdaptiveDOF) {
            if (numOfSubAreaSamplesAdaptiveDOF <= 0) {
                throw new IllegalArgumentException("numOfSubAreaSamplesAdaptiveDOF must be greater than zero");
            }
            this.camera.numOfSubAreaSamplesAdaptiveDOF = numOfSubAreaSamplesAdaptiveDOF;
            return this;
        }


        /**
         * Sets the maximum number of samples for adaptive super sampling for Depth of Field (DOF) effect.
         * @param maxSamplesAdaptiveDOF the maximum number of samples
         * @return the builder instance
         * @throws IllegalArgumentException if maxSamples is less than or equal to zero
         */
        public Builder setMaxSamplesAdaptiveDOF(int maxSamplesAdaptiveDOF) {
            if (maxSamplesAdaptiveDOF <= 0) {
                throw new IllegalArgumentException("maxSamplesAdaptiveDOF must be greater than zero");
            }
            this.camera.maxSamplesAdaptiveDOF = maxSamplesAdaptiveDOF;
            return this;
        }


        /**
         * Sets the color threshold for adaptive super sampling for Depth of Field (DOF) effect.
         * @param colorThresholdAdaptiveDOF the color difference threshold
         * @return the builder instance
         * @throws IllegalArgumentException if colorThreshold is less than or equal to zero
         */
        public Builder setColorThresholdAdaptiveDOF(double colorThresholdAdaptiveDOF) {
            if (colorThresholdAdaptiveDOF < 0) {
                throw new IllegalArgumentException("colorThreshold must be greater than or equal to zero");
            }
            this.camera.colorThresholdAdaptiveDOF = colorThresholdAdaptiveDOF;
            return this;
        }


        /**
         * Sets a field that detects whether to use adaptive super sampling for Anti-Aliasing (AA) effect or not.
         * @param useAdaptiveSuperSamplingForAA true to enable adaptive super sampling, false to disable
         * @return the builder instance
         */
        public Builder setUseAdaptiveSuperSamplingForAA(boolean useAdaptiveSuperSamplingForAA) {
            this.camera.useAdaptiveSuperSamplingForAA = useAdaptiveSuperSamplingForAA;
            return this;
        }


        /**
         * Sets the number of samples per area for Anti-Aliasing (AA) effect with adaptive super sampling.
         * @param numOfSubAreaSamplesAdaptiveAA the number of samples per area
         * @return the builder instance
         * @throws IllegalArgumentException if numOfAreaSamples is less than or equal to zero
         */
        public Builder setNumOfSubAreaSamplesAdaptiveAA(int numOfSubAreaSamplesAdaptiveAA) {
            this.camera.numOfSubAreaSamplesAdaptiveAA = numOfSubAreaSamplesAdaptiveAA;
            return this;
        }

        /**
         * Sets the maximum number of samples for Anti-Aliasing (AA) effect with adaptive super sampling.
         * @param maxSamplesAdaptiveAA the maximum number of samples
         * @return the builder instance
         * @throws IllegalArgumentException if maxSamples is less than or equal to zero
         */
        public Builder setMaxSamplesAdaptiveAA(int maxSamplesAdaptiveAA) {
            this.camera.maxSamplesAdaptiveAA = maxSamplesAdaptiveAA;
            return this;
        }


        /**
         * Sets the color threshold for Anti-Aliasing (AA) effect for adaptive super sampling.
         * @param colorThresholdAdaptiveAA the color difference threshold
         * @return the builder instance
         * @throws IllegalArgumentException if colorThreshold is less than or equal to zero
         */
        public Builder setColorThresholdAdaptiveAA(double colorThresholdAdaptiveAA) {
            if (colorThresholdAdaptiveAA < 0) {
                throw new IllegalArgumentException("colorThreshold must be greater than zero");
            }
            this.camera.colorThresholdAdaptiveAA = colorThresholdAdaptiveAA;
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

            // Validation checks for the camera fields
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

            if (!this.camera.useAA && this.camera.amountOfRays_AA != 0) {
                throw new IllegalStateException("You have specified AA parameters " +
                        "even though useAA is false. Set useAA to true to enable AA.");
            }

            if (this.camera.useAA && !this.camera.useAdaptiveSuperSamplingForAA) {
                // those are cheks regarding amountOfRays_AA, so we only perform them when useAA is true,
                // and also useAdaptiveSuperSamplingForAA is false,
                // because amountOfRays_AA is irrelevant when using adaptive super sampling for aa
                if (this.camera.amountOfRays_AA <= 0) {
                    throw new IllegalArgumentException("amountOfRays_AA must be greater than zero when using AA");
                }

                int rootAmountOfRays_AA = (int) Math.sqrt(this.camera.amountOfRays_AA);
                if (rootAmountOfRays_AA * rootAmountOfRays_AA != this.camera.amountOfRays_AA) {
                    throw new IllegalArgumentException("amountOfRays_AA must be a perfect square (e.g. 16, 25, 36)");
                }
            }

            if (!this.camera.useDOF && (this.camera.amountOfRays_DOF != 0 || this.camera.apertureRadius != 0 ||
                    this.camera.depthOfField != 0)) {
                throw new IllegalStateException("You have specified DOF parameters " +
                        "even though useDOF is false. Set useDOF to true to enable DOF.");
            }

            if (this.camera.useDOF) {
                if (this.camera.apertureRadius <= 0) {
                    throw new IllegalArgumentException("apertureRadius must be greater than zero when using DOF");
                }

                if (this.camera.depthOfField <= 0) {
                    throw new IllegalArgumentException("depthOfField must be greater than zero when using DOF");
                }
            }

            if (this.camera.useDOF && !this.camera.useAdaptiveSuperSamplingForDOF) {
                // those are checks regarding amountOfRays_DOF, so we only perform them when useDOF is true,
                // and also useAdaptiveSuperSamplingForDOF is false,
                // because amountOfRays_DOF is irrelevant when using adaptive super sampling for DOF
                if (this.camera.amountOfRays_DOF <= 0) {
                    throw new IllegalArgumentException("amountOfRays_DOF must be greater than zero when using DOF");
                }

                int rootAmountOfRays_DOF = (int) Math.sqrt(this.camera.amountOfRays_DOF);
                if (rootAmountOfRays_DOF * rootAmountOfRays_DOF != this.camera.amountOfRays_DOF) {
                    throw new IllegalArgumentException("amountOfRays_DOF must be a perfect square (e.g. 16, 25, 36)");
                }
            }

            if (!this.camera.useAdaptiveSuperSamplingForAA && (this.camera.numOfSubAreaSamplesAdaptiveAA != 0 ||
                    this.camera.maxSamplesAdaptiveAA != 0 || this.camera.colorThresholdAdaptiveAA != -1)) {
                throw new IllegalStateException("You have specified adaptive super sampling parameters " +
                        "even though useAdaptiveSuperSamplingForAA is false. Set useAdaptiveSuperSamplingForAA to true to enable adaptive super sampling.");
            }

            if (this.camera.useAdaptiveSuperSamplingForDOF)
            {
                if (!this.camera.useDOF) {
                    throw new IllegalStateException("UseAdaptiveSuperSamplingForDOF is set to true " +
                            "even though useDOF is set false. Set useDOF to true if you wish to use adaptive super sampling for DOF.");
                }
                if (this.camera.numOfSubAreaSamplesAdaptiveDOF <= 0) {
                    throw new IllegalArgumentException("numOfSubAreaSamplesAdaptiveDOF must be greater than zero when using adaptive super sampling for DOF");
                }
                if (this.camera.maxSamplesAdaptiveDOF <= 0) {
                    throw new IllegalArgumentException("maxSamplesAdaptiveDOF must be greater than zero when using adaptive super sampling for DOF");
                }
                if (this.camera.colorThresholdAdaptiveDOF < 0) {
                    throw new IllegalArgumentException("colorThresholdAdaptiveDOF must be greater than zero when using adaptive super sampling for DOF");
                }

                int rootNumOfSubAreaSamplesAdaptiveDOF = (int) Math.sqrt(this.camera.numOfSubAreaSamplesAdaptiveDOF);
                if (rootNumOfSubAreaSamplesAdaptiveDOF * rootNumOfSubAreaSamplesAdaptiveDOF != this.camera.numOfSubAreaSamplesAdaptiveDOF) {
                    throw new IllegalArgumentException("numOfSubAreaSamplesAdaptiveDOF must be a perfect square (e.g. 81, 100, 121)");
                }

                if (this.camera.maxSamplesAdaptiveDOF < this.camera.numOfSubAreaSamplesAdaptiveDOF) {
                    throw new IllegalArgumentException(
                            "maxSamplesAdaptiveDOF must be greater than or equal to numOfSubAreaSamplesAdaptiveDOF," +
                                    " which is " + this.camera.numOfSubAreaSamplesAdaptiveDOF);
                }

                int ratio = this.camera.maxSamplesAdaptiveDOF / this.camera.numOfSubAreaSamplesAdaptiveDOF;

                // Only if all 3 of the following conditions are met, then maxSamplesAdaptiveDOF is
                // numOfSubAreaSamplesAdaptiveDOF multiplied by a power of 4.
                // If even 1 of these conditions is not met, we throw an IllegalArgumentException.
                if(
                    // Condition 1:
                    // Check that maxSamplesAdaptiveDOF is divisible by numOfSubAreaSamplesAdaptiveDOF without remainder.
                    // If not divisible, ratio is not an integer and the requirement fails.
                    (this.camera.maxSamplesAdaptiveDOF % this.camera.numOfSubAreaSamplesAdaptiveDOF != 0 ||

                    // Condition 2:
                    // Check if ratio is a power of 2.
                    // Using bitwise trick: For any power of 2, (n & (n - 1)) == 0.
                    // If this is not true, ratio is not a power of 2, and therefore not a power of 4.
                    (ratio & (ratio - 1)) != 0 ||

                    // Condition 3:
                    // Check if ratio modulo 3 equals 1.
                    // This ensures ratio is a power of 4, since all powers of 4 modulo 3 equal 1.
                    // If not equal to 1, ratio is not a power of 4.
                    ratio % 3 != 1)
                ) {
                    throw new IllegalArgumentException(
                            "maxSamplesAdaptiveDOF must be numOfSubAreaSamplesAdaptiveDOF multiplied by a power of 4");
                }

                if (!this.camera.useAdaptiveSuperSamplingForDOF && (this.camera.numOfSubAreaSamplesAdaptiveDOF != 0 ||
                        this.camera.maxSamplesAdaptiveDOF != 0 || this.camera.colorThresholdAdaptiveDOF != -1)) {
                    throw new IllegalStateException("You have specified adaptive super sampling parameters " +
                            "even though useAdaptiveSuperSamplingForDOF is false. Set useAdaptiveSuperSamplingForDOF to true to enable adaptive super sampling.");
                }
            }


            if (this.camera.useAdaptiveSuperSamplingForAA) {

                if(!this.camera.useAA) {
                    throw new IllegalStateException("UseAdaptiveSuperSamplingForAA is set to true " +
                            "even though useAA is set false. Set useAA to true if you wish to use adaptive super sampling for AA.");
                }
                if (this.camera.numOfSubAreaSamplesAdaptiveAA <= 0) {
                    throw new IllegalArgumentException("numOfSubAreaSamplesAdaptiveAA must be greater than zero when using adaptive super sampling for AA");
                }
                if (this.camera.maxSamplesAdaptiveAA <= 0) {
                    throw new IllegalArgumentException("maxSamplesAdaptiveAA must be greater than zero when using adaptive super sampling for AA");
                }
                if (this.camera.colorThresholdAdaptiveAA < 0) {
                    throw new IllegalArgumentException("colorThresholdAdaptiveAA must be greater than zero when using adaptive super sampling for AA");
                }

                int rootNumOfSubAreaSamplesAdaptiveAA = (int) Math.sqrt(this.camera.numOfSubAreaSamplesAdaptiveAA);
                if (rootNumOfSubAreaSamplesAdaptiveAA * rootNumOfSubAreaSamplesAdaptiveAA != this.camera.numOfSubAreaSamplesAdaptiveAA) {
                    throw new IllegalArgumentException("numOfSubAreaSamplesAdaptiveAA must be a perfect square (e.g. 81, 100, 121)");
                }

                if (this.camera.maxSamplesAdaptiveAA < this.camera.numOfSubAreaSamplesAdaptiveAA) {
                    throw new IllegalArgumentException(
                            "maxSamplesAdaptiveAA must be greater than or equal to numOfSubAreaSamplesAdaptiveAA," +
                                    " which is " + this.camera.numOfSubAreaSamplesAdaptiveAA);
                }

                int ratio = this.camera.maxSamplesAdaptiveAA / this.camera.numOfSubAreaSamplesAdaptiveAA;

                // Only if all 3 of the following conditions are met, then maxSamplesAdaptiveAA is
                // numOfSubAreaSamplesAdaptiveAA multiplied by a power of 4.
                // If even 1 of these conditions is not met, we throw an IllegalArgumentException.
                if(
                    // Condition 1:
                    // Check that maxSamplesAdaptiveAA is divisible by numOfSubAreaSamplesAdaptiveAA without remainder.
                    // If not divisible, ratio is not an integer and the requirement fails.
                    (this.camera.maxSamplesAdaptiveAA % this.camera.numOfSubAreaSamplesAdaptiveAA != 0 ||

                    // Condition 2:
                    // Check if ratio is a power of 2.
                    // Using bitwise trick: For any power of 2, (n & (n - 1)) == 0.
                    // If this is not true, ratio is not a power of 2.
                    (ratio & (ratio - 1)) != 0 ||

                    // Condition 3:
                    // Check if ratio modulo 3 equals 1.
                    // This ensures ratio is a power of 4, since all powers of 4 modulo 3 equal 1.
                    // If not equal to 1, ratio is not a power of 4.
                    ratio % 3 != 1)
                ) {
                    throw new IllegalArgumentException(
                            "maxSamplesAdaptiveAA must be numOfSubAreaSamplesAdaptiveAA multiplied by a power of 4");
                }
            }
            // End of validation checks for the camera fields


            // Calculate the right vector (vRight) as the cross product of vTo and vUp
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
     * @param j  pixel column (0..nX-1)
     * @param i  pixel row    (0..nY-1)
     * @return the ray through the center of that pixel
     */
    public Ray constructRay(int j, int i) {
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
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     */
    public void castRay(int j, int i) {
        Color color;

        if (useAA) {
            // this method also handles dof (if we used both aa and dof)
            color = aaColor(j, i);

        } else if (useDOF) {
            // if aa is used, the previous case already handled dof,
            // and we don't need to handle with it here.
            // that's why it is else-if and not just if.
            color = dofColorFromPixelIndices(j, i);

        } else {
            // handles the simple case where aa and dof are both not used
            Ray ray = constructRay(j, i);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }


    /**
     * Calculate the average color from the traces of the rays in the given list.
     *
     * @param rays the list of rays to trace
     * @return the average color of the rays
     */
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



    /**
     * Constructs Depth of Field (DOF) rays in a specific direction.
     * This method is used when Depth of Field (DOF) effect is enabled.
     *
     * @param direction the direction vector for the DOF rays
     * @return a list of DOF rays
     */
    private ArrayList<Ray> constructDOFRays(Vector direction) {
        ArrayList<Ray> DOFRays = new ArrayList<>();

        Point focalPoint = location.add(direction.scale(depthOfField));

        List<Point> aperturePoints = BlackBoard.generateJitteredSamples(
                location, vRight, vUp, apertureRadius, amountOfRays_DOF, boardShape);

        for (Point aperturePoint : aperturePoints) {
            Vector dir = focalPoint.subtract(aperturePoint).normalize();
            DOFRays.add(new Ray(aperturePoint, dir));
        }

        return DOFRays;
    }


    /**
     * Constructs Anti-Aliasing (AA) rays for a specific pixel.
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return a list of AA rays
     */
    private ArrayList<Ray> constructAARays(int j, int i) {
        ArrayList<Ray> AARays = new ArrayList<>();
        ArrayList<Vector> AAVectors = getAAVectors(j, i);

        for (Vector AAVector : AAVectors) {
            AARays.add(new Ray(location, AAVector));
        }

        return AARays;
    }


    /**
     * Generates Anti-Aliasing (AA) vectors for a specific pixel.
     * These vectors are used to create rays for anti-aliasing effects.
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return a list of AA vectors
     */
    private ArrayList<Vector> getAAVectors(int j, int i) {
        ArrayList<Vector> AAVectors = new ArrayList<>();

        Point pij = calculatePixelCenter(j, i);

        double pixelRadius = (viewPlaneWidth / nX) / 2.0;

        List<Point> pixelPoints = BlackBoard.generateJitteredSamples(
                pij, vRight, vUp, pixelRadius, amountOfRays_AA, boardShape);

        for (Point pixelPoint : pixelPoints) {
            Vector dir = pixelPoint.subtract(location).normalize();
            AAVectors.add(dir);
        }

        return AAVectors;
    }


    /**
     * Calculates the center point of a pixel in the view plane.
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return the center point of the pixel
     */
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



    /**
     * Calculate the averaged color for the sampled area using Depth of Field (DOF) effect.
     * Based on pixel indices (j, i).
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return the averaged color for the sampled area using Depth of Field (DOF) effect.
     */
    private Color dofColorFromPixelIndices(int j, int i) {

        Point pixelCenter = calculatePixelCenter(j, i);
        Vector direction = pixelCenter.subtract(location).normalize();
        return dofColorFromDirection(direction);
    }


    /**
     * Calculate the averaged color for the sampled area using adaptive super sampling for Depth of Field (DOF).
     * Based on a given direction vector.
     *
     * @param direction the direction vector for the DOF rays
     * @return the averaged color for the sampled area using adaptive super sampling for Depth of Field (DOF).
     */
    private Color dofColorFromDirection(Vector direction) {

        Color color;

        if (useAdaptiveSuperSamplingForDOF) {
            // The focal point is where all rays should converge
            Point focalPoint = location.add(direction.scale(depthOfField));

            // Start recursion on the aperture, not the pixel
            color = adaptiveDofColorForSubArea(location, apertureRadius, focalPoint, 0);
        } else {
            color = nonAdaptiveDofColor(direction);
        }

        return color;
    }


    /**
     * Calculate the averaged color for the sampled area using non-adaptive super sampling for Depth of Field (DOF).
     * Based on a given direction vector.
     *
     * @param direction the direction vector for the DOF rays
     * @return the averaged color for the sampled area using non-adaptive super sampling for Depth of Field (DOF).
     */
    private Color nonAdaptiveDofColor(Vector direction) {
        ArrayList<Ray> DOFrays = constructDOFRays(direction);
        return averageRays(DOFrays);
    }


    /**
     * Helper method for adaptive sampling on arbitrary point and radius.
     * This method samples the pixel area adaptively based on color variance.
     *
     * @param center center point of the sub-area
     * @param radius if sampling area (boardShape) is circular, this is the radius of that circle, if square, this is half the side length
     * @param focalPoint the focal point for DOF
     * @param depth current recursion depth
     * @return averaged color for the sampled area
     */
    private Color adaptiveDofColorForSubArea(Point center, double radius, Point focalPoint, int depth) {
        List<Point> apertureSamples = BlackBoard.generateJitteredSamples(
                center, vRight, vUp, radius, numOfSubAreaSamplesAdaptiveDOF, boardShape);

        List<Color> sampleColors = new ArrayList<>();

        for (Point apertureSample : apertureSamples) {
            Vector dir = focalPoint.subtract(apertureSample).normalize();
            Ray ray = new Ray(apertureSample, dir);
            Color color = rayTracer.traceRay(ray);
            sampleColors.add(color);
        }

        // If the number of samples exceeds the maximum allowed or colors are similar, return the average color.

        // Math.pow(4, depth) represents the number of subareas the area is divided into at the current recursion depth.
        // Multiplying it by numOfSubAreaSamplesAdaptiveDOF gives the total number of rays (samples) traced at this depth.

        // for the maximum check we could use == because the number of samples is always numOfSubAreaSamplesAdaptiveDOF * k^4,
        // but we use >= just to be safe
        if (numOfSubAreaSamplesAdaptiveDOF * Math.pow(4, depth) >= maxSamplesAdaptiveDOF || colorsAreSimilar(sampleColors, colorThresholdAdaptiveDOF)) {
            return averageColors(sampleColors);
        }

        double halfRadius = radius / 2.0;
        Color totalColor = Color.BLACK;
        double[] offsetsX = {-halfRadius, halfRadius};
        double[] offsetsY = {-halfRadius, halfRadius};

        for (double offsetX : offsetsX) {
            for (double offsetY : offsetsY) {
                Point subCenter = center.add(vRight.scale(offsetX)).add(vUp.scale(offsetY));
                totalColor = totalColor.add(adaptiveDofColorForSubArea(subCenter, halfRadius, focalPoint, depth + 1));
            }
        }

        // We reduce by 4 to get the average, because total color is the sum of the colors of 4 sub-areas.
        return totalColor.reduce(4);
    }



    /**
     * Adaptive super sampling for Anti-Aliasing (AA) effect.
     * This method samples the pixel area adaptively based on color variance.
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return the averaged color for the sampled area
     */
    private Color aaColor(int j, int i) {
        Color color;

        if (useAdaptiveSuperSamplingForAA) {
            Point pc = calculatePixelCenter(j, i);
            double pixelRadius = (viewPlaneWidth / nX) / 2.0;
            color = adaptiveAaColorForSubArea(pc, pixelRadius, 0);

        } else {
            color = nonAdaptiveAaColor(j, i);
        }

        return color;
    }


    /**
     * Non-adaptive super sampling for Anti-Aliasing (AA) effect.
     * This method samples the pixel area uniformly.
     *
     * @param j the horizontal pixel index
     * @param i the vertical pixel index
     * @return the averaged color for the sampled area
     */
    private Color nonAdaptiveAaColor (int j, int i) {

        Color color;

        if (useDOF) {
            Color totalColor = Color.BLACK;
            List<Vector> AAVectors = getAAVectors(j, i);

            for (Vector AAVector : AAVectors) {
                Color dofColorForAaVector = dofColorFromDirection(AAVector);
                totalColor = totalColor.add(dofColorForAaVector);
            }
            color = totalColor.reduce(AAVectors.size());

        } else {
            List<Ray> rays = constructAARays(j, i);
            color = averageRays(rays);
        }

        return color;
    }


    /**
     * Helper method for adaptive sampling on arbitrary point and radius.
     *
     * @param center center point of the sub-pixel area
     * @param radius if sampling area (boardShape) is circular, this is the radius of that circle, if square, this is half the side length
     * @param depth  current recursion depth
     * @return averaged color for the sampled area
     */
    private Color adaptiveAaColorForSubArea(Point center, double radius, int depth) {
        List<Point> samplePoints = BlackBoard.generateJitteredSamples(
                center, vRight, vUp, radius, numOfSubAreaSamplesAdaptiveAA, boardShape);

        List<Color> sampleColors = new ArrayList<>();

        for (Point samplePoint : samplePoints) {
            Vector dir = samplePoint.subtract(location).normalize();
            Ray ray = new Ray(location, dir);
            Color sampleColor;

            if (useDOF) {
                sampleColor = dofColorFromDirection(dir);
            }
            else {
                sampleColor = rayTracer.traceRay(ray);
            }

            sampleColors.add(sampleColor);
        }

        // If the number of samples exceeds the maximum allowed or colors are similar, return the average color.

        // Math.pow(4, depth) represents the number of subareas the pixel area is divided into at the current recursion depth.
        // Multiplying it by numOfSubAreaSamplesAdaptiveAA gives the total number of rays (samples) traced at this depth.

        // for the maximum check we could use == because the number of samples is always numOfSubAreaSamplesAdaptiveAA * k^4,
        // but we use >= just to be safe
        if (numOfSubAreaSamplesAdaptiveAA * Math.pow(4, depth) >= maxSamplesAdaptiveAA || colorsAreSimilar(sampleColors, colorThresholdAdaptiveAA)) {
            return averageColors(sampleColors);
        }

        double halfRadius = radius / 2.0;
        Color totalColor = Color.BLACK;
        double[] offsetsX = {-halfRadius, halfRadius};
        double[] offsetsY = {-halfRadius, halfRadius};

        for (double offsetX : offsetsX) {
            for (double offsetY : offsetsY) {
                Point subCenter = center.add(vRight.scale(offsetX)).add(vUp.scale(offsetY));
                totalColor = totalColor.add(adaptiveAaColorForSubArea(subCenter, halfRadius, depth + 1));
            }
        }

        // We reduce by 4 to get the average, because total color is the sum of the colors of 4 sub-areas.
        return totalColor.reduce(4);
    }


    /**
     * Checks if the colors in the list are similar based on a color threshold.
     *
     * @param colors          list of colors to check
     * @param colorThreshold  the maximum allowed distance between colors to consider them similar
     * @return true if all colors are similar, false otherwise
     */
    private boolean colorsAreSimilar(List<Color> colors, double colorThreshold) {
        for (int i = 0; i < colors.size(); i++) {
            for (int j = i + 1; j < colors.size(); j++) {
                if (colors.get(i).distance(colors.get(j)) > colorThreshold) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Averages a list of colors.
     *
     * @param colors list of colors
     * @return the average color
     */
    private Color averageColors(List<Color> colors) {
        Color total = Color.BLACK;
        for (Color c : colors) {
            total = total.add(c);
        }
        return total.reduce(colors.size());
    }


}

