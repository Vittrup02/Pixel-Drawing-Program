package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.OvalTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseButton;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

public class OvalToolTest {

    private Controller setupControllerWithOvalTool() {
        Image img = new Image(new Rect(20, 20));
        Controller ctrl = new Controller();
        ctrl.setImage(img);
        ctrl.setTool(new OvalTool());
        return ctrl;
    }

    @Test
    @DisplayName("can draw a bigger oval on the image")
    void canDrawBiggerOvalOnImage() throws IOException {
        Image img = new Image(new Rect(20, 20));

        // Set up the controller with the OvalTool
        Controller ctrl = new Controller().setTool(new OvalTool()).setImage(img);

        // Set the active color and draw an even bigger oval
        ctrl.setColor(Color.RED); // Set the active color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Set the active color to draw the oval
        ctrl.press(new Point(2, 2)); // Press the mouse button to start drawing (starting point)
        ctrl.update(new Point(18, 18)); // Update to draw the oval outline (ending point)
        ctrl.release(new Point(18, 18)); // Release the mouse button to complete the oval

        // Ensure that the even bigger oval is drawn on the image
        TestUtils.goldenTest("ovalTool", img);
    }

    @Test
    @DisplayName("isActive returns true when drawing and false when not")
    void testIsActive() throws IOException {
        Controller ctrl = setupControllerWithOvalTool();
    
        // Set the active color before starting to draw
        ctrl.setColor(Color.RED); // Set a default drawing color
        ctrl.setActiveColor(MouseButton.PRIMARY); // Activate the primary color for drawing
    
        // Initially, isActive should be false
        assertFalse(ctrl.getTool().isActive(), "Tool should not be active initially");
    
        // Simulate pressing the mouse to start drawing
        ctrl.press(new Point(5, 5));
        assertTrue(ctrl.getTool().isActive(), "Tool should be active after press");
    
        // Simulate releasing the mouse to stop drawing
        ctrl.release(new Point(10, 10));
        assertFalse(ctrl.getTool().isActive(), "Tool should not be active after release");
    }
    

    @Test
    @DisplayName("abandon stops drawing and resets tool's state")
    void testAbandon() throws IOException {
    Controller ctrl = setupControllerWithOvalTool();

    // Start drawing an oval
    ctrl.press(new Point(5, 5));
    assertTrue(ctrl.getTool().isActive(), "Tool should be active after press");

    // Abandon the drawing
    ctrl.abandon();

    // Verify that the tool is no longer active
    assertFalse(ctrl.getTool().isActive(), "Tool should not be active after abandon");

    // Additionally, you might want to verify that abandoning the drawing
    // did not leave any unintended marks on the canvas.
    // For example, compare the image before and after abandon:
    Image beforeDrawing = ctrl.getImage().clone();
    ctrl.press(new Point(5, 5));
    ctrl.abandon();
    Image afterAbandon = ctrl.getImage();
    assertTrue(beforeDrawing.equals(afterAbandon), "Image should remain unchanged after abandon");
}
}