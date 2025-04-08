package renderer;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import primitives.Util;

import java.util.MissingResourceException;

public class Camera implements Cloneable {
    // Camera fields
    private Point location;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;

    // View plane fields
    private double viewPlaneHeight = 0.0;
    private double viewPlaneWidth = 0.0;
    private double vpDistance = 0.0;

    public static class Builder {
        final Camera camera  = new Camera();

        public Builder setLocation(Point point) {
            this.camera.location = point;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            this.camera.vTo = vTo.normalize();
            this.camera.vUp = vUp.normalize();
            return this;
        }

        public Builder setDirection(Point target, Vector vUp) {
            if (target == this.camera.location) {
                throw new IllegalArgumentException("the target point cannot be the camera position");
            }
            this.camera.vTo = target.subtract(this.camera.vUp).normalize();
            this.camera.vUp = (this.camera.vRight).crossProduct(this.camera.vTo).normalize();
            return this;
        }

        public Builder setDirection(Point target) {
            if (target == this.camera.location) {
                throw new IllegalArgumentException("the target point cannot be the camera position");
            }
            this.camera.vUp = new Vector(0,1,0);
            this.camera.vTo = target.subtract(this.camera.vUp).normalize();
            Vector vRight = (this.camera.vTo).crossProduct(this.camera.vUp).normalize();
            this.camera.vUp = (this.camera.vRight).crossProduct(this.camera.vTo).normalize();
            return this;
        }

        public Builder setVpSize (double height, double width) {
            if (height <= 0 || width <= 0) {
                throw new IllegalArgumentException("height and width must be greater than zero");
            }
            this.camera.viewPlaneHeight = height;
            this.camera.viewPlaneWidth = width;
        }

        public Builder setVpDistance (double vpDistance) {
            if (vpDistance <= 0.0) {
                throw new IllegalArgumentException("vpDistance must be greater than zero");
            }
            this.camera.vpDistance = vpDistance;
            return this;
        }

        public Camera build() {
            final String camFieldMissingMsg = "The camera field is missing";
            if (this.camera.location == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "Camera location ");
            }
            if (this.camera.vTo == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "Vector vTo");
            }
            if (this.camera.vUp == null) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "Vector vUp");
            }
            if (this.camera.viewPlaneHeight == 0.0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "View plane Height");
            }
            if (this.camera.viewPlaneWidth == 0.0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "View plane Width");
            }
            if (this.camera.vpDistance == 0.0) {
                throw new MissingResourceException(camFieldMissingMsg, "Camera" , "view plane to camera distance");
            }

            // Validate orthogonality of vTo and vUp
            if (!Util.isZero(camera.vTo.dotProduct(camera.vUp))) {
                throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");
            }

            // Calculate vRight
            this.camera.vRight = (this.camera.vTo).crossProduct(this.camera.vUp).normalize();
            try {
                return (Camera) this.camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning is not supported for Camera", e);
            }
        }

    }



    private Camera() {}

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

}
