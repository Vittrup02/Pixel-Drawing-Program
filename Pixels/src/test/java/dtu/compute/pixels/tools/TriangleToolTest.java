package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.TriangleTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;
import java.io.IOException;

public class TriangleToolTest {

    @Test
    @DisplayName("can draw a triangle on the image")
    void canDrawTriangleOnImage() throws IOException {
        Image img = new Image(new Rect(20, 20));

        // Set up the controller with the TriangleTool
        Controller ctrl = new Controller().setTool(new TriangleTool()).setImage(img);

        // Set the active color and draw a triangle
        ctrl.setColor(Color.RED); // Set the active color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to draw the triangle

        // Draw the triangle
        ctrl.press(new Point(2, 2)); // First point
        ctrl.update(new Point(10, 2)); // Temporary update to set the second point
        ctrl.press(new Point(10, 2)); // Second point
        ctrl.update(new Point(6, 10)); // Temporary update to show the final triangle shape
        ctrl.press(new Point(6, 10)); // Third point to complete the triangle

        // Ensure that the triangle is drawn on the image
        TestUtils.goldenTest("triangleTool", img);
    }
}

