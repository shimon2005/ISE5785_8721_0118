package renderer;
// import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import primitives.*;

public class BlackBoard {

    @Test
    public void generateJitteredDiskSamplesImage() {
        int imageSize = 500;
        ImageWriter imageWriter = new ImageWriter("jittered_disk_samples", imageSize, imageSize);
        Point center = new Point(0, 0, 0);
        Vector vRight = new Vector(1, 0, 0); // X axis
        Vector vUp = new Vector(0, 1, 0);    // Y axis
        double radius = 1.0;
        int numPoints = 81;

        List<Point> points = BlackBoard.generateJitteredDiskSamples(center, vRight, vUp, radius, numPoints);

        // Draw all points
        for (Point p : points) {
            // Convert 3D point to 2D pixel space [-1,1] → [0,imageSize)
            double x = p.subtract(center).dotProduct(vRight) / radius; // normalized x in [-1,1]
            double y = p.subtract(center).dotProduct(vUp) / radius;    // normalized y in [-1,1]

            int px = (int) ((x + 1) / 2 * imageSize);
            int py = (int) ((1 - (y + 1) / 2) * imageSize); // flip y axis for image coords

            if (px >= 0 && px < imageSize && py >= 0 && py < imageSize) {
                imageWriter.writePixel(px, py, Color.RED);
            }
        }

        // Optionally draw a circle outline
        int centerX = imageSize / 2;
        int centerY = imageSize / 2;
        int radiusPixels = imageSize / 2;
        Color circleColor = new Color(100, 100, 100);

        for (int x = -radiusPixels; x <= radiusPixels; x++) {
            int y = (int) Math.sqrt(radiusPixels * radiusPixels - x * x);
            imageWriter.writePixel(centerX + x, centerY + y, circleColor);
            imageWriter.writePixel(centerX + x, centerY - y, circleColor);
        }

        imageWriter.writeToImage();
    }
}
