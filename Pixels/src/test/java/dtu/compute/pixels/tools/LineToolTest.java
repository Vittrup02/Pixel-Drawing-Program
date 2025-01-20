package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.Observer;
import dtu.compute.pixels.controller.tools.LineTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class LineToolTest {

    @Test
    @DisplayName("LineTool does not draw when not active")
    void lineToolDoesNotDrawWhenNotActive() throws IOException {
        Controller ctrl = new Controller();
        Image img = new Image(new Rect(20, 20));
        img.reset(Color.WHITE); // Reset the image to a known state
        ctrl.setImage(img);
        ctrl.setColor(Color.BLACK); // Assume Color.BLACK is predefined

        LineTool lineTool = new LineTool();
        ctrl.setTool(lineTool);

        // Attempt to draw a line without first pressing
        Point updatePoint = new Point(10, 10);
        lineTool.update(ctrl, updatePoint); // This should not draw anything

        // Check that the image has not changed
        Image expectedImage = new Image(new Rect(20, 20));
        expectedImage.reset(Color.WHITE);

        assertEquals(expectedImage, ctrl.getImage(), "Image should not change when line tool is not active");

        // Check that the line tool is still not active
        assertFalse(lineTool.isActive(), "Line tool should not be active");
    }

    @Test
    @DisplayName("Abandoning LineTool resets its state")
    void abandoningLineToolResetsItsState() throws IOException {
        Controller ctrl = new Controller();
        Image img = new Image(new Rect(20, 20));
        img.reset(Color.WHITE);
        ctrl.setImage(img);
        ctrl.setColor(Color.BLACK);

        LineTool lineTool = new LineTool();
        ctrl.setTool(lineTool);

        // Simulate starting a line
        Point startPoint = new Point(5, 5);
        ctrl.press(startPoint);

        // Now abandon the drawing
        lineTool.abandon(ctrl);

        // The tool should no longer be active
        assertFalse(lineTool.isActive(), "Line tool should not be active after abandon");

        // Check that the image has not changed (since the line was not committed)
        Image expectedImage = new Image(new Rect(20, 20));
        expectedImage.reset(Color.WHITE);
        assertEquals(expectedImage, ctrl.getImage(), "Image should not change when line drawing is abandoned");
    }

    @Test
    @DisplayName("isActive returns false when not drawing")
    void isActiveReturnsFalseWhenNotDrawing() throws IOException {
        Controller ctrl = new Controller();
        LineTool lineTool = new LineTool();
        ctrl.setTool(lineTool);
        // Initially, the tool should not be active
        assertFalse(lineTool.isActive(), "Line tool should not be active initially");

        // Simulate starting a line
        Point startPoint = new Point(5, 5);
        ctrl.press(startPoint);

        // The tool should be active after pressing
        assertTrue(lineTool.isActive(), "Line tool should be active after press");

        // Abandon the line
        lineTool.abandon(ctrl);

        // The tool should no longer be active after abandoning
        assertFalse(lineTool.isActive(), "Line tool should not be active after abandon");
    }

    @Test
    @DisplayName("LineTool draws a line on the image")
    void lineToolDrawsLineOnImage() throws IOException {
        Controller ctrl = new Controller();
        Image img = new Image(new Rect(20, 20));
        img.reset(Color.WHITE); // Reset the image to a known state
        ctrl.setImage(img);
        ctrl.setColor(Color.BLACK);
        ctrl.setActiveColor(MouseButton.PRIMARY);

        LineTool lineTool = new LineTool();
        ctrl.setTool(lineTool);

        // Simulate the action of drawing a line
        Point startPoint = new Point(5, 5);
        Point endPoint = new Point(15, 15);
        ctrl.press(startPoint);
        ctrl.update(endPoint);
        ctrl.release(endPoint);

        // Compare the resulting image with the golden image
        TestUtils.goldenTest("lineTool", img);
    }

@Test
@DisplayName("LineTool only draws after being activated")
void lineToolOnlyDrawsAfterBeingActivated() throws IOException {
    Controller ctrl = new Controller();
        Image img = new Image(new Rect(20, 20));
        img.reset(Color.WHITE); // Reset the image to a known state
        ctrl.setImage(img);
        ctrl.setColor(Color.BLACK);
        ctrl.setActiveColor(MouseButton.PRIMARY);


    LineTool lineTool = new LineTool();
    ctrl.setTool(lineTool);

    // Attempt to draw without pressing
    Point updatePoint = new Point(10, 10);
    lineTool.update(ctrl, updatePoint); // Should not draw anything

    // Create a golden image which should remain unchanged
    Image goldenImage = new Image(new Rect(20, 20));
    goldenImage.reset(Color.WHITE);

    // Compare the resulting image with the golden image
    TestUtils.goldenTest("lineToolActivation", img);
}
}

/*
 * @Test
 * 
 * @DisplayName("User can draw a line with the LineTool")
 * void userCanDrawLineWithLineTool() throws IOException {
 * // Initialize the controller with an image
 * Controller ctrl = new Controller();
 * Image img = new Image(new Rect(20, 20));
 * img.reset(Color.WHITE);
 * ctrl.setImage(img);
 * ctrl.setColor(Color.BLACK); // Assume Color.BLACK is predefined
 * ctrl.setActiveColor(MouseButton.PRIMARY); // Simulate primary mouse button
 * 
 * // Set up the line tool
 * LineTool lineTool = new LineTool();
 * ctrl.setTool(lineTool);
 * 
 * Point startPoint = new Point(5, 5);
 * Point endPoint = new Point(15, 12);
 * 
 * // Add an observer to the controller
 * TestObserver observer = new TestObserver();
 * ctrl.addObserver(observer);
 * 
 * // Simulate drawing a line with LineTool
 * ctrl.press(startPoint).release(endPoint);
 * 
 * // Check if the observer was notified of changes
 * assertTrue(observer.hasBeenNotified(),
 * "Observer should be notified after drawing a line");
 * 
 * TestUtils.goldenTest("line", ctrl.getImage());
 * 
 * // Ensure the line tool is no longer active
 * assertFalse(lineTool.isActive(),
 * "Line tool should not be active after release");
 * 
 * // Remove observer and ensure it's no longer notified
 * assertTrue(ctrl.removeObserver(observer),
 * "Observer should be successfully removed.");
 * }
 * 
 * // Auxiliary class to test observer notifications
 * private static class TestObserver implements Observer {
 * private boolean notified = false;
 * 
 * @Override
 * public void onChange() {
 * notified = true;
 * }
 * 
 * public boolean hasBeenNotified() {
 * return notified;
 * }
 * }
 * }
 * 
 */
