package renderer;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Camera implements Cloneable {
    // Camera fields
    private Point camPos;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;

    // View plane fields
    private double viewPlaneHeight = 0.0;
    private double viewPlaneWidth = 0.0;
    private double camToViewPlaneDistance = 0.0;


    private Camera() {}

    public getBuilder() {
        // TODO: Implement the builder pattern for Camera
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

}
