package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.SquareTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;
import java.io.IOException;

public class SquareToolTest {

    @Test
    @DisplayName("can draw a square on the image")
    void canDrawSquareOnImage() throws IOException {
        // Create a 10x10 image
        Image img = new Image(new Rect(10, 10));

        // Set up the controller with the SquareTool
        Controller ctrl = new Controller().setTool(new SquareTool()).setImage(img);

        // Set the active color and draw a square
        ctrl.setColor(Color.RED); // Set the active color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to draw the square
        ctrl.press(new Point(2, 2)); // Press the mouse button to start drawing (starting point)
        ctrl.update(new Point(7, 7)); // Update to draw the square outline (ending point)
        ctrl.release(new Point(7, 7)); // Release the mouse button to complete the square

        // Ensure that the square is drawn on the image
        TestUtils.goldenTest("squareTool", img);
        
    }
}
